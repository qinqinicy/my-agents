<template>
  <div class="p-[12px]" v-if="route.query.bc === '1'">
    <Breadcrumb :list="breadcrumbList"></Breadcrumb>
  </div>
  <div
    class="px-[12px] mt-[2px]"
    :class="route.query.bc !== '1' ? 'py-[12px]' : ''"
  >
    <div
      class="monitors bg-white rounded-[8px] py-[10px] px-[14px] pt-0 overflow-hidden"
    >
      <!-- <div class="title">事件列表</div> -->
      <!-- 提示 -->
      <div
        v-if="fxNum > 0"
        class="notice-bar bg-[#FF6249]/[.09] rounded-[2px] px-[16px] py-[5px] mt-[10px] text-[#FF6249] text-[12px] leading-17px relative flex items-center"
      >
        <van-icon class-prefix="icon van-icon" color="#FF6249" name="bell" />
        <span class="ml-[4px]"
          >今日有{{ fxNum }}项指标存在风险，请您重点关注！</span
        >
      </div>
      <!-- 列表 -->
      <div
        v-for="(item, index) in monitorsList"
        :key="index"
        :class="index <= 0 ? 'mt-[12px]' : 'mt-[16px]'"
      >
        <focus-on-checking
          class="mt-[12px]"
          :title="item.title"
          :list="item.list"
          :last="index >= monitorsList.length - 1"
        ></focus-on-checking>
      </div>
    </div>
  </div>
</template>
<script setup>
import FocusOnChecking from "@/components/FocusOnChecking/index.vue";
import { monitorskey } from "@/api/monitors";
import { isArray } from "@/utils/validate";
const breadcrumbList = ["首页", "风险提醒"];
const route = useRoute();
const fxNum = ref(0);
const monitorsList = ref([]);
const getMonitorskey = () => {
  monitorskey({})
    .then(res => {
      fxNum.value = res.result.fxNum || 0;
      if (isArray(res.result.detailList)) {
        monitorsList.value = [...res.result.detailList];
      } else {
        monitorsList.value = [];
      }
      console.log(monitorsList);
    })
    .catch(() => {
      fxNum.value = 0;
      monitorsList.value = [];
    });
};
onMounted(() => {
  getMonitorskey();
});
</script>
<style lang="less" scoped>
.title {
  font-weight: 500;
  font-size: 16px;
  color: #102f2e;
  line-height: 26px;
  text-align: left;
  font-style: normal;
}
.icon {
  width: 9px;
  height: 11px;
}
</style>
