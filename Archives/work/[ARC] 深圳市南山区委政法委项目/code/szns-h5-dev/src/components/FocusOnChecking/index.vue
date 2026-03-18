<template>
  <div>
    <div class="title">{{ title }}</div>
    <div v-for="(item, index) in list" :key="index" class="mt-[10px]">
      <focus-on-checking-item
        :temp="item"
        :placement-top="last && index >= list.length - 2"
      ></focus-on-checking-item>
    </div>
  </div>
</template>
<script setup>
import FocusOnCheckingItem from "@/components/FocusOnCheckingItem/index.vue";
const { title, list } = defineProps({
  title: {
    type: String,
    default: ""
  },
  list: {
    type: Array,
    default: () => []
  },
  last: {
    type: Boolean,
    default: false
  }
});
</script>
<style lang="less" scoped>
.title {
  font-weight: 500;
  font-size: 14px;
  color: #102f2e;
  line-height: 20px;
  text-align: left;
  font-style: normal;
}
.item-container {
}
</style>
