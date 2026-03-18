#!/usr/bin/env python3
"""
Loading_files 自动整理脚本
每晚 11 点运行，将散落在项目根目录的文件按类型归类到对应目录
"""

import json
import os
import shutil
import fnmatch
from pathlib import Path
from datetime import datetime

# 配置路径
SCRIPT_DIR = Path(__file__).parent
CONFIG_FILE = SCRIPT_DIR / ".categories.json"
LOG_FILE = SCRIPT_DIR / ".organize.log"

def load_config():
    """加载分类配置"""
    with open(CONFIG_FILE, 'r', encoding='utf-8') as f:
        return json.load(f)

def log(message):
    """记录日志"""
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    log_entry = f"[{timestamp}] {message}\n"
    with open(LOG_FILE, 'a', encoding='utf-8') as f:
        f.write(log_entry)
    print(log_entry.strip())

def should_ignore(filename, ignore_patterns):
    """检查文件是否应该被忽略"""
    for pattern in ignore_patterns:
        if fnmatch.fnmatch(filename, pattern) or pattern in filename:
            return True
    return False

def find_category(filepath, categories):
    """根据文件路径查找匹配的分类"""
    filename = filepath.name
    file_ext = filepath.suffix.lower()

    for cat_name, cat_config in categories.items():
        # 检查扩展名匹配
        if file_ext in cat_config.get('extensions', []):
            return cat_name

        # 检查模式匹配
        for pattern in cat_config.get('patterns', []):
            if fnmatch.fnmatch(filename, pattern) or pattern in filename:
                return cat_name

    return None

def organize_project(project_path, categories, ignore_patterns):
    """整理单个项目目录"""
    log(f"整理项目：{project_path.name}")

    organized_count = 0
    moved_files = []
    pending_moves = []  # (file_item, category)

    # 扫描项目根目录的文件和子目录
    for item in project_path.iterdir():
        # 跳过隐藏文件和已存在的分类目录
        if item.name.startswith('.') or item.name in categories:
            continue

        if item.is_file():
            # 检查是否应该忽略
            if should_ignore(item.name, ignore_patterns):
                log(f"  忽略：{item.name}")
                continue

            # 查找匹配的分类
            category = find_category(item, categories)
            if category:
                pending_moves.append((item, category))
            else:
                log(f"  未分类：{item.name}")

        elif item.is_dir():
            # 检查是否是构建产物目录
            if item.name in ['target', 'build', 'dist', 'out']:
                pending_moves.append((item, 'builds'))

    # 只有当某类型有文件时才创建目录并移动
    for item, category in pending_moves:
        target_dir = project_path / category
        target_dir.mkdir(exist_ok=True)

        if item.is_file():
            target_path = target_dir / item.name
            # 处理重名文件
            if target_path.exists():
                base = item.stem
                ext = item.suffix
                counter = 1
                while target_path.exists():
                    target_path = target_dir / f"{base}_{counter}{ext}"
                    counter += 1
            shutil.move(str(item), str(target_path))
            log(f"  移动：{item.name} → {category}/")
        elif item.is_dir():
            target_path = target_dir / item.name
            if target_path.exists():
                # 合并目录
                for sub_item in item.iterdir():
                    shutil.move(str(sub_item), str(target_path / sub_item.name))
                item.rmdir()
            else:
                shutil.move(str(item), str(target_path))
            log(f"  移动目录：{item.name} → {category}/")

        moved_files.append((item.name, category))
        organized_count += 1

    return organized_count, moved_files

def main():
    """主函数"""
    log("=" * 50)
    log("开始整理 Loading_files")

    config = load_config()
    categories = config['categories']
    ignore_patterns = config['ignore']['patterns']
    project_areas = config.get('projectAreas', ['work', 'personal'])

    total_organized = 0
    all_moved = []

    # 遍历每个项目区域 (work/personal)
    for area in project_areas:
        area_path = SCRIPT_DIR / area
        if not area_path.exists():
            continue

        # 遍历每个项目
        for project_path in area_path.iterdir():
            if not project_path.is_dir() or project_path.name.startswith('.'):
                continue

            organized, moved = organize_project(project_path, categories, ignore_patterns)
            total_organized += organized
            all_moved.extend(moved)

    # 总结
    log(f"整理完成：共整理 {total_organized} 个文件/目录")
    if all_moved:
        log("移动详情:")
        for filename, category in all_moved:
            log(f"  - {filename} → {category}/")

    log("=" * 50)

if __name__ == '__main__':
    main()
