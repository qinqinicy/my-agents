---
name: myagents-sync
description: MyAgents 多端同步插件。当用户说"同步"、"多端同步"、"多设备同步"、"sync"、"设置定时同步"、"自动同步"、"检查同步状态"、"检测冲突"时触发此技能。
---

# MyAgents 多端同步

基于 Git 的 MyAgents 工作区多端同步方案，支持自动定时同步、状态监控和冲突检测。

## 核心功能

| 功能 | 说明 |
|------|------|
| 手动同步 | `sync.sh` - git pull --rebase + git push |
| 状态查看 | `sync_status.sh` - 查看同步状态、未同步提交数 |
| 冲突检测 | `sync_conflicts.sh` - 检测 git 冲突和冲突标记 |
| 定时同步 | `setup_cron.sh` - 设置 MyAgents cron 定时任务 |

## 快速开始

### 1. 手动同步（立即执行）

```bash
cd .claude/skills/myagents-sync/scripts
./sync.sh
```

预览模式（不实际执行）:
```bash
./sync.sh --dry-run
```

### 2. 查看同步状态

```bash
./sync_status.sh
```

输出示例:
```
=== MyAgents 多端同步状态 ===
📍 当前分支: main
📍 远程仓库: qinqin/MyAgents
📦 工作区: 干净
🔄 与远程: 完全同步
🕐 最后同步: 2026-05-25 10:30:00
📝 最后提交: abc1234 memory: 更新横琴项目
```

### 3. 检测冲突

```bash
./sync_conflicts.sh
```

### 4. 设置定时同步

```bash
# 每 30 分钟同步一次（默认）
./setup_cron.sh

# 每 15 分钟同步一次
./setup_cron.sh 15
```

## 定时同步架构

```
MyAgents Cron (每 N 分钟)
    ↓
调用 Claude Agent 执行同步 prompt
    ↓
git pull --rebase && git push
    ↓
如有冲突 → 记录到 ~/.myagents/sync.log → 下次会话提醒用户
```

## 冲突处理流程

1. **自动检测**: 每次同步前运行 `sync_conflicts.sh`
2. **冲突标记**: 检测到 git conflict markers (<<<<<<<, =======, >>>>>>>)
3. **记录日志**: 冲突详情写入 `~/.myagents/sync.log`
4. **用户提醒**: 下次会话时通知用户有冲突待处理

### 解决冲突选项

```bash
# 选项 1: 保留本地变更（变基到远程）
git pull --rebase

# 选项 2: 保留远程变更（丢弃本地）
git reset --hard @{u}

# 选项 3: 手动合并后解决
git merge @{u}
# 然后编辑冲突文件，git add <file>
git commit
```

## 多端协作约定

| 场景 | 策略 |
|------|------|
| 正常同步 | git pull --rebase（保持线性历史） |
| 有本地提交 + 远程也有提交 | 变基到远程之上 |
| 分歧（两人改了同一文件） | 标记冲突，手动合并 |

**核心原则**: 避免 `git merge`，优先 `git rebase`，保持线性历史减少冲突。

## 日志位置

- 同步日志: `~/.myagents/sync.log`
- Cron 任务: `myagents cron list` 查看

## 注意事项

1. **工作区干净时同步**: 定时同步前确保无未提交的变更
2. **长任务中避免同步**: 如果有正在进行的 AI 对话，等完成后再同步
3. **冲突优先处理**: 发现冲突立即解决，避免多端分歧累积

## 脚本文件

- `scripts/sync.sh` - 主同步脚本
- `scripts/sync_status.sh` - 状态检查
- `scripts/sync_conflicts.sh` - 冲突检测
- `scripts/setup_cron.sh` - Cron 设置
