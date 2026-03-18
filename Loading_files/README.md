# Loading_files - 引用文件仓库

> 这里是上传基础资料的入口，按项目组织原始材料。

## 与项目区的映射关系

### Work (工作项目)

| Loading_files/work/ | Projects/work/ | 状态 |
|---------------------|----------------|------|
| [WIP] 横琴全空间无人体系智能数据中心项目 | [WIP] 横琴全空间无人体系智能数据中心项目 | 进行中（主要项目） |

### Personal (个人项目)

| Loading_files/personal/ | Projects/personal/ | 状态 |
|-------------------------|--------------------|------|
| (空) | (空) | - |

## 标准目录结构

```
Loading_files/
├── work/
│   └── [状态] 项目名/
│       ├── docs/        # 需求文档、设计文档、会议纪要等
│       ├── code/        # 源代码、配置文件
│       ├── builds/      # 编译产物（JAR、dist 等）
│       └── assets/      # 图片、字体、模板等素材
└── personal/
    └── [状态] 项目名/
        ├── docs/
        ├── code/
        └── ...
```

**注意**：只有当项目有实际文件时才会创建对应分类目录。

## 当前项目结构

### 横琴全空间无人体系智能数据中心项目

```
Loading_files/work/[WIP] 横琴全空间无人体系智能数据中心项目/
├── docs/
│   └── 全空间智能无人体系数据资产门户需求规格说明书.docx
└── code/
    └── (待添加)
```

## 使用说明

1. **上传基础资料**：在对应项目的 `docs/` 或 `code/` 下存放原始材料
2. **编译产物**：统一放入 `builds/` 目录，避免污染源码目录
3. **项目关联**：每个 Loading_files 项目对应 `Projects/work/` 或 `Projects/personal/` 中的一个项目
4. **自动整理**：通过心跳唤醒运行 `organize.py` 整理散落文件
5. **项目索引**：查看 `Projects/PROJECTS.md` 了解所有项目的状态和映射关系

## 工作区结构

```
MyAgents/
├── AgentSystem/       # Agent 核心系统
├── Archives/          # 归档仓库（已交付项目归档）
│   ├── ARCHIVES_LOG.md  # 归档日志
│   ├── personal/        # 个人归档
│   └── work/            # 工作归档
├── Knowledge/         # 知识库（跨项目知识沉淀）
│   ├── personal/      # 个人知识
│   ├── public/        # 公共知识（跨工作/个人）
│   └── work/          # 工作知识
├── Loading_files/     # 引用文件仓库（上传的基础资料）
│   ├── personal/
│   └── work/
├── Projects/          # 项目资产（状态前缀管理）
│   ├── PROJECTS.md    # 全局项目索引
│   ├── personal/
│   │   ├── [HLD] 暂停项目
│   │   └── [WIP] 进行中项目
│   └── work/
│       ├── [HLD] 暂停项目
│       └── [WIP] 进行中项目
└── SilentSpace/       # 临时工作台（每日草稿纸）
    ├── personal/
    └── work/
```

**项目状态前缀**：
- `[WIP]` - Work In Progress，进行中
- `[HLD]` - Hold，暂停/挂起
- `[ARC]` - Archived，已归档（仅在 Archives/ 中）

## 文件流转

```
Loading_files/（上传的基础资料）
    ↓
Projects/（建立对应项目，状态前缀管理）
    ↓
SilentSpace/（临时工作台，每日草稿纸）
    ↓
Archives/（项目归档）
    ↓
Knowledge/（知识沉淀，可复用知识）
```

**项目同步规则**：项目名称在 `Projects/`、`Loading_files/`、`SilentSpace/`、`Archives/` 之间保持一致。修改 `Projects/` 中的项目名称后，其他位置应同步更新。

## 自动整理

通过心跳唤醒运行整理脚本：

```bash
# 手动运行整理脚本
python3 Loading_files/organize.py

# 查看整理日志
cat Loading_files/.organize.log
```

### 分类规则

编辑 `.categories.json` 可自定义分类：

| 分类 | 扩展名示例 | 匹配模式 |
|------|------------|---------|
| `docs/` | .md, .doc, .docx, .pdf | *需求*, *设计*, *说明* |
| `code/` | .java, .js, .ts, .py | *.java, *.js |
| `config/` | .yml, .json, .xml | pom.xml, package.json |
| `builds/` | .jar, .war, .class | target/, build/, dist/ |
| `assets/` | .png, .jpg, .svg | *图标*, *图片* |
| `archive/` | .zip, .rar, .7z | - |

## 版本控制

- `Loading_files/` 已在 `.gitignore` 中，不会被 Git 跟踪
- 重要文档应同步到 `Projects/work/` 或 `Projects/personal/` 中的 Git 仓库
