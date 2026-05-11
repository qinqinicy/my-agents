# MEMORY.md - Long-Term Memory

*Your curated memories. The distilled essence, not raw logs.*

## About This File & Memory System

- **Be mindful in shared contexts** — this file contains personal context about your human. In group chats or shared sessions, don't leak private preferences, decisions, or project details

### Three-Layer Memory

Your memory has three layers, each with different responsibilities and access patterns:

**Core memory (this file, 04-MEMORY.md)** — Auto-loaded every session
- What goes here: cross-project lessons, key decisions, user preferences, technical knowledge, one-line project summaries + pointers
- What doesn't: detailed project experience (that's what topic files are for)
- **Add a timestamp `(YYYY-MM-DD)` to each entry** — helps trace back, judge recency, clean up

**Topic memory (`AgentSystem/memory/topics/work/<name>.md` | `personal/<name>.md` | `public/<name>.md`)** — Read before working on a project
- What goes here: full accumulated experience for one project/topic — status, key facts, what you did, what worked, what didn't, decisions and rationale, next steps
- More detailed than core memory (which only has pointers), more synthesized than daily logs (which are raw chronological notes)
- Update during memory maintenance or when a project enters a new phase

**Daily journal (`AgentSystem/memory/YYYY-MM-DD.md`)** — Read today + yesterday at session start
- What goes here: what happened that day, raw chronological record
- This is the source of all memory, but searching it for specific project info is inefficient (multiple projects mixed in one day)

### Information Flow

```
Daily logs (raw material) → topic files (synthesized per-project) → 04-MEMORY (cross-project essence)
```

- During work: just write the daily log
- During maintenance: sync from logs to topics, distill new cross-project lessons to this file
- **Information lives in one place only** — don't duplicate between topic files and 04-MEMORY

### When to Read What

- Just woke up → this file is already loaded + read today/yesterday's logs
- About to work on a project → read its `AgentSystem/memory/topics/work/<name>.md` or `personal/<name>.md`
- Memory maintenance → read all recent logs + all active topic files

---

## Lessons Learned

Organize by topic as your lessons grow. A flat list becomes unreadable fast.

### Working Style

- **工作区划分** (2026-03-02): `Projects/` 是项目资产仓库，`Knowledge/` 和 `Archives/` 是跨项目知识库和归档库，`AgentSystem/memory/topics/` 是档案索引，`SilentSpace/` 是每日草稿纸。项目文件存仓库，临时文件用完即弃。
- **记忆系统初始化** (2026-03-02): 为 4 个项目创建记忆档案：横琴项目、软考备考、知识图谱产品、交通部招聘笔试。
- **工作区重构** (2026-03-18):
  - 核心分区标签：`work/` 和 `personal/`（小写统一）
  - `Knowledge/` 提升至根目录，下设 `work/`、`personal/`、`public/`（公共知识融合）
  - `Archives/` 提升至根目录，下设 `work/`、`personal/`，增加 `ARCHIVES_LOG.md` 归档日志
  - `Projects/` 下使用状态前缀管理项目：`[WIP]` 进行中、`[HLD]` 暂停；已完成项目直接归档至 `Archives/`
  - 文件流转：`Loading_files/`（基础资料）→ `Projects/`（项目管理）→ `SilentSpace/`（临时工作台）→ `Archives/`（归档）→ `Knowledge/`（沉淀）
  - 项目索引：`Projects/PROJECTS.md` 维护全局项目清单和映射关系
- **工作区配置统一** (2026-04-23):
  - `.claude/` 为实体目录（原在 `AgentSystem/.claude/`），统一维护一份配置
  - `myagents_files/` 更名为 `Outputs/`，明确输出文档位置
  - `Projects/work/` 项目内部结构统一为 `docs/{设计,运维,平台,其他}/` 子分类
  - 孔明平台作为横琴项目的子平台，不再是独立项目

### Communication

### Technical

- **Spring Boot JAR 打包** (2026-03-17): Nested JAR 必须使用 STORED（无压缩）模式。错误："compressed and nested jar files must be stored without compression"。解决：`zip -0` 处理 lib/*.jar，其余文件正常压缩。
- **JWT 字段兼容性** (2026-03-17): iPaas OAuth 使用标准 JWT 字段（sub, name），代码需兼容多种字段名（userId/sub/userid, userName/name/username）。
- **ZLMediaKit Docker 网络** (2026-04-22): Docker 容器必须使用 `--network host`，否则跨容器通信失败（"wait http response header timeout"）。
- **ZLMediaKit addStreamProxy** (2026-04-22): 必须传 `vhost=__defaultVhost__` 参数，否则报 "Required parameter missed: vhost"。
- **rclone serve IPv4 绑定** (2026-04-22): rclone serve 默认绑定 IPv6（`[::]:port`），需加 `--bind 0.0.0.0` 显式绑定 IPv4。
- **rclone sync 正在录制文件** (2026-04-22): 同步正在写入的文件会报 "corrupted on transfer: sizes differ"，需排除 `.` 开头的临时文件：`--exclude "**/.*"`。
- **ZLM FFmpegSource fork 失效** (2026-04-24): ZLM 在 Docker + seccomp=2 下无法 spawn FFmpeg 进程，addFFmpegSource 返回成功但日志静默。换用 SRS 5.0 ingest 功能替代。
- **SRS ingest 拉流** (2026-04-24): SRS 内置 ingest 调用容器内 ffmpeg 拉取外部 RTMP 源，可替代 ZLM addStreamProxy。配置简单，稳定可靠。
- **StreamUI 路径问题** (2026-04-22): StreamUI 镜像内写死了 `/opt/media/bin/www`，需创建 symlink `ln -sf /opt/media/www /opt/media/bin/www`。
- **ZLM addStreamProxy RTMP 限制** (2026-04-23): ZLM addStreamProxy 对部分 RTMP URL 格式校验严格，无法直接拉取时用 ffmpeg 中转方案：ffmpeg 拉取外部 RTMP → 推送 `rtmp://127.0.0.1/live/{stream}` → ZLM 接收录制。systemd 服务管理 ffmpeg，确保断线自动重启。
- **运维手册模板** (2026-04-22): 创建 `.claude/templates/01-运维手册规范.md`，规范运维文档的内容分类（10/8章结构）、排版格式（标题层级、表格、代码块、警告提示）、HTML 转换规则（封面页、目录、样式）。适用单一大规模软件用精简8章，通用企业IT系统用完整10章。
- **Oryx start_redis 递归问题** (2026-04-27): Oryx 容器内 `auto/start_redis` 被 bootstrap 调用，不能用 `exec bootstrap` 替换进程，否则递归调用。正确做法：加载环境变量后直接返回。
- **Oryx load_env 不存在** (2026-04-27): Oryx 容器内 `auto/load_env` 文件不存在，实际从 `containers/data/config/.env` 读取环境变量。
- **Oryx 8080 端口 errno=98** (2026-04-27): Oryx Go 服务绑定 0.0.0.0:8080 失败，ss 显示无占用但实际 bind 失败，可能与容器网络模式或 SRS 残留进程有关。**方案搁置，用户回归 ZLM + StreamUI**。
- **IoTDB 时序路径命名** (2026-05-08): 存储组/场景/数据表三级结构，如 root.dhq.ugv_status、root.jincheng.device
- **腾讯云 WireGuard 防火墙** (2026-05-08): 云服务器有 YJ-FIREWALL-INPUT 自定义链，默认 DROP；放行 UDP 1194 需 `iptables -I YJ-FIREWALL-INPUT 1 -p udp --dport 1194 -j ACCEPT`；MASQUERADE 需手动添加
- **WireGuard 密钥对生成** (2026-05-10): `wg genkey` 产私钥，`wg pubkey` 产公钥，严禁混用填反；填反后 WireGuard 不报错，只是握手静默丢弃，排查需结合 `wg show` + tcpdump；`wg set` 不持久化，必须写入 wg0.conf
- **IoTDB CLI 多语句执行** (2026-05-08): CLI 的 `-e` 参数内多语句用分号分隔，整个 SQL 字符串用双引号包裹
- **流命名规范化** (2026-04-29): machinenest（机巢）、drone（无人机）、5gacamera（5G-A摄像头），录制路径 `{场景}/{流ID}/`
- **rclone sync 按场景分流** (2026-04-29): sync_by_scene.sh 遍历场景目录，检查桶存在性后同步到 `minio:{scene}-media/`
- **MinIO IAM 权限管理** (2026-04-29): cszlkey + cszl-readonly 策略，只读访问 cszl-media 桶

## Important Decisions

- **工作区结构** (2026-03-02): 采用 `Projects/Work/` 和 `Projects/Personal/` 区分工作项目和个人项目，各自有 `Archive/` 归档和 `Knowledge/` 知识库。
- **工作区重构** (2026-03-18):
  - `workspace/` → `silentspace/`（临时工作台）
  - `myagents_files/` → `Loading_files/`（引用文件仓库）
  - `memory/topics/` 分为 `work/`、`personal/`、`public/` 三个子目录
  - `Work/` 和 `Personal/` → `Projects/Work/` 和 `Projects/Personal/`（统一项目资产管理）
  - `Knowledge/` 和 `Archives/` 提升至根目录（跨项目知识/归档）
  - 统一大小写：`projects/` → `Projects/`，`active/` → `Active/`
- **工作区简化** (2026-03-18):
  - 核心分区：`work/` 和 `personal/`（小写）
  - `Projects/` 下移除 `Projects/` 和 `Active/` 子目录，改用状态前缀：`[WIP]`、`[HLD]`
  - `SilentSpace/`、`Loading_files/`、`Archives/`、`Knowledge/` 均按 `work/`、`personal/` 分类
  - `Knowledge/` 新增 `public/` 融合公共知识
- **工作区最终简化** (2026-03-19):
  - 移除 `[OK]` 状态，已完成项目直接归档至 `Archives/`（带 `[ARC]` 前缀）
  - 创建 `Projects/PROJECTS.md` 全局项目索引，维护所有项目状态和映射关系
  - 创建 `Archives/ARCHIVES_LOG.md` 归档日志，记录归档详情和知识沉淀路径
  - 项目状态前缀统一：`[WIP]`（进行中）、`[HLD]`（暂停）、`[ARC]`（已归档）
- **Outputs 目录与 Work 项目结构** (2026-04-23):
  - `myagents_files/` → `Outputs/`，Agent 生成输出放此目录
  - `Projects/work/` 项目内部统一为 `docs/{设计,运维,平台,其他}/` 子分类
  - 孔明平台并入横琴项目作为子平台 `docs/平台/孔明/`

## User Preferences

- 用户同时处理工作项目（横琴数据中心）和个人提升（软考、交通部招聘考试）
- 偏好用中文交流和记录
- AI 名称：清 (2026-03-18)

## Technical Knowledge

## Ongoing Context

### Active Projects
| 项目 | 类型 | 状态 | 记忆档案 |
|------|------|------|---------|
| 横琴全空间无人体系智能数据中心 | Work | 详细设计阶段 | `AgentSystem/memory/topics/work/横琴全空间无人体系智能数据中心.md` |
| 视频平台运维（ZLM+StreamUI+MinIO）| Work | 运维阶段 | `AgentSystem/memory/topics/work/视频平台运维.md` |
| 2026 软考系统架构师备考 | Personal | 备考中 | `AgentSystem/memory/topics/personal/软考备考.md` |
| 交通部第四批招聘笔试备考 | Personal | 已归档 | `AgentSystem/memory/topics/personal/交通部招聘笔试.md` |
| 知识图谱产品手册 | Work | 知识库建设 | `AgentSystem/memory/topics/work/知识图谱产品.md` |

### Recent Work (2026-04/05)
- **流命名规范化** (2026-04-29): machinenest/drone/5gacamera，录制路径 {场景}/{流ID}/，rclone sync 按场景分流
- **MinIO IAM** (2026-04-29): cszlkey 只读访问 cszl-media，S3 API 对外服务
- **IoTDB 场景接入** (2026-05-08): 大横琴无人车(root.dhq, 48时序)、金城无人机(root.jincheng, 17时序)
- **WireGuard VPN 部署** (2026-05-08/10): 腾讯云 129.204.87.213 服务端运行中，Mac + Win1 已连通（10.0.0.0/24），Win2 待连接，Win1 RDP 连接调试中

---

*Update this file as you learn. It's how you persist.*
