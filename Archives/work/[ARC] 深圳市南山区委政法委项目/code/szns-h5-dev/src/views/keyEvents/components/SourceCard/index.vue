<template>
  <div v-if="lable" class="source-group px-[6px] ml-[8px]">{{ lable }}</div>
</template>
<script setup>
const { source, lable } = defineProps({
  source: {
    type: String,
    default: ""
  },
  lable: {
    type: String,
    default: ""
  }
});
function statusFilter(status) {
  const statusMap = {
    1: "处理中",
    2: "未处理",
    3: "已处理"
  };
  return statusMap[status];
}
</script>
<style lang="less" scoped>
.source-group {
  height: 20px;
  background: rgba(39, 200, 233, 0.12);
  border-radius: 4px;
  font-weight: 400;
  font-size: 12px;
  color: #27c8e9;
  line-height: 20px;
  text-align: center;
  font-style: normal;
}
</style>
