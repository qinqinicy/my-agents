#!/bin/bash
# MyAgents 多端同步主脚本
# 用法: ./sync.sh [--dry-run]

set -e

DRY_RUN=""
if [[ "$1" == "--dry-run" ]]; then
    DRY_RUN="--dry-run"
fi

SYNC_LOG="$HOME/.myagents/sync.log"
TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')

log() {
    echo "[$TIMESTAMP] $1" | tee -a "$SYNC_LOG"
}

cd /Users/qinqin/Library/CloudStorage/OneDrive-个人/Share/WinMac/Qin/MyAgents 2>/dev/null || {
    echo "Error: 工作区目录不存在"
    exit 1
}

log "=== 开始同步 ==="
log "当前分支: $(git branch --show-current)"
log "远程状态:"

# Fetch 远程最新状态
log "执行 git fetch..."
git fetch --all 2>&1 | tee -a "$SYNC_LOG"

# 检查状态
GIT_STATUS=$(git status -s)
if [[ -z "$GIT_STATUS" ]]; then
    log "工作区干净，无本地变更"
else
    log "本地变更文件数: $(echo "$GIT_STATUS" | wc -l)"
    echo "$GIT_STATUS" | tee -a "$SYNC_LOG"
fi

# 检查远程领先/落后
LOCAL=$(git rev-parse @)
REMOTE=$(git rev-parse @{u} 2>/dev/null || echo "$LOCAL")
BASE=$(git merge-base @ @{u})

if [[ "$LOCAL" == "$REMOTE" ]]; then
    log "与远程完全同步"
elif [[ "$LOCAL" == "$BASE" ]]; then
    log "远程有新的提交，需要 pull"
    if [[ -z "$DRY_RUN" ]]; then
        log "执行 git pull --rebase..."
        git pull --rebase 2>&1 | tee -a "$SYNC_LOG"
    else
        log "[DRY-RUN] git pull --rebase"
    fi
elif [[ "$REMOTE" == "$BASE" ]]; then
    log "本地有新的提交，需要 push"
    if [[ -z "$DRY_RUN" ]]; then
        log "执行 git push..."
        git push 2>&1 | tee -a "$SYNC_LOG"
    else
        log "[DRY-RUN] git push"
    fi
else
    log "⚠️  分歧 detected! 需要手动解决"
    log "本地: $LOCAL"
    log "远程: $REMOTE"
    log "共同祖先: $BASE"
fi

# 检查冲突标记
if git diff --check 2>/dev/null; then
    log "无冲突标记"
else
    log "⚠️  存在冲突标记文件!"
fi

log "=== 同步完成 ==="
log ""

# 输出汇总
echo ""
echo "=== 同步汇总 ==="
echo "时间: $TIMESTAMP"
echo "分支: $(git branch --show-current)"
echo "最后提交: $(git log -1 --oneline)"
echo "详细日志: $SYNC_LOG"
