---
title: IoTDB时序数据库运维手册
version: v1.0
date: 2026-04-22
author: qin
reviewer: 秦
status: 正式发布
---

# IoTDB 时序数据库运维手册

> 横琴全空间无人体系智能数据中心项目

---

## 1. 概述

### 1.1 手册目的与范围

本手册旨在为 IoTDB 2.0.7 集群提供完整的运维操作指南，涵盖安装配置、日常运维、数据接入、故障排除等全生命周期管理。

**适用范围**：
- IoTDB 2.0.7 集群部署与运维
- 城市治理、运营中心、智慧灯杆等场景数据接入
- REST API / MQTT 等协议接入配置

### 1.2 系统简介

IoTDB（Internet of Things Database）是一款专为物联网时序数据设计的国产数据库，支持集群部署、MQTT 协议原生接入、REST API 操作。

| 组件 | 说明 |
|------|------|
| ConfigNode | 集群配置管理节点，负责元数据管理和调度 |
| DataNode | 数据存储节点，负责时序数据读写 |
| Schema Region | 元数据Region，管理时序元数据 |
| Data Region | 数据Region，管理时序数据 |
| 内置MQTT Broker | 原生支持MQTT协议数据接入 |
| REST API | HTTP协议数据访问接口 |

### 1.3 术语与缩写

| 缩写 | 全称 | 说明 |
|------|------|------|
| TS | Time Series | 时间序列 |
| SG | Storage Group | 存储组 |
| MQTT | Message Queuing Telemetry Transport | 物联网消息传输协议 |
| RPC | Remote Procedure Call | 远程过程调用 |
| CLI | Command Line Interface | 命令行接口 |

### 1.4 参考文献

- IoTDB 官方文档：https://iotdb.apache.org/
- REST API v2：/rest/v2/nonQuery（写入）、/rest/v2/query（查询）
- MQTT 接入：port 1883，JSON 格式

---

## 2. 系统架构

### 2.1 硬件环境

| 项目 | 配置 |
|------|------|
| 集群节点 | 10.129.80.142 / 10.129.80.201 / 10.129.80.230 |
| CPU | x86_64 多核 |
| 内存 | ≥8GB |
| 磁盘 | SSD，推荐 ≥100GB |

### 2.2 软件架构

```
                    ┌─────────────────┐
                    │   应用系统       │
                    └───────┬─────────┘
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
        ┌──────────┐  ┌──────────┐  ┌──────────┐
        │REST API │  │  MQTT    │  │  CLI     │
        │ port    │  │  port    │  │          │
        │ 18080   │  │  1883    │  │  6667    │
        └────┬─────┘  └────┬─────┘  └────┬─────┘
             │             │             │
             └─────────────┼─────────────┘
                           ▼
              ┌──────────────────────────┐
              │      DataNode (x3)        │
              │  ┌────────────────────┐  │
              │  │ Data Region (副本2) │  │
              │  └────────────────────┘  │
              │  ┌────────────────────┐  │
              │  │ Schema Region (副本3)│  │
              │  └────────────────────┘  │
              └──────────────────────────┘
                           ▲
                           │
              ┌──────────────────────────┐
              │      ConfigNode (x3)     │
              │    集群元数据管理         │
              └──────────────────────────┘
```

### 2.3 网络拓扑

| 节点 | IP | 端口 | 用途 |
|------|-----|------|------|
| DataNode-1 | 10.129.80.142 | 6667 | RPC通信 |
| DataNode-2 | 10.129.80.201 | 6667 | RPC通信 |
| DataNode-3 | 10.129.80.230 | 6667 | RPC通信 |
| ConfigNode | 各节点 | 10710 | 集群管理 |
| REST API | 各节点 | 18080 | HTTP访问 |
| MQTT Broker | 各节点 | 1883 | 物联网接入 |

### 2.4 第三方依赖

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | ≥1.8 | 运行要求 |
| 网络 | TCP/IP | 节点间通信 |

---

## 3. 安装配置

### 3.1 服务端口配置

在 `iotdb-system.properties` 中配置：

```properties
# RPC通信端口
rpc_port=6667

# REST服务端口
enable_rest_service=true
rest_service_port=18080

# MQTT服务端口
enable_mqtt_service=true
mqtt_port=1883

# ConfigNode端口
confignode_port=10710

# Schema Region端口
schema_region_port=10750

# Data Region端口
data_region_port=10760
```

### 3.2 配置文件说明

| 配置文件 | 路径 | 说明 |
|----------|------|------|
| iotdb-system.properties | /data/iotdb/conf/ | 系统配置（端口、服务开关） |
| iotdb-confignode.properties | /data/iotdb/conf/ | ConfigNode配置 |
| iotdb-datanode.properties | /data/iotdb/conf/ | DataNode配置 |
| log_datanode_all.log | /data/iotdb/logs/ | DataNode日志 |
| log_confignode_all.log | /data/iotdb/logs/ | ConfigNode日志 |

### 3.3 服务启停

**启动 DataNode**：
```bash
$IOTDB_HOME/sbin/start-datanode.sh
```

**停止 DataNode**：
```bash
$IOTDB_HOME/sbin/stop-datanode.sh
```

**启动 ConfigNode**：
```bash
$IOTDB_HOME/sbin/start-confignode.sh
```

**停止 ConfigNode**：
```bash
$IOTDB_HOME/sbin/stop-confignode.sh
```

**一键启动集群**：
```bash
$IOTDB_HOME/sbin/start-standalone.sh
```

**一键停止集群**：
```bash
$IOTDB_HOME/sbin/stop-standalone.sh
```

> **注意**：修改配置后需重启对应服务生效。

---

## 4. 日常运维

### 4.1 连接与认证

**CLI 连接**：
```bash
$IOTDB_HOME/sbin/start-cli.sh -h 10.129.80.142 -p 6667 -u root -pw root
```

**快捷执行**：
```bash
$IOTDB_HOME/sbin/start-cli.sh -h 10.129.80.142 -p 6667 -u root -pw root -e "SHOW DATABASES"
```

> **提示**：默认用户名/密码为 root/root，生产环境请修改。

### 4.2 存储组管理

**创建存储组**：
```sql
CREATE DATABASE root.cszl
```

**查看存储组**：
```sql
SHOW DATABASES
```

**删除存储组**：
```sql
DROP DATABASE root.cszl
```

### 4.3 时间序列管理

**创建单个时间序列**：
```sql
CREATE TIMESERIES root.cszl.uav.flyId WITH DATATYPE=TEXT, ENCODING=PLAIN
```

**批量创建（城市治理场景 31 个字段）**：
```sql
CREATE TIMESERIES root.cszl.uav.alt WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.contacts WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.deviceName WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.direction WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.flvStreamUrl WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.flyId WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.gimbalPitch WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.height WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.historyId WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.httpsFlvStreamUrl WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserError WITH DATATYPE=INT32, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserMeasureFlag WITH DATATYPE=BOOLEAN, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetLocationAls WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetLocationDistance WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetLocationLat WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetLocationLng WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetPointX WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.laserTargetPointY WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.lat WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.latSource WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.lon WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.lonSource WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.mobilePhone WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.power WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.rtmpStreamUrl WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.rtspStreamUrl WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.spId WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.spName WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.speed WITH DATATYPE=DOUBLE, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.tkId WITH DATATYPE=TEXT, ENCODING=PLAIN
CREATE TIMESERIES root.cszl.uav.tkName WITH DATATYPE=TEXT, ENCODING=PLAIN
```

**查看与删除**：
```sql
SHOW TIMESERIES root.cszl.uav
SHOW DEVICES root.cszl.uav
DELETE TIMESERIES root.cszl.uav.flyId
DELETE TIMESERIES root.cszl.uav.*
```

### 4.4 数据操作

**INSERT 单行**：
```sql
INSERT INTO root.cszl.uav (time, flyId, lat, lon, alt, contacts, deviceName, direction, speed, height)
VALUES (1776747236000, 'D0101209', 22.161540985107423, 113.5276870727539, 7.15, '卢忠尉', '金融岛中央公园', -83.9000015258789, 0.0, 0.0)
```

**INSERT 多行**：
```sql
INSERT INTO root.cszl.uav (time, flyId, lat, lon)
VALUES (1776747236000, 'D0101209', 22.1, 113.5),
       (1776747237000, 'D0101210', 22.2, 113.6)
```

**查询数据**：
| 操作 | SQL |
|------|-----|
| 查询所有数据 | `SELECT * FROM root.cszl.uav` |
| 查询指定设备 | `SELECT * FROM root.cszl.uav.D0101209` |
| 条件查询 | `SELECT * FROM root.cszl.uav WHERE time > 1776747236000` |
| 限制行数 | `SELECT * FROM root.cszl.uav LIMIT 10` |
| 时间范围 | `SELECT * FROM root.cszl.uav WHERE time >= 1776747236000 AND time <= 1776747240000` |
| 聚合统计 | `SELECT COUNT(*) FROM root.cszl.uav` |
| 平均值 | `SELECT AVG(lat) FROM root.cszl.uav` |
| 最大值 | `SELECT MAX(alt) FROM root.cszl.uav WHERE time > 1776747236000` |
| 最新数据 | `SELECT LAST * FROM root.cszl.uav` |

**清空数据**：
```sql
DELETE FROM root.cszl.uav.*
```

---

## 5. 接入配置

### 5.1 REST API 配置

**健康检查**：
```bash
curl -s "http://10.129.80.142:18080/ping"
```

**创建存储组**：
```bash
curl -X POST "http://10.129.80.142:18080/rest/v2/nonQuery" \
  -H "Content-Type: application/json" \
  -u "root:root" \
  -d '{"sql": "CREATE DATABASE root.yyzx"}'
```

**查询数据**：
```bash
curl -X POST "http://10.129.80.142:18080/rest/v2/query" \
  -H "Content-Type: application/json" \
  -u "root:root" \
  -d '{"sql": "SHOW DATABASES"}'
```

**写入数据**：
```bash
curl -X POST "http://10.129.80.142:18080/rest/v2/nonQuery" \
  -H "Content-Type: application/json" \
  -u "root:root" \
  -d '{"sql": "INSERT INTO root.cszl.uav (time, flyId, lat, lon) VALUES (1776747236000, '\''D0101209'\'', 22.1, 113.5)"}'
```

### 5.2 MQTT 服务配置

**MQTT 配置项（iotdb-system.properties）**：

| 配置项 | 值 | 说明 |
|--------|-----|------|
| enable_mqtt_service | true | 启用 MQTT 服务 |
| mqtt_host | 0.0.0.0 | 监听地址 |
| mqtt_port | 1883 | MQTT 端口 |
| mqtt_handler_pool_size | 16 | 处理器线程数 |
| mqtt_payload_formatter | json | JSON 格式解析 |
| mqtt_max_message_size | 1048576 | 最大消息 1MB |

**MQTT 消息格式（JSON）**：
```json
{
  "device": "root.cszl.uav",
  "timestamp": 1586076045524,
  "measurements": ["s1", "s2"],
  "values": [0.530635, 0.530635]
}
```

> **注意**：IoTDB 2.0.7 内置 MQTT 不支持普通 flat JSON，必须使用 `device/measurements/values` 数组格式。

### 5.3 MQTTX 连接配置

| 参数 | 值 |
|------|-----|
| Broker | tcp://10.129.80.142 |
| Port | 1883 |
| Username | root |
| Password | root |

### 5.4 多场景快速配置

| 场景 | 存储组 | MQTT Topic | 说明 |
|------|--------|------------|------|
| 城市治理 | root.cszl | root.cszl.uav | 无人机数据 |
| 运营中心 | root.yyzx | root.yyzx.device | 设备数据 |
| 智慧灯杆 | root.zhdg | root.zhdg.lamp | 灯杆数据 |

**运营中心快速配置**：
```sql
-- 创建存储组
CREATE DATABASE root.yyzx

-- 创建设备模板（按需）
CREATE DEVICE TEMPLATE yyzx_template (temperature DOUBLE, humidity DOUBLE)

-- 设置设备模板
SET DEVICE TEMPLATE yyzx_template TO root.yyzx.device
```

**智慧灯杆快速配置**：
```sql
-- 创建存储组
CREATE DATABASE root.zhdg
```

> **提示**：MQTT 消息中的 device 字段需与存储组路径对应，如 `root.yyzx.device`。

---

## 6. 监控运维

### 6.1 端口检查

```bash
ss -tlnp | grep 1883    # MQTT
ss -tlnp | grep 18080   # REST
ss -tlnp | grep 6667    # RPC
ss -tlnp | grep 10710   # ConfigNode
ss -tlnp | grep 10750   # Schema Region
ss -tlnp | grep 10760   # Data Region
```

### 6.2 日志分析

**查看 DataNode 日志**：
```bash
tail -100 /data/iotdb/logs/log_datanode_all.log
```

**查看 ConfigNode 日志**：
```bash
tail -100 /data/iotdb/logs/log_confignode_all.log
```

**搜索错误信息**：
```bash
grep -i "error|exception|mqtt|rest" /data/iotdb/logs/log_datanode_all.log | tail -20
```

### 6.3 性能监控

**查看集群状态**：
```sql
SHOW CLUSTER DETAILS
```

**查看查询状态**：
```sql
SHOW QUERIES
```

**统计时序数量**：
```sql
SELECT COUNT(*) FROM root.cszl.uav
SELECT COUNT(*) FROM root.**
```

**检查集群节点健康**：
```bash
ping -c 1 10.129.80.142
ps -ef | grep iotdb
jps -l
```

### 6.4 巡检指标

| 指标 | 正常值 | 检查方法 |
|------|--------|----------|
| 集群节点在线 | 3/3 | `SHOW CLUSTER DETAILS` |
| RPC 端口 | 6667 监听 | `ss -tlnp \| grep 6667` |
| REST 服务 | 18080 监听 | `curl -s http://host:18080/ping` |
| MQTT 服务 | 1883 监听 | `ss -tlnp \| grep 1883` |
| 数据写入 | 正常 | `SELECT COUNT(*) FROM root.cszl.uav` |

---

## 7. 数据管理

### 7.1 备份恢复

**导出数据（CLI）**：
```sql
# 查询导出
SELECT * FROM root.cszl.uav INTO OUTFILE '/tmp/backup.csv'
```

**导入数据（CLI）**：
```sql
# 从文件导入
LOAD '/tmp/backup.csv' INTO root.cszl.uav
```

> **提示**：生产环境建议使用集群备份工具或定期快照。

### 7.2 数据过期策略

**设置 TTL（毫秒）**：
```sql
-- 7天过期
SET DATABASE root.cszl TTL 604800000

-- 30天过期
SET DATABASE root.cszl TTL 2592000000

-- 取消过期
SET DATABASE root.cszl TTL 0
```

**查看 TTL**：
```sql
SHOW DATABASES
```

### 7.3 权限管理

```sql
-- 创建用户
CREATE USER username 'password'

-- 授予读权限
GRANT READ ON root.cszl TO username

-- 授予写权限
GRANT WRITE ON root.cszl TO username

-- 授予所有权限
GRANT ALL ON root.cszl TO username

-- 查看用户
SHOW USERS

-- 删除用户
DROP USER username
```

---

## 8. 故障排除

### 8.1 常见问题汇总

| 问题 | 原因 | 解决方法 |
|------|------|----------|
| REST API 返回 404 | 路径错误 | 使用 `/rest/v2/nonQuery` 和 `/rest/v2/query` |
| MQTT 消息不接收 | JSON 格式错误 | 使用 `device/measurements/values` 格式 |
| 连接被拒绝 | 端口未监听 | 检查服务是否启动 `ps -ef \| grep iotdb` |
| 时间序列创建失败 | 数据类型错误 | 使用 `INT32` 而非 `INT`，使用 `DOUBLE` 而非 `FLOAT` |
| `time` 是保留字 | 列名冲突 | 使用反引号 `` `time` `` 包裹 |

### 8.2 诊断工具

**检查服务状态**：
```bash
jps -l
ps -ef | grep iotdb
```

**检查端口占用**：
```bash
ss -tlnp | grep <port>
netstat -tlnp | grep <port>
```

**检查磁盘空间**：
```bash
du -sh /data/iotdb/data/
df -h /data/iotdb/
```

### 8.3 排错案例

**案例：MQTTX 连接成功但数据不写入**

1. 检查 MQTT 服务是否启用：
   ```bash
   ss -tlnp | grep 1883
   ```

2. 检查 JSON 格式是否正确（必须包含 device/measurements/values）：
   ```json
   {
     "device": "root.cszl.uav",
     "measurements": ["flyId", "lat"],
     "values": ["D001", 22.1],
     "timestamp": 1586076045524
   }
   ```

3. 检查存储组是否存在：
   ```sql
   SHOW DATABASES
   ```

4. 检查时间序列是否已创建：
   ```sql
   SHOW TIMESERIES root.cszl.uav
   ```

---

## 9. 附录

### 9.1 命令参考

**服务管理命令**：

| 操作 | 命令 |
|------|------|
| 启动 DataNode | `$IOTDB_HOME/sbin/start-datanode.sh` |
| 停止 DataNode | `$IOTDB_HOME/sbin/stop-datanode.sh` |
| 启动 ConfigNode | `$IOTDB_HOME/sbin/start-confignode.sh` |
| 停止 ConfigNode | `$IOTDB_HOME/sbin/stop-confignode.sh` |
| 一键启动集群 | `$IOTDB_HOME/sbin/start-standalone.sh` |
| 一键停止集群 | `$IOTDB_HOME/sbin/stop-standalone.sh` |

### 9.2 配置参考

**数据类型速查**：

| 类型 | 说明 | 示例 |
|------|------|------|
| INT32 | 32位整数 | 4 |
| INT64 | 64位整数 | 1234567890 |
| FLOAT | 单精度浮点 | 1.23 |
| DOUBLE | 双精度浮点 | 1.23456789 |
| BOOLEAN | 布尔值 | true / false |
| TEXT | 字符串 | 卢忠尉 |

**编码方式速查**：

| 编码 | 适用类型 | 说明 |
|------|----------|------|
| PLAIN | 所有类型 | 无压缩，通用 |
| RLE | INT/FLOAT | 游程编码 |
| TS_2DIFF | INT/INT64 | 二阶差分 |
| GORILLA | FLOAT/DOUBLE | Gorilla压缩 |
| REGULAR | INT | 规律编码 |

**端口用途速查**：

| 端口 | 用途 |
|------|------|
| 6667 | DataNode RPC |
| 10710 | ConfigNode |
| 10720 | ConfigNode 共识 |
| 10730 | DataNode 内部通信 |
| 10740 | MPP 数据交换 |
| 10750 | Schema Region 共识 |
| 10760 | Data Region 共识 |
| 18080 | REST API |
| 1883 | MQTT |
| 9091 | ConfigNode Prometheus |
| 9092 | DataNode Prometheus |

### 9.3 联系人列表

| 角色 | 联系方式 | 备注 |
|------|----------|------|
| 技术负责人 | | |
| 值班人员 | | |

---

## 10. 修订记录

### 10.1 版本历史

| 版本 | 日期 | 作者 | 变更内容 |
|------|------|------|----------|
| v1.0 | 2026-04-22 | qin | 按规范模板重构，md + html 双版本输出 |
| v0.1 | 2026-04-21 | qin | 初始版本，IoTDB 集群接入验证 |

---

*本手册由 IoTDB 集群运维经验总结生成，适用于横琴全空间无人体系智能数据中心项目。*