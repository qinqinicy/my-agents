<script setup>
const { value } = defineProps({
  item: {
    type: String,
    default: ""
  },
  value: {
    type: String,
    default: ""
  }
});
</script>

<template>
  <div class="text-nowrap whitespace-nowrap max-w-[80px] overflow-hidden">
    {{ value }}
  </div>
  <div class="down-icon w-[6px] h-[4px] ml-[4px]"></div>
</template>

<style lang="less" scoped>
.down-icon {
  background: url("@/assets/images/menu-down-icon.png") 0px 0px no-repeat;
  background-size: 100%;
}
</style>
