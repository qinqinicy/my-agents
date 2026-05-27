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

- **工作区划分** (2026-03-02): `Projects/` 是项目资产仓库，`Knowledge/` 是跨项目知识库，`AgentSystem/memory/topics/` 是档案索引，`SilentSpace/` 是每日草稿纸。项目文件存仓库，临时文件用完即弃。
- **记忆系统初始化** (2026-03-02): 为 4 个项目创建记忆档案：横琴项目、软考备考、知识图谱产品、交通部招聘笔试。
- **工作区重构** (2026-03-18):
  - 核心分区标签：`work/` 和 `personal/`（小写统一）
  - `Knowledge/` 提升至根目录，下设 `work/`、`personal/`、`public/`（公共知识融合）
  - `Projects/` 下使用状态前缀管理项目：`[WIP]` 进行中、`[HLD]` 暂停；已完成项目直接归档（带 `[ARC]` 前缀）
  - 文件流转：`Loading_files/`（基础资料）→ `Projects/`（项目管理）→ `SilentSpace/`（临时工作台）→ `归档（改前缀）`→ `Knowledge/`（沉淀）
  - 项目索引：`Projects/PROJECTS.md` 维护全局项目清单和映射关系
- **工作区配置统一** (2026-04-23):
  - `.claude/` 为实体目录（原在 `AgentSystem/.claude/`），统一维护一份配置
  - `myagents_files/` 更名为 `Outputs/`，明确输出文档位置
  - `Projects/work/` 项目内部统一为 `docs/{设计,运维,平台,其他}/` 子分类
  - 孔明平台作为横琴项目的子平台，不再是独立项目
- **Archives 目录移除** (2026-05-14): 归档 = 知识沉淀时机，用户要求区分"立即沉淀"和"归档时沉淀"；ARCHIVES_LOG.md 移至 Projects/，项目文件保留在原位（改前缀即可）
- **hmshare-sync 跨平台同步** (2026-05-27): Hanako ↔ MyAgents 通过 HMShare/HsM/ 共享记忆/项目/规范。两端各自维护独立脚本，MyAgents 配置 `~/.myagents/hmshare-sync.json`，Hanako 配置 `~/.hanako/hmshare-sync.json`。实体目录 + 导出模式（非软链），软链导致 MyAgents 文件预览失败。
- **Standards 规范目录** (2026-05-27): 新增 `Standards/` 目录存放规范文档模板，hmshare-sync 同步到 `HsM/standards/`

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
- **MinIO 自定义桶策略** (2026-05-21): `mc admin policy create minio {policy-name} {policy.json}` 创建策略，`mc admin policy attach minio {policy} --user {user}` 附加到用户；策略 JSON 中 Resource 格式为 `arn:aws:s3:::bucket-name` 和 `arn:aws:s3:::bucket-name/*`
- **孔明平台 License 双路径** (2026-05-15): alopex_proxy 读 `/opt/haizhi/.license/haizhi.license`，ai-web 读 `/opt/haizhi/license/haizhi.license`，新部署时两个目录都要配置
- **孔明离线安装 Docker** (2026-05-15): 离线包解包后 cp docker/* /usr/bin/，docker.sock 权限需 chmod 666 才能让非 root 用户操作
- **孔明 DMC 对接防火墙** (2026-05-18): DMC 服务器防火墙默认不开 Redis 6679 端口，孔明 hz-fe 连不上导致所有 /api/alchemy/* 502；`firewall-cmd --add-port=6679/tcp` 解决
- **孔明 alchemy DMC 配置** (2026-05-18): 默认无 `application-prod.yaml`，需手动创建并配置 `client.usercenter.url` 指向 DMC usercenter
- **孔明 brain/config.js** (2026-05-18): `dmcDomain` 为空时不显示"控制台"按钮，DMC SSO 跳转不自动是 DMC 前端问题，非孔明侧
- **MyAgents skill 管理** (2026-05-27): `myagents skill add` 不支持本地路径，只限 GitHub repo URL（foo/bar 格式）；repo 大小限 50 MB（超限 413）；本地 skill 放 `.claude/skills/` 自动发现
- **GitHub SSH Key** (2026-05-27): 本机 Mac ed25519 key (SHA256:a02CMpTtGwx3Jpyzz16tirdkko1FPt2s0T0kWa5Ej5M)，需添加到 GitHub Settings > SSH and GPG keys
- **火山引擎 Coding Plan 配置** (2026-05-27): 认证端点 `https://ark.cn-beijing.volces.com/api/coding`，使用 `Authorization: Bearer <apiKey>` 认证（authType: auth_token）。企业版和个人版认证方式一致。跨平台排查首先确认 API Key 是否一致（本次 MyAgents 和 Hanako Key 不同导致 401）
- **Word docx 样式导入** (2026-05-27): 从模板导入样式须清除 Theme 引用（`*Theme` 属性），换显式 TNR/宋体/黑体/仿宋，否则 macOS Word 回退日文字体
- **Word 多级编号** (2026-05-27): `suff=space`（非 tab）、`b w:val="0"`（编号不加粗）、`left=0 hanging=0`（编号顶格）
- **Word 表格规范** (2026-05-27): 0.5pt 全边框、cellMar 左右 108twips/上下 0、宽度 15.98cm、表头居中加粗 `表格-[Alt+w]`、数据左对齐 `表内容`
- **Word 正文段落** (2026-05-27): `firstLineChars=200`（2字符缩进）、`after=120twips`（6pt 段后）、`before=0`
- **Word lang 必设** (2026-05-27): docDefaults `eastAsia="zh-CN"`，主题 latin=TNR, ea=黑体/仿宋
- **heading pStyle 陷阱** (2026-05-27): heading 段落的 pStyle 值为 "1"~"9"（非 "Heading"），判 skip 时要检查数字范围，否则正文 firstLineChars 误应用到标题
- **Python threading.Lock 死锁** (2026-05-27): MQTT 回调（on_message）中若调用同锁函数（stop_recording），`threading.Lock` 会死锁。录制服务必须用 `threading.RLock`（可重入锁）。
- **MQTT 静默超时兜底** (2026-05-27): IoT 设备任务结束时可能不发"结束"消息（如无人机降落时 MQTT 直接断流），需加静默超时机制自动停止录制/释放资源。
- **FFmpeg 录制优雅退出** (2026-05-27): 用 `SIGTERM`（`proc.terminate()`）+ `wait(timeout=8)` 给 FFmpeg 时间写 MP4 moov 头，超时才 `kill()`。直接 kill 会导致文件无法播放。

## Important Decisions

- **工作区结构** (2026-03-02): 采用 `Projects/Work/` 和 `Projects/Personal/` 区分工作项目和个人项目，各自有 `Archive/` 归档和 `Knowledge/` 知识库。
- **工作区重构** (2026-03-18):
  - `workspace/` → `silentspace/`（临时工作台）
  - `myagents_files/` → `Loading_files/`（引用文件仓库）
  - `memory/topics/` 分为 `work/`、`personal/`、`public/` 三个子目录
  - `Work/` 和 `Personal/` → `Projects/Work/` 和 `Projects/Personal/`（统一项目资产管理）
  - `Knowledge/` 提升至根目录（跨项目知识库）
  - 统一大小写：`projects/` → `Projects/`，`active/` → `Active/`
- **工作区最终简化** (2026-03-19):
  - 核心分区：`work/` 和 `personal/`（小写）
  - `Projects/` 下移除 `Projects/` 和 `Active/` 子目录，改用状态前缀：`[WIP]`、`[HLD]`
  - `SilentSpace/`、`Loading_files/`、`Knowledge/` 均按 `work/`、`personal/` 分类
  - `Knowledge/` 新增 `public/` 融合公共知识
  - 移除 `[OK]` 状态，已完成项目直接归档（带 `[ARC]` 前缀）
  - 创建 `Projects/PROJECTS.md` 全局项目索引，维护所有项目状态和映射关系
  - 创建 `Projects/ARCHIVES_LOG.md` 归档日志，记录归档详情和知识沉淀路径
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
| 知识图谱产品手册 | Work | 知识库建设 | `AgentSystem/memory/topics/work/知识图谱产品.md` |
| hmshare-sync 跨平台同步 | Work | v2.0 运行中 | `AgentSystem/memory/topics/work/hmshare-sync.md` |
| 数据中心文档体系 | Work | 文档填充中 | `AgentSystem/memory/topics/work/横琴全空间无人体系智能数据中心.md` |

### Recent Work (2026-05)
- **数据中心文档体系建设** (2026-05-26/27): 7 份框架文档 + 格式规范，填充 数据库集群-架构设计（基于环境台账），55 个样式从模板导入
- **Word 格式规范** (2026-05-27): Standards/02-Word文档格式规范.md，涵盖编号/表格/正文/题注 OOXML 参数
- **hmshare-sync 重构** (2026-05-27): 两端独立脚本，数据目录对齐 Hanako，实体目录 + HsM 导出
- **工作区整理** (2026-05-27): 新增 Standards/ 目录，清理临时文件
- **无人机分段录制服务** (2026-05-27): drone_segment_recorder.py + systemd 部署，修复 Lock 死锁和 MQTT 静默超时兜底，运维手册更新至 v2.4

### Recent Work (2026-04/05)
- **流命名规范化** (2026-04-29): machinenest/drone/5gacamera，录制路径 {场景}/{流ID}/，rclone sync 按场景分流
- **MinIO IAM** (2026-04-29): cszlkey + cszl-readonly 策略，只读访问 cszl-media 桶
- **IoTDB 场景接入** (2026-05-08): 大横琴无人车(root.dhq, 48时序)、金城无人机(root.jincheng, 17时序)
- **WireGuard VPN 部署** (2026-05-08/10): 腾讯云 129.204.87.213 服务端运行中，Mac + Win1 已连通（10.0.0.0/24），Win2 待连接
- **孔明平台新部署** (2026-05-15): 10.129.80.235 离线部署完成（ai_2.0.2，CentOS 7，4T 盘挂载 /data），License 双路径问题已解决
- **孔明 DMC 对接调试** (2026-05-18): 旧平台(192.168.2.100)对接 DMC，Redis 6679 端口打通，usercenter 联通，SSO 验证通过，配置 brain/config.js dmcDomain
- **MinIO dkqx 场景接入** (2026-05-21): 新增低空气象数据场景 dkqx-data，数据源方通过 S3 API 直写 MinIO，独立 MinIO 运维手册发布
- **myagents-bridge 多端同步** (2026-05-27): 基于 Hanako hanako-bridge 创建 MyAgents 版本。HMShare 中介同步 skills/agents/providers/plugins/config.json，export/import/status 三命令。已导出 198 文件到 HMShare/myagents/darwin/。等待 push 到 GitHub 后可跨端安装。

---

*Update this file as you learn. It's how you persist.*

## Hanako facts（自动合并）

- 用户可能用"Hanako"称呼助手。
  <sub>`cherry` `2026-05-25` #Hanako #称呼助手</sub>
- 用户可能以"秦"被称呼。
  <sub>`cherry` `2026-05-25` #秦 #称呼</sub>
- 用户近期持续在关注 Hanako 插件的同步能力设计
  <sub>`agent-mpcjiq75` `2026-05-25` #Hanako #插件 #同步设计 #近况</sub>
- 用户名可能是Sakura
  <sub>`agent-mpcjiq75` `2026-05-25` #Sakura #姓名 #身份</sub>
- 用户近期在关注数据中心相关文档产出
  <sub>`agent-mpcjiq75` `2026-05-25` #数据中心 #文档产出 #近况</sub>
- 用户在处理文档模板标准化与批量生成
  <sub>`agent-mpcjiq75` `2026-05-25` #文档模板 #标准化 #批量生成</sub>
- 用户最近在配置 Hanako
  <sub>`agent-mpcjiq75` `2026-05-25` #Hanako #配置 #近况</sub>
- 用户近期在整理测试文档
  <sub>`agent-mpcjiq75` `2026-05-25` #测试文档 #近况</sub>
- 用户近期在关注更适合现有需求的视频平台方向
  <sub>`agent-mpcjiq75` `2026-05-22` #视频平台 #平台选型 #近况</sub>
- 用户近期比较过两个 AKStream 项目的适配性
  <sub>`agent-mpcjiq75` `2026-05-22` #AKStream #项目比较 #适配性</sub>
- 用户近期在关注离线流可见性问题
  <sub>`agent-mpcjiq75` `2026-05-22` #离线流 #可见性 #视频平台</sub>
- 用户近期在推进 StreamUI 的免密访问需求
  <sub>`agent-mpcjiq75` `2026-05-22` #StreamUI #免密访问 #近况</sub>
- 用户近期持续关注视频平台的流媒体代理链路问题
  <sub>`agent-mpcjiq75` `2026-05-22` #视频平台 #流媒体代理 #代理链路 #近况</sub>
- | 项目 | 类型 | 状态 |
|------|------|------|
| 横琴全空间无人体系智能数据中心 | Work | [WIP] 详细设计阶段 |
| 视频平台运维（ZLMediaKit+MinIO）| Work | [WIP] 部署完成 |
| 2026 软考系统架构师备考 | Personal | [WIP] 备考中 |
| 知识图谱产品手册 | Work | [WIP] 知识库建设 |
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## 当前项目
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - Spring Boot Nested JAR 打包（STORED 模式）
- JWT 字段兼容性处理
- ZLMediaKit + MinIO 视频平台部署
- Ollama + 孔明平台分离部署（无 GPU 环境）
- rclone 文件同步（排除正在写入文件）
- IoTDB 运维管理
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## 技术积累
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- | 偏好 | 说明 |
|------|------|
| 简洁直接 | 不废话，一句能说完不用三句 |
| 自动化 | 希望文件自动归类到正确目录 |
| 系统化 | 建立规则后希望复用，不接受混乱结构 |
| Git 管理 | 所有重要变更提交到版本库 |
| 确认后执行 | 不喜欢被突然问一堆问题，确认方向后直接执行 |
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## 习惯/偏好
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - **大模型应用**：孔明平台、Ollama、Rerank 服务部署（无 GPU 分离部署）
- **视频平台运维**：ZLMediaKit、MinIO、Docker 网络配置
- **数据平台**：IoTDB、ADS 层专题库设计
- **软考备考**：2026 系统架构师
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## 兴趣/关注领域
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### 文件管理
- **Loading_files/** — 用户上传的输入材料
- **Outputs/** — Agent 生成的输出文档
- 目录名偏好中文自然分隔，不用下划线/连字符
- 文件放对位置，不接受乱丢
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### 项目管理
- 同时处理工作项目（横琴数据中心）和个人项目（软考备考）
- 项目使用状态前缀：`[WIP]` 进行中、`[HLD]` 暂停、`[ARC]` 已归档
- Work 项目统一结构：`docs/{设计,运维,平台,其他}/`
- 新项目全路径同步创建
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## 工作模式
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - **Name:** 秦
- **What to call them:** 秦
- **Pronouns:** he/him
- **Timezone:** Asia/Shanghai (UTC+8)
- **Language:** 中文
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- <!-- Hanako User Profile (auto-merged) -->
# USER.md - About Your Human
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- *Update this file as you learn. It's how you persist.*
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### Recent Work (2026-05)
- **hmshare-sync 重构** (2026-05-27): 两端各自维护独立脚本，MyAgents 端 v2.0 实现完整导出/导入链，同步范围包括 memories、standards、projects/Archives/Knowledge/Outputs/Loading_files
- **工作区整理** (2026-05-27): 新增 Standards/ 目录，清理临时文件，迁移目录到 HsM 后恢复实体目录

### Recent Work (2026-04/05)
- **流命名规范化** (2026-04-29): machinenest/drone/5gacamera，录制路径 {场景}/{流ID}/，rclone sync 按场景分流
- **MinIO IAM** (2026-04-29): cszlkey 只读访问 cszl-media，S3 API 对外服务
- **IoTDB 场景接入** (2026-05-08): 大横琴无人车(root.dhq, 48时序)、金城无人机(root.jincheng, 17时序)
- **WireGuard VPN 部署** (2026-05-08/10): 腾讯云 129.204.87.213 服务端运行中，Mac + Win1 已连通（10.0.0.0/24），Win2 待连接
- **孔明平台新部署** (2026-05-15): 10.129.80.235 离线部署完成（ai_2.0.2，CentOS 7，4T 盘挂载 /data），License 双路径问题已解决
- **孔明 DMC 对接调试** (2026-05-18): 旧平台(192.168.2.100)对接 DMC，Redis 6679 端口打通，usercenter 联通，SSO 验证通过，配置 brain/config.js dmcDomain
- **MinIO dkqx 场景接入** (2026-05-21): 新增低空气象数据场景 dkqx-data，数据源方通过 S3 API 直写 MinIO，独立 MinIO 运维手册发布
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### Active Projects
| 项目 | 类型 | 状态 | 记忆档案 |
|------|------|------|---------|
| 横琴全空间无人体系智能数据中心 | Work | 详细设计阶段 | `AgentSystem/memory/topics/work/横琴全空间无人体系智能数据中心.md` |
| 视频平台运维（ZLM+StreamUI+MinIO）| Work | 运维阶段 | `AgentSystem/memory/topics/work/视频平台运维.md` |
| 2026 软考系统架构师备考 | Personal | 备考中 | `AgentSystem/memory/topics/personal/软考备考.md` |
| 知识图谱产品手册 | Work | 知识库建设 | `AgentSystem/memory/topics/work/知识图谱产品.md` |
| hmshare-sync 跨平台同步 | Work | v2.0 运行中 | `AgentSystem/memory/topics/work/hmshare-sync.md` |
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## Ongoing Context
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## Technical Knowledge
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - 用户同时处理工作项目（横琴数据中心）和个人提升（软考、交通部招聘考试）
- 偏好用中文交流和记录
- AI 名称：清 (2026-03-18)
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## User Preferences
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - **工作区结构** (2026-03-02): 采用 `Projects/Work/` 和 `Projects/Personal/` 区分工作项目和个人项目，各自有 `Archive/` 归档和 `Knowledge/` 知识库。
- **工作区重构** (2026-03-18):
  - 核心分区标签：`work/` 和 `personal/`（小写统一）
  - `Knowledge/` 提升至根目录，下设 `work/`、`personal/`、`public/`（公共知识融合）
  - `Projects/` 下使用状态前缀管理项目：`[WIP]` 进行中、`[HLD]` 暂停；已完成项目直接归档（带 `[ARC]` 前缀）
  - 文件流转：`Loading_files/`（基础资料）→ `Projects/`（项目管理）→ `SilentSpace/`（临时工作台）→ `归档（改前缀）`→ `Knowledge/`（沉淀）
  - 项目索引：`Projects/PROJECTS.md` 维护全局项目清单和映射关系
- **工作区配置统一** (2026-04-23):
  - `.claude/` 为实体目录（原在 `AgentSystem/.claude/`），统一维护一份配置
  - `myagents_files/` 更名为 `Outputs/`，明确输出文档位置
  - `Projects/work/` 项目内部统一为 `docs/{设计,运维,平台,其他}/` 子分类
  - 孔明平台作为横琴项目的子平台，不再是独立项目
- **Archives 目录移除** (2026-05-14): 归档 = 知识沉淀时机，用户要求区分"立即沉淀"和"归档时沉淀"；ARCHIVES_LOG.md 移至 Projects/，项目文件保留在原位（改前缀即可）
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### Working Style
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- Organize by topic as your lessons grow. A flat list becomes unreadable fast.
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## Lessons Learned
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ---
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - Just woke up → this file is already loaded + read today/yesterday's logs
- About to work on a project → read its `AgentSystem/memory/topics/work/<name>.md` or `personal/<name>.md`
- Memory maintenance → read all recent logs + all active topic files
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### When to Read What
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - During work: just write the daily log
- During maintenance: sync from logs to topics, distill new cross-project lessons to this file
- **Information lives in one place only** — don't duplicate between topic files and 04-MEMORY
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ```
Daily logs (raw material) → topic files (synthesized per-project) → 04-MEMORY (cross-project essence)
```
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### Information Flow
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- **Daily journal (`AgentSystem/memory/YYYY-MM-DD.md`)** — Read today + yesterday at session start
- What goes here: what happened that day, raw chronological record
- This is the source of all memory, but searching it for specific project info is inefficient (multiple projects mixed in one day)
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- **Topic memory (`AgentSystem/memory/topics/work/<name>.md` | `personal/<name>.md` | `public/<name>.md`)** — Read before working on a project
- What goes here: full accumulated experience for one project/topic — status, key facts, what you did, what worked, what didn't, decisions and rationale, next steps
- More detailed than core memory (which only has pointers), more synthesized than daily logs (which are raw chronological notes)
- Update during memory maintenance or when a project enters a new phase
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- **Core memory (this file, 04-MEMORY.md)** — Auto-loaded every session
- What goes here: cross-project lessons, key decisions, user preferences, technical knowledge, one-line project summaries + pointers
- What doesn't: detailed project experience (that's what topic files are for)
- **Add a timestamp `(YYYY-MM-DD)` to each entry** — helps trace back, judge recency, clean up
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- Your memory has three layers, each with different responsibilities and access patterns:
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ### Three-Layer Memory
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- - **Be mindful in shared contexts** — this file contains personal context about your human. In group chats or shared sessions, don't leak private preferences, decisions, or project details
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- ## About This File & Memory System
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- *Your curated memories. The distilled essence, not raw logs.*
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- # MEMORY.md - Long-Term Memory
  <sub>`cherry` `2026-05-21` #hmshare #sync</sub>
- 用户最近在关注 PostGIS
  <sub>`cherry` `2026-05-21` #PostGIS #地理数据库 #近况</sub>
- 用户最近在关注钉钉渠道配置
  <sub>`cherry` `2026-05-21` #钉钉 #渠道配置 #近况</sub>
- 用户近期在关注 Hanako/Clawbot 的微信消息联通与主动通知能力
  <sub>`agent-mpcjiq75` `2026-05-21` #Hanako #Clawbot # #通知</sub>
- 用户近期持续关注数据中心项目推进
  <sub>`agent-mpcjiq75` `2026-05-21` #数据中心 #项目推进 #近况</sub>
- 用户近期持续关注周报撰写
  <sub>`agent-mpcjiq75` `2026-05-21` #周报 #近况</sub>
- 用户近期在折腾 Hanako 插件开发
  <sub>`agent-mpcjiq75` `2026-05-21` #Hanako #插件开发 #近况</sub>
- 用户近期在维护视频平台
  <sub>`agent-mpcjiq75` `2026-05-21` #视频平台 #运维 #近况</sub>
- 用户最近在关注工作区整理
  <sub>`cherry` `2026-05-15` #工作区整理 #近况</sub>
- 用户偏好精简、原位状态管理的工作区
  <sub>`cherry` `2026-05-15` #工作区 #原位管理 #精简</sub>
- 用户近期在做时序图谱建模
  <sub>`hanako` `2026-05-15` #时序图谱 #图谱建模 #近况</sub>
- 用户近期在做气象本体建模
  <sub>`hanako` `2026-05-15` #气象本体 #本体建模 #近况</sub>
- 次日用户暂时不考虑继续推进 Hanako 图片生成这条方向
  <sub>`cherry` `2026-05-14` #Hanako #图片生成 #暂停 #近况</sub>
- 用户近期在折腾 Hanako 的代理图片生成链路
  <sub>`cherry` `2026-05-14` #Hanako #图片生成 #代理 #近况</sub>
- 用户近期在关注可用的图片供应商
  <sub>`cherry` `2026-05-13` #图片供应商 #图像服务 #近况</sub>
- 用户近期在关注 Hanako 的图片生成能力
  <sub>`cherry` `2026-05-13` #Hanako #图片生成 #近况</sub>
- 用户近期关注Hanako多媒体
  <sub>`cherry` `2026-05-13` #Hanako多媒体 #近况</sub>
- 用户在试探助手的能力边界
  <sub>`cherry` `2026-05-13` #能力边界 #AI助手 #近况</sub>
- 用户关注推理能力
  <sub>`cherry` `2026-05-13` #推理 #能力 #近况</sub>
- 用户关注视觉理解能力
  <sub>`cherry` `2026-05-13` #视觉理解 #图像 #近况</sub>
- 用户最近在关注记忆系统
  <sub>`cherry` `2026-05-13` #记忆系统 #近况</sub>
- 用户是技术工作者
  <sub>`cherry` `2026-05-13` #技术工作者 #身份</sub>
- 用户正在学习 Git 的基础概念
  <sub>`cherry` `2026-05-13` #Git #学习 #基础概念</sub>
- 用户正在学习 GitHub 的基础概念
  <sub>`cherry` `2026-05-13` #GitHub #学习 #基础概念</sub>
- 用户使用 macOS 系统
  <sub>`cherry` `2026-05-13` #macOS #操作系统</sub>
- 用户使用 Edge 浏览器
  <sub>`cherry` `2026-05-13` #Edge #浏览器</sub>
- 用户使用 Safari 浏览器
  <sub>`cherry` `2026-05-13` #Safari #浏览器</sub>
- 用户偏好中文界面
  <sub>`cherry` `2026-05-13` #中文界面 #偏好</sub>
- 用户对英文工具界面有抵触
  <sub>`cherry` `2026-05-13` #英文界面 #抵触 #工具</sub>
- 用户近期在关注配置自定义 AI 供应商
  <sub>`cherry` `2026-05-13` #AI供应商 #OpenAI #配置 #近况</sub>
- 助手建议继续使用 qwen3.5-plus 因为 deepseek-v4-flash 可能不支持图片输入
  <sub>`hanako` `2026-05-01` #模型选择 #qwen3.5-plus #图片输入</sub>
- 用户询问能否将模型改为 deepseek-v4-flash
  <sub>`hanako` `2026-05-01` #模型切换 #deepseek-v4-flash</sub>
- Skill 工作正常
  <sub>`hanako` `2026-04-30` #Skill #状态确认</sub>
- 识别到 Xshell 6 连接阿里云服务器
  <sub>`hanako` `2026-04-30` #Xshell #阿里云 #SSH连接</sub>
- 识别到远程主机 qkjwrtx-sjzx-iotdb-001
  <sub>`hanako` `2026-04-30` #主机名 #qkjwrtx-sjzx-iotdb-001</sub>
- 识别到登录信息 root@10.129.80.142
  <sub>`hanako` `2026-04-30` #root #IP #10.129.80.142 #SSH</sub>
- 确认支持本地文件路径
  <sub>`hanako` `2026-04-30` #Skill #本地文件路径 #功能支持</sub>
- 确认支持 HTTP/HTTPS URL
  <sub>`hanako` `2026-04-30` #Skill #HTTP #HTTPS #功能支持</sub>
- 确认支持中文 Prompt
  <sub>`hanako` `2026-04-30` #Skill #中文 #Prompt #功能支持</sub>
- 确认支持代理自动检测，端口 7890
  <sub>`hanako` `2026-04-30` #Skill #代理 #7890 #自动检测</sub>
- 建议用户重启 Hanako 应用使 minimax-understand-image skill 生效
  <sub>`hanako` `2026-04-30` #Hanako #重启 #minimax-understand-image</sub>
- 执行了 export MINIMAX_API_KEY 命令
  <sub>`hanako` `2026-04-30` #export #MINIMAX_API_KEY #环境变量</sub>
- [#ch_crew] 用户报告了error.defaultChannelDesc错误，我询问了触发场景。
  <sub>`cherry` `2026-04-20` #频道 #ch_crew</sub>
- 助手提供了批量配置脚本以支持网段全通
  <sub>`hanako` `2026-03-27` #批量脚本 #配置 #网段</sub>
- 用户将远程连接需求从单 IP 变更网段全通
  <sub>`hanako` `2026-03-27` #远程连接 #网段 #10.129.80.0/24 #需求变更</sub>
- 确认从节点需通过主节点配置远程连接
  <sub>`hanako` `2026-03-26` #从节点 #主节点 #远程连接 #配置</sub>
- 用户询问远程连接配置
  <sub>`hanako` `2026-03-26` #用户 #远程连接 #配置 #询问</sub>
- 助手指导修改主节点以支持远程连接
  <sub>`hanako` `2026-03-26` #助手 #主节点 #修改 #指导</sub>
- 方案定义了检索匹配规则与回复策略
  <sub>`hanako` `2026-03-26` #检索规则 #回复策略 #提示词</sub>
- 助手输出 LLM 提示词设计方案
  <sub>`hanako` `2026-03-26` #LLM #提示词 #设计方案</sub>
- 用户要求设计智能助手角色设定文档
  <sub>`hanako` `2026-03-26` #智能助手 #角色设定 #文档设计</sub>
- 助手输出包含角色、能力及限制的设定方案
  <sub>`hanako` `2026-03-26` #角色设定 #能力定义 #限制条件</sub>
- 确认核心数据：任务 112 条
  <sub>`hanako` `2026-03-26` #核心数据 #任务统计</sub>
- 确认核心数据：装备 66 台（含无人车 51 辆等）
  <sub>`hanako` `2026-03-26` #核心数据 #装备统计 #无人车</sub>
- 确认核心数据：航线 49 条
  <sub>`hanako` `2026-03-26` #核心数据 #航线统计</sub>
- 用户索要两份 FAQ 的 xlsx 文件
  <sub>`hanako` `2026-03-26` #FAQ #xlsx #文件请求</sub>
- 助手交付两份 FAQ 的 xlsx 文件
  <sub>`hanako` `2026-03-26` #FAQ #文件交付</sub>
- 用户要求按层级下钻思路新增问数场景 FAQ
  <sub>`hanako` `2026-03-26` #FAQ #层级下钻 #问数场景</sub>
- 助手模拟数据生成问数场景 FAQ
  <sub>`hanako` `2026-03-26` #FAQ #模拟数据 #生成</sub>
- 用户提供真实业务数据修正统计值
  <sub>`hanako` `2026-03-26` #业务数据 #统计修正</sub>
- 助手根据真实数据更新文件
  <sub>`hanako` `2026-03-26` #文件更新 #数据处理</sub>
- 用户偏好"总指标→分类→细化"的问数逻辑
  <sub>`hanako` `2026-03-26` #用户偏好 #问数逻辑 #数据展示</sub>
- 用户询问如何在 macOS 卸载微信输入法
  <sub>`hanako` `2026-03-26` #微信输入法 #macOS #卸载</sub>
- 助手提供微信输入法移除步骤及清理方法
  <sub>`hanako` `2026-03-26` #微信输入法 #清理 #操作指南</sub>
- 用户反馈 QSpace Pro 出现闪退问题
  <sub>`hanako` `2026-03-26` #QSpace Pro #闪退 #故障</sub>
- 确认 QSpace Pro 版本为 6.1.4
  <sub>`hanako` `2026-03-26` #QSpace Pro #6.1.4 #版本</sub>
- 助手分析日志发现 QSpace Pro 为破解版导致签名异常
  <sub>`hanako` `2026-03-26` #QSpace Pro #破解版 #签名异常 #日志分析</sub>
- 用户现使用 macOS 系统
  <sub>`hanako` `2026-03-26` #macOS #操作系统</sub>
- postgres 系统用户无密码
  <sub>`hanako` `2026-03-26` #postgres #系统用户 #无密码</sub>
- 助手指出从节点限制并提供 root 执行方案
  <sub>`hanako` `2026-03-26` #助手 #从节点限制 #root #执行方案</sub>
- 从节点配置为只读模式
  <sub>`hanako` `2026-03-26` #从节点 #只读 #数据库</sub>
- 助手介绍功能并保持在线
  <sub>`hanako` `2026-03-26` #助手功能 #在线状态</sub>
- 用户要求发送 cloud_air_map.xls 文件但反馈未收到
  <sub>`hanako` `2026-03-26` #文件发送 #cloud_air_map.xls #失败反馈</sub>
- 助手将文件从微信容器复制到工作目录后重新发送
  <sub>`hanako` `2026-03-26` #文件复制 #微信容器 #工作目录 #重发</sub>
- 确认用户可查看重新发送的文件
  <sub>`hanako` `2026-03-26` #文件查看 #确认</sub>
- 用户多次问候并询问助手身份
  <sub>`hanako` `2026-03-26` #用户交互 #身份询问</sub>
- 用户关注 Hanako 的 archive 功能
  <sub>`hanako` `2026-03-26` #Hanako #archive #功能</sub>
- 助手诊断 VMware 文件锁定报错
  <sub>`hanako` `2026-03-26` #助手 #诊断 #VMware #文件锁定</sub>
- 用户提供脚本要求添加删除.lck 文件逻辑
  <sub>`hanako` `2026-03-26` #用户 #脚本 #.lck #逻辑</sub>
- 助手完成脚本修改
  <sub>`hanako` `2026-03-26` #助手 #脚本修改 #完成</sub>
- 用户询问并确认是指 Hanako 的 archive 功能
  <sub>`hanako` `2026-03-26` #用户 #询问 #Hanako #archive</sub>
- 助手解释 Hanako 的 archive 功能为自动记忆系统
  <sub>`hanako` `2026-03-26` #助手 #解释 #Hanako #自动记忆系统</sub>
- 用户习惯使用批处理脚本管理 VMware
  <sub>`hanako` `2026-03-26` #用户习惯 #批处理脚本 #VMware</sub>
- 用户需手动创建 7 个 MQTT 账号
  <sub>`hanako` `2026-03-26` #MQTT #账号 #手动创建</sub>
- 用户偏好使用中文 Web 界面
  <sub>`hanako` `2026-03-26` #Web 界面 #中文 #偏好</sub>
- 用户决定通过 Web 页面手动构建 7 个用户名密码
  <sub>`hanako` `2026-03-26` #Web 页面 #用户名 #密码 #决策</sub>
- 助手提供手动构建用户名密码的操作步骤
  <sub>`hanako` `2026-03-26` #助手 #操作步骤 #用户名 #密码</sub>
- 用户要求将 MQTT Web 页面切换为中文
  <sub>`hanako` `2026-03-26` #MQTT #Web 页面 #中文 #语言设置</sub>
- 助手提供语言设置方法
  <sub>`hanako` `2026-03-26` #助手 #语言设置 #方法</sub>
- 助手定位到 cloud_air_map.xls 的具体路径并告知用户
  <sub>`hanako` `2026-03-26` #cloud_air_map.xls #路径 #文件查找 #助手操作</sub>
- 用户需在 macOS 微信本月保存目录查找名为 cloud_air_map.xls 的文件
  <sub>`hanako` `2026-03-26` #macOS #微信 #cloud_air_map.xls #文件查找</sub>
- 用户要求增加大模型及 Agent 相关面试题
  <sub>`hanako` `2026-03-26` #面试题 #大模型 #Agent</sub>
- 用户提供面试小结并要求按指定格式输出两人评价
  <sub>`hanako` `2026-03-26` #面试小结 #评价 #格式</sub>
- 用户补充王欣的会议概览
  <sub>`hanako` `2026-03-26` #王欣 #会议概览</sub>
- 用户要求根据王欣的会议概览重新生成评价
  <sub>`hanako` `2026-03-26` #王欣 #评价 #重新生成</sub>
- 用户请求配置小秘秘 API 并提供 Key 及文档链接
  <sub>`hanako` `2026-03-26` #小秘秘 #API #配置</sub>
- 助手完成小秘秘 API 配置并告知结果
  <sub>`hanako` `2026-03-26` #小秘秘 #API #配置</sub>
- 助手于次日 00:03 回应用户并询问需求
  <sub>`hanako` `2026-03-26` #助手 #回应 #询问需求</sub>
- 用户于 23:09 再次发起问候
  <sub>`hanako` `2026-03-26` #用户 #问候 #交互</sub>
- 用户偏好精准 PDF 识别
  <sub>`hanako` `2026-03-25` #用户偏好 #PDF #识别</sub>
- 决定删除 pptx-generator
  <sub>`hanako` `2026-03-25` #pptx-generator #删除 #决策</sub>
- 决定保留 MiniMax 与 AgentSystem 重复技能
  <sub>`hanako` `2026-03-25` #MiniMax #AgentSystem #技能 #保留</sub>
- 助手安装了 11 个 MiniMax 技能
  <sub>`hanako` `2026-03-25` #助手 #MiniMax #技能 #安装</sub>
- 用户要求对比重复技能
  <sub>`hanako` `2026-03-25` #用户 #重复技能 #对比</sub>
- 助手建议全保留重复技能
  <sub>`hanako` `2026-03-25` #助手 #建议 #保留</sub>
- 用户因识别不准要求更换 PDF 技能
  <sub>`hanako` `2026-03-25` #用户 #PDF #技能 #更换</sub>
- 助手安装了 Docling 等高精度工具
  <sub>`hanako` `2026-03-25` #助手 #Docling #工具 #安装</sub>
- 用户于 17:13 发起问候
  <sub>`hanako` `2026-03-26` #用户 #问候</sub>
- 用户于 17:45 询问助手身份
  <sub>`hanako` `2026-03-26` #用户 #身份 #询问</sub>
- 助手自我介绍为 Hanako 并列举服务范围
  <sub>`hanako` `2026-03-26` #Hanako #自我介绍 #服务范围</sub>
- 用户于 19:55 确认在线状态
  <sub>`hanako` `2026-03-26` #用户 #在线状态 #确认</sub>
- 助手回应随时待命
  <sub>`hanako` `2026-03-26` #助手 #待命 #回应</sub>
- 用户决定强制卸载本机 dsm-client 软件
  <sub>`hanako` `2026-03-25` #dsm-client #卸载 #决策</sub>
- 用户要求查找并强制卸载 dsm-client
  <sub>`hanako` `2026-03-25` #dsm-client #查找 #卸载 #请求</sub>
- 助手定位软件路径及组件并列出卸载方案
  <sub>`hanako` `2026-03-25` #dsm-client #路径 #组件 #卸载方案</sub>
- 用户确认执行卸载操作
  <sub>`hanako` `2026-03-25` #dsm-client #确认 #执行</sub>
- 助手因权限限制提供终端脚本
  <sub>`hanako` `2026-03-25` #权限限制 #终端脚本 #助手</sub>
- 用户虚拟机 IP 配置曾为静态绑定
  <sub>`hanako` `2026-03-25` #虚拟机 #IP #静态绑定</sub>
- 15:29 助手指导用户切换网络模式
  <sub>`hanako` `2026-03-25` #助手 #网络模式 #指导</sub>
- 15:32 用户指出 IP 地址被写死
  <sub>`hanako` `2026-03-25` #用户 #IP #写死</sub>
- 15:32 助手建议将 IP 配置改为 DHCP
  <sub>`hanako` `2026-03-25` #助手 #DHCP #建议</sub>
- 15:34 用户确认 IP 已变更为 NAT 网段
  <sub>`hanako` `2026-03-25` #用户 #IP #NAT #确认</sub>
- 用户提供候选人简历背景
  <sub>`hanako` `2026-03-25` #简历 #候选人 #背景信息</sub>
- 用户请求生成大数据实施面试题
  <sub>`hanako` `2026-03-25` #面试题 #大数据 #生成</sub>
- 用户关注大数据实施问题
  <sub>`hanako` `2026-03-25` #大数据 #实施 #面试准备</sub>
- 用户将面试实施顾问赵俊杰
  <sub>`hanako` `2026-03-25` #赵俊杰 #面试 #实施顾问</sub>
- 用户补充反馈 GitHub 无法访问
  <sub>`hanako` `2026-03-25` #GitHub #网络故障 #用户反馈</sub>
- 用户提出按需切换代理的需求
  <sub>`hanako` `2026-03-25` #代理 #按需切换 #需求</sub>
- 助手推荐 ClashX Pro 作为系统级解决方案
  <sub>`hanako` `2026-03-25` #ClashX Pro #解决方案 #推荐</sub>
- 助手解释了台前调度的功能
  <sub>`hanako` `2026-03-25` #助手 #台前调度 #功能解释</sub>
- 用户询问 Mac 台前调度逻辑
  <sub>`hanako` `2026-03-25` #Mac #台前调度 #逻辑</sub>
- 用户决定修复 PDF 初始化时机问题
  <sub>`hanako` `2026-03-24` #PDF #初始化 #修复 #逻辑</sub>
- 用户于 14:15 在新路径提交代码要求编译
  <sub>`hanako` `2026-03-24` #代码提交 #编译 #新路径</sub>
- 用户于 14:38 反馈 PDF 首次加载失败且渲染异常
  <sub>`hanako` `2026-03-24` #PDF #加载失败 #渲染异常 #反馈</sub>
- 助手于 14:40 分析确认为初始化时机问题
  <sub>`hanako` `2026-03-24` #问题分析 #初始化时机 #诊断</sub>
- 助手将高度属性改为 height 并修复逻辑后重新编译
  <sub>`hanako` `2026-03-24` #代码修改 #height #重新编译 #修复</sub>
- 用户决定将 PDF 容器高度由 min-height 改为 height
  <sub>`hanako` `2026-03-24` #PDF #容器 #高度 #CSS</sub>
- 用户报告 bdp-noah 服务连接拒绝错误
  <sub>`hanako` `2026-03-24` #bdp-noah #服务 #连接拒绝 #错误</sub>
- 助手给出排查思路
  <sub>`hanako` `2026-03-24` #助手 #排查思路 #技术支持</sub>
- 助手排查发现代理服务未运行
  <sub>`hanako` `2026-03-24` #代理服务 #排查 #故障原因</sub>
- 用户反馈导航站无法访问
  <sub>`hanako` `2026-03-24` #导航站 #故障 #用户反馈</sub>
- 决定在 Hanako 平台配置阿里云 Coding Plan
  <sub>`hanako` `2026-03-20` #Hanako #阿里云 #Coding Plan #配置</sub>
- 明确 Hanako 平台配置非 OpenClaw
  <sub>`hanako` `2026-03-20` #Hanako #OpenClaw #平台选择</sub>
- 用户提供了 API Key
  <sub>`hanako` `2026-03-20` #API Key #凭证 #阿里云</sub>
- 偏好使用 bailian/qwen3.5-plus 作为默认模型
  <sub>`hanako` `2026-03-20` #bailian #qwen3.5-plus #模型 #偏好</sub>
- 助手在 Hanako 配置文件中新增 bailian 配置项
  <sub>`hanako` `2026-03-20` #Hanako #配置文件 #bailian #修改</sub>
- 测试新模型调用成功并验证配置生效
  <sub>`hanako` `2026-03-20` #测试 #模型调用 #验证 #成功</sub>
- 用户操作系统为 macOS
  <sub>`hanako` `2026-03-20` #macOS #操作系统 #环境</sub>
<!-- END Hanako facts -->
