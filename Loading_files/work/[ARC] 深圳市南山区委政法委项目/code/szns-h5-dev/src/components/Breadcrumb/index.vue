<template>
  <div class="breadcrumb-wrapper flex justify-between">
    <div class="breadcrumb-list flex flex-1">
      <span v-for="(item, index) in list" :key="index">
        {{ index >= 1 ? "/" + item : item }}
      </span>
    </div>
    <div
      class="breadcrumb-back w-[71px] h-[22px] ml-[5px]"
      @click="onBack"
    ></div>
  </div>
</template>
<script setup name="Breadcrumb">
const router = useRouter();
const { list, customBack } = defineProps({
  list: {
    type: Array,
    default: () => []
  },
  customBack: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(["back"]);
function onBack() {
  console.log(customBack);
  if (!customBack) {
    if (window.history.state.back) history.back();
    else router.replace("/");
  } else {
    emit("back");
  }
}
</script>
<style lang="less">
// 面包屑
.breadcrumb-wrapper {
  .breadcrumb-list {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    span:last-child {
      color: #1699ad;
    }
  }
  .breadcrumb-back {
    background: url("@/assets/images/breadcrumb-back.png") 0px 0px no-repeat;
    background-size: 100%;
  }
}
</style>
