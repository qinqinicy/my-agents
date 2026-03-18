<template>
  <div class="relative">
    <!-- <van-sticky :offset-top="46"> -->
    <tab />
    <div class="p-[12px]">
      <Breadcrumb :list="breadcrumbList"></Breadcrumb>
    </div>
    <!-- </van-sticky> -->
    <div class="audit-center-container px-[12px] pb-[12px]">
      <div class="flex flex-col rounded-[8px] px-[12px] py-[10px] bg-white">
        <span class="title mb-[10px]">今日关注审核中心</span>
        <!-- 审核状态筛选 -->
        <filter-button-group
          v-model:state="listQuery.examineStatus"
          :state-list="stateList"
          @change="handleStateChange"
          class="mb-[12px]"
        ></filter-button-group>
        <!-- 搜索框 -->
        <Search
          v-model="listQuery.name"
          placeholder="输入事件关键字搜索"
          shape="round"
          @search="onSearch"
        />

        <!-- 筛选 -->
        <DropdownMenu :listQuery="listQuery" class="mt-[10px]" />

        <!-- 卡片 -->
        <van-list class="mt-[18px]" finished-text="没有更多了">
          <card
            v-for="(item, index) in list"
            :key="index"
            :item="item"
            class="mb-[12px]"
            @click="handleCardClick(item)"
          ></card>
        </van-list>
      </div>
    </div>
  </div>
</template>

<script setup name="AuditCenter">
import Tab from "@/components/Tab/index.vue";
import Breadcrumb from "@/components/Breadcrumb/index.vue";
import Search from "@/components/Search/index.vue";
import DropdownMenu from "@/components/DropdownMenu/index.vue";
import Card from "@/components/Card/index.vue";
import FilterButtonGroup from "@/components/FilterButtonGroup/index.vue";
import { eventAuditList } from "@/api/auditCenter";
import { isArray } from "@/utils/validate";

const router = useRouter();
const breadcrumbList = ["首页", "审核中心"];

const stateList = [
  { title: "未审核", id: 0 },
  { title: "审核通过", id: 1 },
  { title: "审核不通过", id: 2 }
];
function handleStateChange(index) {
  nextTick(getAuditList());
}

// 列表
const list = ref([]);

function handleCardClick(item) {
  router.push({
    path: "/eventDetails",
    query: {
      id: item.id,
      bt: 1,
      fr: 2
    }
  });
}

const listQuery = reactive({
  appealSource: null,
  examineStatus: 0,
  name: "",
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
    getAuditList();
  }
);

const getAuditList = async () => {
  const res = await eventAuditList({
    ...listQuery
  });
  if (isArray(res.result.events)) {
    list.value = res.result.events;
  } else {
    list.value = [];
  }
};

onMounted(() => {
  getAuditList();
});
// 搜索在点击键盘上的搜索/回车按钮后触发
const onSearch = val => {
  getAuditList();
};
</script>
<style lang="less" scoped>
@import url("./index.less");
</style>
