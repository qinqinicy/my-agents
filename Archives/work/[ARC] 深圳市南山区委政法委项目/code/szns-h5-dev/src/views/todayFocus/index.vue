<template>
  <div class="p-[12px]" v-if="route.query.bc === '1'">
    <Breadcrumb :list="breadcrumbList"></Breadcrumb>
  </div>
  <div class="search-container p-[16px]">
    <search
      v-model="listQuery.name"
      placeholder="输入事件关键字搜索"
      shape="round"
      @search="onSearch"
    ></search>
    <!-- 筛选 -->
    <DropdownMenu :listQuery="listQuery" class="mt-[10px]" />
  </div>

  <!-- 列表 -->
  <!-- 卡片 -->
  <van-list finished-text="没有更多了">
    <div class="list-wrapper px-[12px] mt-[12px]">
      <div class="list-container bg-white px-[14px] py-[16px] pt-[12px]">
        <div
          v-show="listQuery.beginDate"
          class="text-[15px] text-[#102F2E] font-medium leading-[21px] mb-[10px]"
        >
          {{ listTitle }}
        </div>
        <card
          v-for="(item, index) in list"
          :key="index"
          :item="item"
          class="mb-[12px]"
          @click="handleCardClick(item)"
        ></card>
      </div>
    </div>
  </van-list>
</template>
<script setup name="TodayFocus">
import Search from "@/components/Search/index.vue";
import DropdownMenu from "@/components/DropdownMenu/index.vue";
import Card from "@/components/Card/index.vue";
import { todayEventAttention } from "@/api/home";
import { isArray } from "@/utils/validate";
const breadcrumbList = ["首页", "今日关注"];
const router = useRouter();
const route = useRoute();
// 列表
const list = ref([]);
const listTitle = ref("");
function handleCardClick(item) {
  router.push({
    path: "/eventDetails",
    query: {
      id: item.id,
      bt: 2,
      fr: 6
    }
  });
}
const listQuery = reactive({
  name: "",
  appealSource: null,
  handleStatus: null,
  incidentArea: null,
  beginDate: null,
  endDate: null
});

watch(
  [
    () => listQuery.handleStatus,
    () => listQuery.appealSource,
    () => listQuery.beginDate,
    () => listQuery.endDate,
    () => listQuery.incidentArea
  ],
  () => {
    handleSuccessReq();
  }
);
// 搜索在点击键盘上的搜索/回车按钮后触发
const onSearch = val => {
  handleSuccessReq();
};
// 今日关注列表
const handleSuccessReq = async () => {
  const res = await todayEventAttention({
    ...listQuery
  });
  if (listQuery.beginDate) {
    listTitle.value = `${listQuery.beginDate.replace(/-/g, "/")}-${listQuery.endDate.replace(/-/g, "/")}`;
  } else {
    listTitle.value = "";
  }
  if (isArray(res.result.events)) {
    list.value = [...res.result.events];
  } else {
    list.value = [{}];
  }
};
onMounted(() => {
  handleSuccessReq();
});
</script>
<style lang="less" scoped>
@import url("./index.less");
</style>
