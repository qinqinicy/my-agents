#!/bin/bash
# MyAgents 冲突检测脚本
# 用法: ./sync_conflicts.sh

set -e

cd /Users/qinqin/Library/CloudStorage/OneDrive-个人/Share/WinMac/Qin/MyAgents 2>/dev/null || {
    echo "Error: 工作区目录不存在"
    exit 1
}

echo "=== MyAgents 冲突检测 ==="
echo ""

# 1. 检查 git 状态中的未合并文件
CONFLICT_FILES=$(git diff --name-only --diff-filter=U 2>/dev/null)
if [[ -n "$CONFLICT_FILES" ]]; then
    echo "⚠️  Git 冲突文件:"
    echo "$CONFLICT_FILES" | sed 's/^/   /'
    echo ""
fi

# 2. 检查 conflict markers
echo "检查冲突标记 (<<<<<<<, =======, >>>>>>>)..."
CONFLICT_MARKERS=$(grep -rl "<<<<<<<" --include="*.md" --include="*.txt" --include="*.json" . 2>/dev/null | grep -v ".git/" | head -20)
if [[ -n "$CONFLICT_MARKERS" ]]; then
    echo "⚠️  存在冲突标记的文件:"
    echo "$CONFLICT_MARKERS" | sed 's/^/   /'
else
    echo "✅ 无冲突标记"
fi
echo ""

# 3. 检查 rebase in progress
REBASE_IN_PROGRESS=$(git rev-parse --git-path rebase-merge 2>/dev/null || true)
if [[ -d "$REBASE_IN_PROGRESS" ]]; then
    echo "⚠️  Rebase 正在进行中"
    echo "   使用 git rebase --abort 取消 或 git rebase --continue 继续"
    echo ""
fi

# 4. 检查 mergetool 状态
if git diff --staged --name-only | grep -q . 2>/dev/null; then
    STAGED=$(git diff --staged --name-only)
    echo "📋 已暂存的合并冲突文件:"
    echo "$STAGED" | sed 's/^/   /'
    echo ""
fi

# 5. 分支分歧检测
LOCAL=$(git rev-parse @)
REMOTE=$(git rev-parse @{u} 2>/dev/null || echo "$LOCAL")
BASE=$(git merge-base @ @{u} 2>/dev/null || echo "$BASE")

if [[ "$LOCAL" != "$REMOTE" && "$LOCAL" != "$BASE" && "$REMOTE" != "$BASE" ]]; then
    echo "⚠️  检测到分支分歧!"
    echo "   本地提交: $(git log --oneline -1 @)"
    echo "   远程提交: $(git log --oneline -1 @{u} 2>/dev/null || echo 'N/A')"
    echo "   共同祖先: $(git log --oneline -1 $BASE)"
    echo ""
    echo "解决选项:"
    echo "   1. 保留本地: git pull --rebase (变基到远程之上)"
    echo "   2. 保留远程: git reset --hard @{u}"
    echo "   3. 手动合并: git merge @{u}"
    echo ""
fi

# 6. 总结
if [[ -z "$CONFLICT_FILES" && -z "$CONFLICT_MARKERS" && ! -d "$REBASE_IN_PROGRESS" ]]; then
    if [[ "$LOCAL" == "$REMOTE" ]]; then
        echo "✅ 无冲突，完全同步"
    else
        echo "✅ 无冲突，但有未同步的提交"
    fi
else
    echo "⚠️  需要手动处理冲突"
fi
