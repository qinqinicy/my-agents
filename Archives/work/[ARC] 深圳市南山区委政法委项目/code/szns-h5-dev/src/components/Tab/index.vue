<template>
  <!-- <van-tabs v-model:active="active">
    <van-tab v-for="(item, index) in tabData" :key="index" :title="item.title">
    </van-tab>
  </van-tabs> -->
  <div></div>
</template>
<script setup>
const active = ref(0);
const tabData = reactive([
  { title: "首页" },
  { title: "风险监测" },
  { title: "业务管理" },
  { title: "要素管理" },
  { title: "工作管理" }
]);
</script>
<style lang="less">
:root:root {
  --van-tab-text-color: #818181;
  --van-tab-active-text-color: #1699ad;
  --van-tab-font-size: 14px;
  --van-tab-line-height: 20px;
  --van-tabs-default-color: rgba(255, 255, 255, 0.5);
  --van-tabs-line-height: 50px;
  --van-tabs-card-height: 50px;
  --van-tabs-nav-background: rgba(255, 255, 255, 0.5);
  --van-tabs-bottom-bar-width: 30px;
  --van-tabs-bottom-bar-color: #1699ad;
}
.van-tabs__line {
  bottom: -6px;
}
</style>
