<script setup>
import NavBar from "@/components/NavBar/index.vue";
import { useDarkMode } from "@/hooks/useToggleDarkMode";
import { useCachedViewStoreHook } from "@/store/modules/cachedView";

const cachedViews = computed(() => {
  return useCachedViewStoreHook().cachedViewList;
});
</script>

<template>
  <div class="app-wrapper">
    <van-config-provider :theme="useDarkMode() ? 'dark' : 'light'">
      <!-- 背景快 -->
      <!-- <div class="bg-wrapper absolute top-[-20px] right-0"></div> -->
      <!-- <nav-bar /> -->
      <router-view v-slot="{ Component }">
        <keep-alive :include="cachedViews">
          <component :is="Component" />
        </keep-alive>
      </router-view>
    </van-config-provider>
  </div>
</template>

<style lang="less" scoped>
@import "@/styles/mixin.less";

.app-wrapper {
  .clearfix();
  position: relative;
  height: 100%;
  width: 100%;
}
.bg-wrapper {
  width: 333px;
  height: 196px;
  background: linear-gradient(
    166deg,
    rgba(32, 150, 255, 0.36) 0%,
    rgba(29, 143, 244, 0) 100%
  );
  filter: blur(7px);
  z-index: -1;
}
</style>
