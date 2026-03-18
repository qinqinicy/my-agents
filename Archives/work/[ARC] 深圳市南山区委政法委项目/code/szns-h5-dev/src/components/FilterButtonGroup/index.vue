<template>
  <div class="fliter-container flex">
    <div
      v-for="(item, index) in stateList"
      :key="index"
      class="filter-item flex-1"
      :class="{
        active: state === item.id ? 'active' : '',
        bold,
        border: state === item.id && border
      }"
      @click="handleStateChange(item.id)"
    >
      {{ item.title }}
    </div>
  </div>
</template>
<script setup>
const { stateList, state } = defineProps({
  stateList: {
    type: Array,
    default: () => []
  },
  state: {
    type: Number,
    default: 0
  },
  bold: {
    type: Boolean,
    default: false
  },
  border: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(["update:state", "change"]);
const handleStateChange = state => {
  console.log(state, 999);
  emit("update:state", state);
  emit("change", state);
};
</script>
<style lang="less" scoped>
.fliter-container {
  font-weight: 400;
  font-size: 14px;
  color: #767676;
  line-height: 32px;
  text-align: center;
  font-style: normal;

  .filter-item {
    border-radius: 2px;
    &.active {
      background: rgba(22, 153, 173, 0.1);
      color: #1699ad;
      &.border {
        border: 1px solid rgba(22, 153, 173, 0.34);
        line-height: 30px;
      }
    }

    &.bold {
      font-weight: 500;
      border: none;
      line-height: 32px;
    }
  }
}
</style>
