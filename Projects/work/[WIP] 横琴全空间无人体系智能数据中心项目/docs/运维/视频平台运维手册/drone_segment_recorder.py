#!/usr/bin/env python3
"""
无人机视频分段录制服务（v2.5）
MQTT 监听 /ucare/third/uavState，按 historyId 分段录制视频

逻辑：
- height > 0 且 historyId 有效 → 启动 FFmpeg 录制（每个 historyId 独立进程）
- height == 0 或 MQTT 静默 30s → 停止录制、faststart 重封装、合并同 ID 片段
- 文件保存在 /opt/media/www/record/cszl/drone-segment/YYYY-MM-DD/
- 与原 ZLM 连续录制并行运行，互不干扰
"""

import json
import os
import signal
import subprocess
import sys
import threading
import time
from datetime import datetime

# 尝试导入 paho-mqtt，未安装则退出
try:
    import paho.mqtt.client as mqtt
except ImportError:
    print("ERROR: paho-mqtt 未安装，请执行: pip3 install paho-mqtt==1.6.1")
    sys.exit(1)

# ==================== 配置 ====================
MQTT_BROKER = "10.129.80.225"
MQTT_PORT = 1883
MQTT_USER = "cszl"
MQTT_PASS = "cszl@2026"
MQTT_TOPIC = "/ucare/third/uavState"

# 源流地址（脚本内会根据 MQTT 消息中的 rtmpStreamUrl 自动替换代理地址）
STREAM_URL = "rtmp://127.0.0.1/cszl/drone"

# 输出目录
SEGMENTS_DIR = "/opt/media/www/record/cszl/drone-segment"
LOG_FILE = "/opt/media/logs/drone_segment.log"

# FFmpeg 路径（优先使用 /opt/media/tools/ 下的 ffmpeg）
FFMPEG_PATH = "/opt/media/tools/ffmpeg"
if not os.path.exists(FFMPEG_PATH):
    FFMPEG_PATH = "ffmpeg"

# 代理服务器配置（用于替换 MQTT 消息中的源地址）
PROXY_HOST = "10.129.80.244"
PROXY_PORT = 21020

# 超时配置
HISTORY_SILENCE_TIMEOUT = 30  # 单个 historyId 静默超时（秒）

# ==================== 全局状态 ====================
# 必须用 RLock，避免 on_message / watchdog / stop_recording 之间死锁
lock = threading.RLock()
recordings = {}  # historyId -> {proc, start_ts, tmp_path, last_msg_ts, stream_url}

# ==================== 工具函数 ====================

def log(msg):
    ts = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    line = f"[{ts}] {msg}"
    print(line, flush=True)
    try:
        with open(LOG_FILE, "a", encoding="utf-8") as f:
            f.write(line + "\n")
    except Exception:
        pass

def ensure_dir(date_str):
    """按日期创建子目录，返回完整路径"""
    d = os.path.join(SEGMENTS_DIR, date_str)
    os.makedirs(d, exist_ok=True)
    return d

def replace_proxy(url):
    """将 MQTT 消息中的源端地址替换为代理服务器地址"""
    if not url:
        return STREAM_URL
    # 简单替换 host:port 为代理地址
    import re
    # rtmp://10.129.23.248:21019/... -> rtmp://10.129.80.244:21020/...
    replaced = re.sub(
        r"rtmp://[^/]+:\d+",
        f"rtmp://{PROXY_HOST}:{PROXY_PORT}",
        url,
    )
    return replaced

def faststart_remux(src_path, dst_path, creation_time_str=None):
    """将 moov 头移到文件开头，注入 creation_time，支持播放器拖动进度条"""
    cmd = [
        FFMPEG_PATH, "-y",
        "-i", src_path,
        "-c", "copy",
        "-movflags", "+faststart",
    ]
    if creation_time_str:
        cmd += ["-metadata", f"creation_time={creation_time_str}"]
    cmd.append(dst_path)
    try:
        subprocess.run(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, check=True)
        os.replace(dst_path, src_path)
        return True
    except Exception as e:
        log(f"WARN: faststart failed: {e}")
        return False

def merge_history_segments(date_str, history_id):
    """合并同一天同一 historyId 的所有片段"""
    day_dir = os.path.join(SEGMENTS_DIR, date_str)
    if not os.path.isdir(day_dir):
        return

    pattern = f"{history_id}_"
    files = sorted([
        f for f in os.listdir(day_dir)
        if f.startswith(pattern) and f.endswith(".mp4") and "_recording" not in f
    ])

    if len(files) <= 1:
        return

    list_path = os.path.join(day_dir, f".{history_id}_concat_list.txt")
    with open(list_path, "w", encoding="utf-8") as f:
        for name in files:
            f.write(f"file '{os.path.join(day_dir, name)}'\n")

    merged_name = f"{history_id}_merged_{datetime.now().strftime('%H%M%S')}.mp4"
    merged_path = os.path.join(day_dir, merged_name)

    # 用第一个片段的开始时间作为合并后文件的 creation_time（文件名是本地时间，转 UTC）
    import time
    first_start_str = files[0].split('_')[1]
    local_dt = datetime.strptime(first_start_str, "%Y%m%d%H%M%S")
    timestamp = time.mktime(local_dt.timetuple())
    creation_time = datetime.utcfromtimestamp(timestamp).strftime("%Y-%m-%dT%H:%M:%S.000000Z")

    cmd = [
        FFMPEG_PATH, "-y",
        "-f", "concat",
        "-safe", "0",
        "-i", list_path,
        "-c", "copy",
        "-metadata", f"creation_time={creation_time}",
        merged_path,
    ]
    try:
        subprocess.run(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL, check=True)
        # 合并成功后删除原始片段
        for name in files:
            os.remove(os.path.join(day_dir, name))
        # 对合并后的文件再做 faststart
        faststart_remux(merged_path, merged_path + ".tmp", creation_time)
        log(f"[MERGED] {merged_path} (from {len(files)} segments)")
    except Exception as e:
        log(f"WARN: merge failed: {e}")
    finally:
        if os.path.exists(list_path):
            os.remove(list_path)

def build_final_name(history_id, start_ts, end_ts):
    """生成最终文件名: {historyId}_{start_time}_{end_time}.mp4"""
    start_str = datetime.fromtimestamp(start_ts).strftime("%Y%m%d%H%M%S")
    end_str = datetime.fromtimestamp(end_ts).strftime("%Y%m%d%H%M%S")
    return f"{history_id}_{start_str}_{end_str}.mp4"

def start_recording(history_id, stream_url):
    """启动 FFmpeg 录制（每个 historyId 独立）"""
    with lock:
        if history_id in recordings:
            return

        date_str = datetime.now().strftime("%Y-%m-%d")
        day_dir = ensure_dir(date_str)
        start_ts = int(time.time())
        tmp_path = os.path.join(day_dir, f"{history_id}_{start_ts}_recording.mp4")

        cmd = [
            FFMPEG_PATH, "-y",
            "-fflags", "+genpts",
            "-i", stream_url,
            "-c", "copy",
            "-movflags", "+frag_keyframe+empty_moov",
            "-f", "mp4",
            tmp_path,
        ]

        log(f"[START] historyId={history_id}, stream={stream_url}")
        try:
            proc = subprocess.Popen(
                cmd,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
            )
        except FileNotFoundError:
            log(f"ERROR: FFmpeg 未找到: {FFMPEG_PATH}")
            return

        recordings[history_id] = {
            "proc": proc,
            "start_ts": start_ts,
            "tmp_path": tmp_path,
            "last_msg_ts": time.time(),
            "stream_url": stream_url,
        }

def stop_recording(history_id, reason=""):
    """停止 FFmpeg 录制、faststart、合并同 ID 片段"""
    with lock:
        rec = recordings.pop(history_id, None)
        if rec is None:
            return

    proc = rec["proc"]
    tmp_path = rec["tmp_path"]
    start_ts = rec["start_ts"]

    log(f"[STOP] historyId={history_id}, reason={reason}")

    # 发送 SIGTERM 让 FFmpeg 优雅退出
    proc.terminate()
    try:
        proc.wait(timeout=8)
    except subprocess.TimeoutExpired:
        log("WARN: FFmpeg 未在 8s 内退出，强制 kill")
        proc.kill()
        proc.wait()

    end_ts = int(time.time())
    date_str = datetime.fromtimestamp(start_ts).strftime("%Y-%m-%d")
    day_dir = os.path.join(SEGMENTS_DIR, date_str)
    final_name = build_final_name(history_id, start_ts, end_ts)
    final_path = os.path.join(day_dir, final_name)

    try:
        if os.path.exists(tmp_path):
            os.rename(tmp_path, final_path)
            # faststart 重封装 + 注入 creation_time（UTC，与文件名本地时间对应）
            creation_time = datetime.utcfromtimestamp(start_ts).strftime("%Y-%m-%dT%H:%M:%S.000000Z")
            faststart_remux(final_path, final_path + ".tmp", creation_time)
            log(f"[SAVED] {final_path}")
            # 合并同一天同 historyId 的片段
            merge_history_segments(date_str, history_id)
        else:
            log(f"WARN: 临时文件不存在: {tmp_path}")
    except Exception as e:
        log(f"ERROR: 保存失败: {e}")

# ==================== MQTT 回调 ====================

def on_connect(client, userdata, flags, rc):
    if rc == 0:
        log(f"MQTT connected, subscribe: {MQTT_TOPIC}")
        client.subscribe(MQTT_TOPIC)
    else:
        log(f"MQTT connect failed, rc={rc}")

def on_disconnect(client, userdata, rc):
    log(f"MQTT disconnected, rc={rc}")

def on_message(client, userdata, msg):
    try:
        payload = json.loads(msg.payload.decode("utf-8"))
    except Exception as e:
        log(f"ERROR: JSON parse failed: {e}")
        return

    history_id_raw = payload.get("historyId")
    height_raw = payload.get("height")
    rtmp_url = payload.get("rtmpStreamUrl", "")

    # 过滤无效数据
    if history_id_raw is None or history_id_raw == 0 or height_raw is None:
        return

    try:
        history_id = str(int(float(history_id_raw)))
    except (ValueError, TypeError):
        return

    try:
        height = float(height_raw)
    except (ValueError, TypeError):
        return

    stream_url = replace_proxy(rtmp_url) if rtmp_url else STREAM_URL

    with lock:
        rec = recordings.get(history_id)
        if rec is not None:
            # 更新最后消息时间
            rec["last_msg_ts"] = time.time()

            if height == 0.0:
                # height 降为 0，停止录制
                # 注意：不要在持锁时调用 stop_recording，它内部也会抢锁
                # 但这里用的是 RLock，所以安全
                stop_recording(history_id, reason="height=0")
        else:
            if height > 0:
                start_recording(history_id, stream_url)

# ==================== 静默监控线程 ====================

def silence_watchdog():
    """监控每个 historyId 的 MQTT 消息静默时间，超时时自动停止录制"""
    while True:
        time.sleep(5)
        now = time.time()
        with lock:
            # 复制 keys 避免遍历时修改
            ids = list(recordings.keys())

        for history_id in ids:
            with lock:
                rec = recordings.get(history_id)
                if rec is None:
                    continue
                if now - rec["last_msg_ts"] > HISTORY_SILENCE_TIMEOUT:
                    # 静默超时，停止录制（RLock 可重入，安全）
                    stop_recording(history_id, reason="silence_timeout")

# ==================== 主函数 ====================

def main():
    os.makedirs(SEGMENTS_DIR, exist_ok=True)
    log("=" * 50)
    log("Drone Segment Recorder v2.5 started")
    log(f"FFMPEG_PATH={FFMPEG_PATH}")
    log(f"SEGMENTS_DIR={SEGMENTS_DIR}")
    log(f"PROXY={PROXY_HOST}:{PROXY_PORT}")
    log(f"SILENCE_TIMEOUT={HISTORY_SILENCE_TIMEOUT}s")

    # 启动静默监控线程
    t = threading.Thread(target=silence_watchdog, daemon=True)
    t.start()

    client = mqtt.Client(client_id="drone_segment_recorder_" + str(os.getpid()))
    client.username_pw_set(MQTT_USER, MQTT_PASS)
    client.on_connect = on_connect
    client.on_message = on_message
    client.on_disconnect = on_disconnect

    def signal_handler(sig, frame):
        log(f"Received signal {sig}, shutting down...")
        with lock:
            ids = list(recordings.keys())
        for history_id in ids:
            stop_recording(history_id, reason="signal_shutdown")
        client.disconnect()
        sys.exit(0)

    signal.signal(signal.SIGTERM, signal_handler)
    signal.signal(signal.SIGINT, signal_handler)

    # 断线自动重连循环
    while True:
        try:
            client.connect(MQTT_BROKER, MQTT_PORT, keepalive=60)
            client.loop_forever()
        except Exception as e:
            log(f"MQTT exception: {e}, retry in 5s...")
            time.sleep(5)

if __name__ == "__main__":
    main()
