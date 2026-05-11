---
name: minimax-understand-image
description: >
  使用 MiniMax MCP 的 understand_image 工具分析图片。当用户发送图片并要求理解、识别、描述、分析图片内容时激活。
  触发条件：(1) 用户上传或发送了图片 (2) 用户要求分析/理解/描述图片 (3) 需要从图片中提取文字或信息。
---

# MiniMax 图片理解 Skill

使用 MiniMax Token Plan MCP 的 `understand_image` 工具分析图片内容。

## 前置条件

1. `uvx` 已安装（如未安装：`curl -LsSf https://astral.sh/uv/install.sh | sh`）
2. `minimax-coding-plan-mcp` 已安装
3. 环境变量已配置：
   - `MINIMAX_API_KEY` — Token Plan API Key
   - `MINIMAX_API_HOST` — `https://api.minimaxi.com`（中国大陆）

## 使用方式

当用户发送图片并需要理解时，执行以下流程：

### 步骤 1：准备图片

将图片复制到可访问路径。建议放在 `~/minimax-images/` 目录：

```bash
mkdir -p ~/minimax-images
# 图片已在会话中自动保存到 attachments/ 目录
```

### 步骤 2：调用 understand_image

使用 Python 脚本通过 MCP 调用图片理解服务：

```bash
bash scripts/understand_image.sh <图片路径> "<分析要求>"
```

### 脚本参数

| 参数 | 说明 |
|------|------|
| 图片路径 | 本地文件路径或 HTTP/HTTPS URL |
| 分析要求 | 中文描述，如"详细描述图片内容" |

## 示例

```bash
# 描述图片内容
bash scripts/understand_image.sh ~/minimax-images/photo.jpg "详细描述这张图片的内容"

# 提取文字
bash scripts/understand_image.sh ~/minimax-images/screenshot.png "提取图片中的所有文字"

# 识别物体
bash scripts/understand_image.sh ~/minimax-images/product.jpg "识别图片中的物体并说明用途"
```

## 注意事项

- 支持格式：JPEG、PNG、WebP（最大 20MB）
- 图片路径避免包含 `@` 前缀
- 如果图片较大，加载可能需要几秒钟
- API Key 额度用完会返回错误，请确保 Token Plan 在有效期内