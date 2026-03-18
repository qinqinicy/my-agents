<template>
  <div
    class="status-group px-[6px]"
    :class="status === '0' ? 'danger' : status === '2' ? 'success' : 'warning'"
  >
    {{ statusFilter(status) }}
  </div>
</template>
<script setup>
import { isEmpty } from "@/utils/validate";
const { status } = defineProps({
  status: {
    type: [Number, String],
    default: null
  }
});
function statusFilter(status) {
  const statusMap = {
    1: "处理中",
    0: "未处理",
    2: "已处理"
  };
  return statusMap[Number(status)];
}
</script>
<style lang="less" scoped>
.status-group {
  height: 20px;
  border-radius: 4px;
  border: 1px solid transparent;
  font-weight: 400;
  font-size: 12px;
  line-height: 20px;
  text-align: center;
  font-style: normal;
  &.warning {
    color: #fe9856;
    border-color: #fe9856;
    background: rgba(254, 152, 86, 0.15);
  }
  &.success {
    color: #1ab62a;
    border-color: #1ab62a;
    background: rgba(86, 254, 139, 0.15);
  }
  &.danger {
    color: #ff4444;
    border-color: #ff4444;
    background: rgba(255, 68, 68, 0.15);
  }
}
</style>
