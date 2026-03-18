<template>
  <van-search
    v-model="searchValue"
    shape="round"
    :placeholder="placeholder"
    :left-icon="leftIcon"
    class="search-container"
  />
</template>

<script setup>
import leftIcon from "@/assets/images/search-left-icon.png";
const { placeholder } = defineProps({
  placeholder: {
    type: String,
    default: "请输入搜索关键词"
  }
});
const searchValue = defineModel("searchValue");
</script>

<style lang="less">
:root:root {
  --van-search-padding: 0;
  --van-search-background: transparent;
  --van-search-content-background: #f4f4f4;
  --van-search-input-height: 32px;
  --van-search-label-padding: 100px;
}
.search-container {
  .van-search__content--round {
    border-radius: 19px;
  }
  .van-search__content {
    padding-left: 12px;
  }
  .van-field__left-icon {
    margin-right: 4px;
  }
}
</style>
