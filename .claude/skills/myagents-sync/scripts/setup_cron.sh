#!/bin/bash
# MyAgents 定时同步 Cron 设置脚本
# 用法: ./setup_cron.sh [interval_minutes]
# 默认: 30 分钟

INTERVAL=${1:-30}
SKILL_DIR="/Users/qinqin/Library/CloudStorage/OneDrive-个人/Share/WinMac/Qin/MyAgents/.claude/skills/myagents-sync/scripts"
PROMPT="执行 MyAgents 工作区同步 (git pull --rebase && git push)，如有冲突则记录到日志并标记需要手动处理"

echo "=== MyAgents 定时同步设置 ==="
echo ""
echo "同步间隔: 每 $INTERVAL 分钟"
echo ""

# 检查 myagents cron 是否可用
if ! command -v myagents &> /dev/null; then
    echo "Error: myagents CLI 未安装"
    exit 1
fi

# 检查是否已有同步任务
EXISTING=$(myagents cron list --json 2>/dev/null | grep -i "myagents-sync\|workspace-sync\|同步" || true)
if [[ -n "$EXISTING" ]]; then
    echo "⚠️  已存在同步任务:"
    echo "$EXISTING"
    echo ""
    read -p "是否删除现有任务后重新创建? (y/N): " CONFIRM
    if [[ "$CONFIRM" != "y" ]]; then
        echo "取消操作"
        exit 0
    fi
    # 删除现有任务 (需要 task ID)
    echo "请先使用 'myagents cron list' 查看并删除现有任务"
    exit 1
fi

# 创建 cron 任务
echo "创建定时同步任务..."
myagents cron add \
    --name "MyAgents 工作区同步" \
    --prompt "$PROMPT" \
    --every "$INTERVAL"

echo ""
echo "✅ 定时同步任务已创建"
echo ""
echo "查看任务: myagents cron list"
echo "查看执行记录: myagents cron runs <taskId>"
