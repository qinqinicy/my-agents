# Loading_files - 引用文件仓库

> 这里是 Work/ 和 Personal/ 项目产出的**原始材料**存储地。

## 与项目区的映射关系

### Work/ (工作项目)

| Loading_files/work/ | Work/projects/ | 状态 |
|---------------------|----------------|------|
| 横琴全空间无人体系智能数据中心项目 | 横琴全空间无人体系智能数据中心项目 | 进行中（主要项目） |
| 深圳市南山区委政法委项目 | (已归档) | 已交付 |

### Personal/ (个人项目)

| Loading_files/personal/ | Personal/projects/ | 状态 |
|-------------------------|--------------------|------|
| (空) | 交通部第四批招聘笔试备考 | 备考中 |

## 标准目录结构

```
Loading_files/
├── work/
│   └── <项目名>/
│       ├── docs/        # 需求文档、设计文档、会议纪要等
│       ├── code/        # 源代码、配置文件
│       ├── builds/      # 编译产物（JAR、dist 等）
│       └── assets/      # 图片、字体、模板等素材
└── personal/
    └── <项目名>/
        ├── docs/
        ├── code/
        └── ...
```

## 当前项目结构

### 横琴全空间无人体系智能数据中心项目

```
Loading_files/work/横琴全空间无人体系智能数据中心项目/
├── docs/
│   └── 全空间智能无人体系数据资产门户需求规格说明书.docx
└── code/
    └── (待添加)
```

### 深圳市南山区委政法委项目

```
Loading_files/work/深圳市南山区委政法委项目/
├── code/
│   └── decision/          # Java 后端源码 (原始位置，已移至 builds)
└── builds/
    └── decision/
        └── target/        # 编译产物 (decision.jar 等)
```

## 使用说明

1. **新增项目材料**：在对应项目的 `docs/` 或 `code/` 下存放
2. **编译产物**：统一放入 `builds/` 目录，避免污染源码目录
3. **项目关联**：每个 Loading_files 项目对应 Work/projects/ 或 Personal/projects/ 中的一个项目

## 版本控制

- `Loading_files/` 已在 `.gitignore` 中，不会被 Git 跟踪
- 重要文档应同步到 Work/projects/ 或 Personal/projects/ 中的 Git 仓库
