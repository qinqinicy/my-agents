# 本地大模型部署记录

> 横琴项目 - 本地大模型部署与 API 服务搭建

**创建日期**: 2026-03-19
**状态**: 已完成基础部署，待扩展孔明平台集成

---

## 部署目标

在横琴项目本地环境部署大语言模型，提供 API 服务支撑项目智能问答、文档分析、代码生成等能力。

---

## 一、Ollama 部署

### 1.1 硬件环境

| 组件 | 配置 |
|------|------|
| CPU | Intel Core i9-14900KF |
| GPU | NVIDIA RTX 4080 SUPER 16GB |
| 内存 | 32GB DDR5 |
| 系统 | Windows 11 |

### 1.2 安装步骤

#### 步骤 1：下载 Ollama
```powershell
# 方式一：官网下载（推荐）
# 访问 https://ollama.com/download/windows
# 下载 OllamaSetup.exe 并运行

# 方式二：使用 winget
winget install Ollama.Ollama
```

#### 步骤 2：验证安装
```powershell
ollama --version
```

### 1.3 模型部署

#### 拉取 Qwen3-8B
```powershell
# 拉取模型（约 4.7GB）
ollama pull qwen3:8b

# 运行模型
ollama run qwen3:8b
```

#### 可选量化版本
```powershell
# 4-bit 量化（约 4.2GB，速度更快）
ollama pull qwen3:8b:4bit

# 8-bit 量化（约 8GB，精度更高）
ollama pull qwen3:8b:8bit

# 原始精度（约 16GB，显存够用）
ollama pull qwen3:8b:full
```

### 1.4 GPU 直通配置

#### 检查 GPU 识别
```powershell
ollama ps
```

预期输出：
```
NAME       ID           SIZE  PROCESSOR
qwen3:8b   xxxxxxxxx    8GB   100% GPU
```

#### 环境变量配置（如未启用 GPU）
```powershell
# 设置 GPU 层数（Qwen3-8B 总共 35 层）
$env:OLLAMA_GPU_LAYERS="35"

# 永久设置
[System.Environment]::SetEnvironmentVariable('OLLAMA_GPU_LAYERS', '35', 'User')
```

### 1.5 API 调用

#### 后台启动服务
```powershell
Start-Process ollama serve
```

#### API 调用示例
```powershell
# PowerShell 调用
curl http://localhost:11434/api/generate -d @{
    model = "qwen3:8b"
    prompt = "你好，介绍一下你自己"
    stream = $false
} | ConvertFrom-Json

# Python 调用
import requests
response = requests.post('http://localhost:11434/api/generate', json={
    'model': 'qwen3:8b',
    'prompt': '你好',
    'stream': False
})
print(response.json())
```

### 1.6 性能指标

| 指标 | 预期值 |
|------|--------|
| 首 Token 延迟 | 50-100ms |
| 生成速度 | 40-60 tokens/s |
| 显存占用 | 6-8GB（4-bit 量化） |
| 内存占用 | 8-10GB |

### 1.7 注意事项

1. **NVIDIA 驱动**: 升级到最新版（550+ 系列）
2. **端口**: 默认 11434，确保防火墙放行
3. **并发**: 16GB 显存可同时跑 2-3 个 8B 模型
4. **温度**: 建议监控 GPU 温度，上限 85°C

---

## 二、孔明平台集成（待扩展）

> 孔明平台（Polaris）是大模型应用开发平台，支持模型管理、API 网关、应用编排等能力。

### 2.1 部署计划

- [ ] 孔明平台本地部署
- [ ] 孔明平台与 Ollama 对接
- [ ] 统一 API 网关配置
- [ ] 模型路由策略
- [ ] 访问控制与鉴权

### 2.2 预期架构

```
┌─────────────────────────────────────────────────────┐
│                   应用层                              │
│   智能问答  │  文档分析  │  代码助手  │  数据洞察       │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│              孔明平台（Polaris）                      │
│   API 网关  │  模型路由  │  鉴权控制  │  流量管理       │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│              模型服务层                              │
│   Ollama (Qwen3-8B)  │  vLLM  │  其他模型服务         │
└─────────────────────────────────────────────────────┘
```

### 2.3 待办事项

| 任务 | 优先级 | 说明 |
|------|--------|------|
| 孔明平台安装部署 | 高 | 本地环境安装 |
| Ollama 接入孔明 | 高 | 配置模型提供商 |
| API 统一封装 | 中 | 标准化接口格式 |
| 鉴权配置 | 中 | API Key 管理 |
| 监控告警 | 低 | 服务健康检查 |

---

## 三、API 接口规范

### 3.1 Ollama 原生接口

```
POST /api/generate
POST /api/chat
GET  /api/tags
GET  /api/ps
```

### 3.2 统一接口（待实现）

```
POST /api/v1/completions      # 兼容 OpenAI 格式
POST /api/v1/chat/completions # 兼容 OpenAI 聊天
GET  /api/v1/models           # 模型列表
```

---

## 更新记录

| 日期 | 变更内容 |
|------|----------|
| 2026-03-19 | 创建部署文档，完成 Ollama+Qwen3-8B 部署 |
| TBD | 孔明平台集成 |
