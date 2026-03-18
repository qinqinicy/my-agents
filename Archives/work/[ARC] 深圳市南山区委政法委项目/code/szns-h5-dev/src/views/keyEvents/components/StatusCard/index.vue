<template>
  <div class="status-group px-[6px] success">
    <!-- :class="status === '0' ? 'danger' : status === '2' ? 'success' : 'warning'" -->
    {{ status }}
  </div>
</template>
<script setup>
const { status } = defineProps({
  status: {
    type: [Number, String],
    default: null
  }
});
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
