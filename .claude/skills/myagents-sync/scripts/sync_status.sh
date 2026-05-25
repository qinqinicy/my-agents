#!/bin/bash
# MyAgents 同步状态检查脚本
# 用法: ./sync_status.sh

set -e

cd /Users/qinqin/Library/CloudStorage/OneDrive-个人/Share/WinMac/Qin/MyAgents 2>/dev/null || {
    echo "Error: 工作区目录不存在"
    exit 1
}

SYNC_LOG="$HOME/.myagents/sync.log"

echo "=== MyAgents 多端同步状态 ==="
echo ""

# 分支信息
echo "📍 当前分支: $(git branch --show-current)"
echo "📍 远程仓库: $(git remote get-url origin 2>/dev/null | sed 's|https://github.com/||' || echo '未配置')"
echo ""

# 本地变更
GIT_STATUS=$(git status -s)
if [[ -z "$GIT_STATUS" ]]; then
    echo "📦 工作区: 干净"
else
    echo "📦 工作区: 有变更 ($(echo "$GIT_STATUS" | wc -l | tr -d ' ') 个文件)"
    echo "$GIT_STATUS" | head -10
    if [[ $(echo "$GIT_STATUS" | wc -l) -gt 10 ]]; then
        echo "... 还有更多文件"
    fi
fi
echo ""

# 与远程对比
LOCAL=$(git rev-parse @)
REMOTE=$(git rev-parse @{u} 2>/dev/null || echo "$LOCAL")
BASE=$(git merge-base @ @{u} 2>/dev/null || echo "$BASE")

if [[ "$LOCAL" == "$REMOTE" ]]; then
    echo "🔄 与远程: 完全同步"
elif [[ "$LOCAL" == "$BASE" ]]; then
    echo "🔻 远程领先: 需要 pull"
    BEHIND=$(git rev-list --count @{u}..@ 2>/dev/null || echo "?")
    echo "   落后 $BEHIND 个提交"
elif [[ "$REMOTE" == "$BASE" ]]; then
    echo "🔺 本地领先: 需要 push"
    AHEAD=$(git rev-list --count @..@{u} 2>/dev/null || echo "?")
    echo "   领先 $AHEAD 个提交"
else
    echo "⚠️  分歧: 本地和远程有冲突的变更"
fi
echo ""

# 最后同步时间
if [[ -f "$SYNC_LOG" ]]; then
    LAST_SYNC=$(tail -20 "$SYNC_LOG" | grep "=== 同步完成 ===" | tail -1 | grep -oP '\[\K[0-9-]+ [0-9:]+')
    if [[ -n "$LAST_SYNC" ]]; then
        echo "🕐 最后同步: $LAST_SYNC"
    fi
else
    echo "🕐 最后同步: 无记录"
fi
echo ""

# 最后提交
echo "📝 最后提交:"
git log -1 --format="   %h %s (%cr)" 2>/dev/null || echo "   无提交"
echo ""

# 未提交的变更预览
UNTRACKED=$(git ls-files --others --exclude-standard 2>/dev/null | head -5)
if [[ -n "$UNTRACKED" ]]; then
    echo "📋 未跟踪文件 (前5个):"
    echo "$UNTRACKED" | sed 's/^/   /'
fi
