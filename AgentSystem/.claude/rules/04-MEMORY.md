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

- **工作区划分** (2026-03-02): `Projects/Work/` 和 `Projects/Personal/` 是资产仓库，`AgentSystem/memory/topics/` 是档案索引，`SilentSpace/` 是每日草稿纸。项目文件存仓库，临时文件用完即弃。
- **记忆系统初始化** (2026-03-02): 为 4 个项目创建记忆档案：横琴项目、软考备考、知识图谱产品、交通部招聘笔试。

### Communication

### Technical

- **Spring Boot JAR 打包** (2026-03-17): Nested JAR 必须使用 STORED（无压缩）模式。错误："compressed and nested jar files must be stored without compression"。解决：`zip -0` 处理 lib/*.jar，其余文件正常压缩。
- **JWT 字段兼容性** (2026-03-17): iPaas OAuth 使用标准 JWT 字段（sub, name），代码需兼容多种字段名（userId/sub/userid, userName/name/username）。

## Important Decisions

- **工作区结构** (2026-03-02): 采用 `Projects/Work/projects/` 和 `Projects/Personal/projects/` 区分工作项目和个人项目，各自有 `Archive/` 归档和 `Knowledge/` 知识库。
- **工作区重构** (2026-03-18): `workspace/` → `silentspace/`（临时工作台），`myagents_files/` → `Loading_files/`（引用文件仓库），`memory/topics/` 分为 `work/`、`personal/`、`public/` 三个子目录。
- **Projects 整合** (2026-03-18): `Work/` 和 `Personal/` → `Projects/Work/` 和 `Projects/Personal/`，统一项目资产管理。

## User Preferences

- 用户同时处理工作项目（横琴数据中心）和个人提升（软考、交通部招聘考试）
- 偏好用中文交流和记录
- AI 名称：清 (2026-03-18)

## Technical Knowledge

## Ongoing Context

### Active Projects
| 项目 | 类型 | 状态 | 记忆档案 |
|------|------|------|---------|
| 横琴全空间无人体系智能数据中心 | Projects/Work | 详细设计阶段 | `AgentSystem/memory/topics/work/横琴项目.md` |
| 工单流转系统（决策后端）| Projects/Work | 调试中（401问题）| `AgentSystem/memory/topics/work/工单流转系统.md` |
| 2026 软考系统架构师备考 | Projects/Personal | 备考中 | `AgentSystem/memory/topics/personal/软考备考.md` |
| 交通部第四批招聘笔试备考 | Projects/Personal | 备考中 | `AgentSystem/memory/topics/personal/交通部招聘笔试.md` |
| 知识图谱产品手册 | Projects/Work | 知识库建设 | `AgentSystem/memory/topics/work/知识图谱产品.md` |

---

*Update this file as you learn. It's how you persist.*
