# Loading_files - 引用文件仓库

> 这里是 `Projects/work/` 和 `Projects/personal/` 项目产出的**原始材料**存储地。

## 与项目区的映射关系

### Work (工作项目)

| Loading_files/work/ | Projects/work/ | 状态 |
|---------------------|----------------|------|
| [WIP] 横琴全空间无人体系智能数据中心项目 | [WIP] 横琴全空间无人体系智能数据中心项目 | 进行中（主要项目） |
| [ARC] 深圳市南山区委政法委项目 | [ARC] 深圳市南山区委政法委项目 | 已交付 |

### Personal (个人项目)

| Loading_files/personal/ | Projects/personal/ | 状态 |
|-------------------------|--------------------|------|
| (空) | [OK] 交通部第四批招聘笔试备考 | 已完成 |

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

## 当前项目结构

### 横琴全空间无人体系智能数据中心项目

```
Loading_files/work/[WIP] 横琴全空间无人体系智能数据中心项目/
├── docs/
│   └── 全空间智能无人体系数据资产门户需求规格说明书.docx
└── code/
    └── (待添加)
```

### 深圳市南山区委政法委项目

```
Loading_files/work/[ARC] 深圳市南山区委政法委项目/
├── code/
│   └── decision/          # Java 后端源码
└── builds/
    └── decision/
        └── target/        # 编译产物 (decision.jar 等)
```

## 使用说明

1. **新增项目材料**：在对应项目的 `docs/` 或 `code/` 下存放
2. **编译产物**：统一放入 `builds/` 目录，避免污染源码目录
3. **项目关联**：每个 Loading_files 项目对应 `Projects/work/` 或 `Projects/personal/` 中的一个项目
4. **自动整理**：通过心跳唤醒运行 `organize.py` 整理散落文件

## 工作区结构

```
MyAgents/
├── AgentSystem/       # Agent 核心系统
├── Archives/          # 归档仓库（已交付项目归档）
│   ├── personal/
│   └── work/
├── Knowledge/         # 知识库（跨项目知识沉淀）
│   ├── personal/      # 个人知识
│   ├── public/        # 公共知识（跨工作/个人）
│   └── work/          # 工作知识
├── Loading_files/     # 引用文件仓库（自动整理）
│   ├── personal/
│   └── work/
├── Projects/          # 项目资产（状态前缀管理）
│   ├── personal/
│   │   ├── [OK] 已完成项目
│   │   ├── [HLD] 暂停项目
│   │   └── [WIP] 进行中项目
│   └── work/
│       ├── [OK] 已完成项目
│       ├── [HLD] 暂停项目
│       └── [WIP] 进行中项目
└── SilentSpace/       # 临时工作台（每日草稿纸）
    ├── personal/
    └── work/
```

**项目状态前缀**：
- `[WIP]` - Work In Progress，进行中
- `[HLD]` - Hold，暂停/挂起
- `[OK]` / `[ARC]` - 已完成/待归档

## 文件流转

```
SilentSpace/ (临时输出)
    ↓
Loading_files/ (整理归类)
    ↓
Projects/ (状态管理)
    ↓
Archives/ (归档) 或 Knowledge/ (沉淀)
```

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
