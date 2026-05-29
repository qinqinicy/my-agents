#!/bin/bash
# 批量修复 drone-segment MP4 的 creation_time 元数据（v2：输出到 /tmp 再 mv）

FFMPEG="/opt/media/tools/ffmpeg"
BASE_DIR="/opt/media/www/record/cszl/drone-segment"

fixed=0
failed=0

for filepath in $(find "$BASE_DIR" -name "*.mp4" -not -name "*_recording*" | sort); do
    filename=$(basename "$filepath")
    if [[ "$filename" =~ ^[0-9]+_([0-9]{14})_[0-9]+\.mp4$ ]]; then
        start_str="${BASH_REMATCH[1]}"
        creation_time="${start_str:0:4}-${start_str:4:2}-${start_str:6:2}T${start_str:8:2}:${start_str:10:2}:${start_str:12:2}.000000Z"
        tmpfile="/tmp/fix_${filename}"

        echo "Fixing: $filename -> $creation_time"
        "$FFMPEG" -y -i "$filepath" -c copy -movflags +faststart \
            -metadata "creation_time=$creation_time" "$tmpfile" >/dev/null 2>&1

        if [ $? -eq 0 ]; then
            mv -f "$tmpfile" "$filepath"
            echo "  OK"
            ((fixed++))
        else
            echo "  FAIL"
            rm -f "$tmpfile"
            ((failed++))
        fi
    fi
done

echo ""
echo "Fixed: $fixed, Failed: $failed"
