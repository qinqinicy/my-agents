#!/bin/bash
#
# MiniMax MCP 图片理解调用脚本
# 用法: bash understand_image.sh <图片路径> "<分析要求>"
#

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# 动态查找带 minimax_mcp 的 Python（uv 或系统 python3）
find_python() {
    # 方式 1: uvx 自带的 python
    if command -v uvx &> /dev/null; then
        local uv_python
        uv_python=$(uv python find 2>/dev/null | head -1)
        if [ -n "$uv_python" ] && [ -x "$uv_python" ] && "$uv_python" -c "import minimax_mcp.server" 2>/dev/null; then
            echo "$uv_python"
            return 0
        fi
    fi
    # 方式 2: 系统 python3
    if command -v python3 &> /dev/null && python3 -c "import minimax_mcp.server" 2>/dev/null; then
        echo "python3"
        return 0
    fi
    # 方式 3: 回退 python3（可能报错，但给出明确提示）
    echo "python3"
    return 1
}

MCP_PYTHON=$(find_python)

# 检查参数
if [ $# -lt 2 ]; then
    echo "用法: $0 <图片路径或URL> <分析要求>"
    echo "示例: $0 ~/photo.jpg \"详细描述这张图片\""
    exit 1
fi

IMAGE_SOURCE="$1"
PROMPT="$2"

# 检查 API Key
if [ -z "$MINIMAX_API_KEY" ]; then
    echo "错误: MINIMAX_API_KEY 未设置"
    echo "请先设置: export MINIMAX_API_KEY=\"你的Token Plan API Key\""
    exit 1
fi

# 检查 minimax_mcp 是否可用
if ! "$MCP_PYTHON" -c "import minimax_mcp.server" 2>/dev/null; then
    echo "错误: minimax_mcp 未安装"
    echo "请先运行: uvx minimax-coding-plan-mcp"
    exit 1
fi

# 自动检测系统代理（macOS 专用，其他平台跳过）
if [ -z "$HTTP_PROXY" ] && [ -z "$HTTPS_PROXY" ] && command -v networksetup &> /dev/null; then
    PROXY_INFO=$(networksetup -getwebproxy "Wi-Fi" 2>/dev/null || networksetup -getwebproxy "Ethernet" 2>/dev/null)
    if echo "$PROXY_INFO" | grep -q "Enabled: Yes"; then
        PROXY_SERVER=$(echo "$PROXY_INFO" | grep "Server:" | awk '{print $2}')
        PROXY_PORT=$(echo "$PROXY_INFO" | grep "Port:" | awk '{print $2}')
        if [ -n "$PROXY_SERVER" ] && [ -n "$PROXY_PORT" ]; then
            export HTTP_PROXY="http://${PROXY_SERVER}:${PROXY_PORT}"
            export HTTPS_PROXY="http://${PROXY_SERVER}:${PROXY_PORT}"
        fi
    fi
fi

# 调用 MCP
RESULT=$(echo '{"jsonrpc":"2.0","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":{"name":"hanako-skill","version":"1.0"}},"id":0}
{"jsonrpc":"2.0","method":"notifications/initialized","params":{}}
{"jsonrpc":"2.0","method":"tools/call","id":2,"params":{"name":"understand_image","arguments":{"prompt":"'"$PROMPT"'","image_source":"'"$IMAGE_SOURCE"'"}}}' | \
    MINIMAX_API_KEY="$MINIMAX_API_KEY" \
    MINIMAX_API_HOST="${MINIMAX_API_HOST:-https://api.minimaxi.com}" \
    HTTP_PROXY="${HTTP_PROXY:-}" \
    HTTPS_PROXY="${HTTPS_PROXY:-}" \
    "$MCP_PYTHON" -c "
import sys, os, importlib

# 继承环境变量中的代理设置
os.environ['HTTP_PROXY'] = os.environ.get('HTTP_PROXY', '')
os.environ['HTTPS_PROXY'] = os.environ.get('HTTPS_PROXY', '')

from minimax_mcp.server import main
main()
" 2>/dev/null)

# 解析 JSON-RPC 响应
if echo "$RESULT" | python3 -c "
import sys, json
for line in sys.stdin:
    try:
        resp = json.loads(line)
        if resp.get('id') == 2 and 'result' in resp:
            content = resp['result'].get('content', [])
            if content and content[0].get('type') == 'text':
                print(content[0]['text'])
    except:
        pass
" 2>/dev/null; then
    :
else
    echo "错误: 无法解析 MCP 响应"
    echo "原始响应: $RESULT"
    exit 1
fi