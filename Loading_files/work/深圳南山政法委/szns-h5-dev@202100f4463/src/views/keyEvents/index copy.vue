<script setup>
import { scrollLeftTo } from "vant/es/tabs/utils.mjs";
import Menu from "./components/Menu/index.vue";
import { parseTime, isArray } from "@/utils/validate";
import Card from "@/components/Card/index.vue";
import { eventList, eventClassNum } from "@/api/keyEvents";
const breadcrumbList = ["首页", "事件列表"];
const queryTemp = reactive({
  appealSource: null, // 事件来源
  areaDistribution: null, // 区域分布
  beginDate: parseTime(new Date(), "{y}-{m}-{d}"), // 发生时间
  dangerLevel: null, // 风险等级
  emergencyLable: null, // 紧急标签：一般件、紧急件、督办件
  endDate: parseTime(new Date(), "{y}-{m}-{d}"), // 发生时间
  eventClass: null, // 事件分类：纠纷化解、投诉建议、心理服务、法律服务、帮扶救助
  eventSourceClass: null, // 事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求
  eventType: null, // 事件类型：一般事件、即采即办
  handleStatus: null, // 处理状态 0未处理 1处理中 2已处理 默认0
  incidentArea: null, // 区域
  name: null,
  organizer: null, // 主办单位
  relatedField: null //涉及领域
});

const onSearch = val => {
  console.log("搜索", val);
  getList();
};
const route = useRoute();
switch (route.query.fr) {
  case "0":
    queryTemp.eventSourceClass = "维稳事件";
    break;
  case "1":
    queryTemp.eventSourceClass = "重点事件";
    break;
  default:
    break;
}

const labelList = ref([
  {
    eventSourceClass: "维稳事件",
    num: "0"
  },
  {
    eventSourceClass: "重点事件",
    num: "0"
  },
  {
    eventSourceClass: "矛盾纠纷",
    num: "0"
  },
  {
    eventSourceClass: "网格事件",
    num: "0"
  },
  {
    eventSourceClass: "民生诉求",
    num: "0"
  }
]);
const labelItemRefs = ref({});
// 设置引用的函数
const setLabelItemRef = id => {
  return el => {
    if (el) {
      labelItemRefs.value[id] = el;
    }
  };
};
const navRef = ref(null);
const handleLabelClick = (label, index) => {
  queryTemp.eventSourceClass = label.eventSourceClass;
  queryTemp.appealSource = null;
  queryTemp.areaDistribution = null;
  queryTemp.dangerLevel = null;
  queryTemp.emergencyLable = null;
  queryTemp.eventClass = null;
  queryTemp.eventType = null;
  queryTemp.organizer = null;
  queryTemp.relatedField = null;
  // labelItemRefs.value[index].scrollIntoView({
  //   // 滚动到指定节点
  //   block: "start", // 值有start,center,end，nearest，当前显示在视图区域中间
  //   behavior: "auto" // 值有auto、instant,smooth，缓动动画（当前是慢速的）
  // });
  const title = labelItemRefs.value[index];
  const to =
    title.offsetLeft - (navRef.value.offsetWidth - title.offsetWidth) / 2;
  scrollLeftTo(navRef.value, to, 0.3);
  nextTick().then(() => {
    getList();
  });
};

// *******************   列表   ***************************
const list = ref([]);
const router = useRouter();
const handleCardClick = item => {
  router.push({
    path: "/eventDetails",
    query: {
      id: item.id,
      bt: 2,
      fr: 1
    }
  });
};
const getList = async () => {
  const res = await eventList({
    ...queryTemp
  });
  if (isArray(res.result.events)) {
    list.value = res.result.events;
  } else {
    list.value = [];
  }
};
onMounted(() => {
  eventClassNum({}).then(res => {
    if (isArray(res.result.eventClassNums)) {
      labelList.value = [...res.result.eventClassNums];
      if (!route.query.fr) {
        nextTick().then(() => {
          let flag = true;
          for (let i = 0; i <= res.result.eventClassNums.length - 1; i++) {
            const v = res.result.eventClassNums[i];
            console.log(v);
            if (v.num && v.num > 0) {
              handleLabelClick(v, i);
              flag = false;
              break;
            }
          }
          if (flag) {
            handleLabelClick(res.result.eventClassNums[0], 0);
          }
        });
      } else {
        getList();
      }
    }
  });
});
const handleTimeChange = () => {
  nextTick().then(() => {
    getList();
  });
};
</script>

<template>
  <div>
    <div class="p-[12px]">
      <Breadcrumb :list="breadcrumbList"></Breadcrumb>
    </div>
    <div class="query-wrapper bg-white py-[16px]">
      <!-- 搜索 -->
      <div class="px-[16px]">
        <Search
          v-model="queryTemp.name"
          placeholder="输入事件关键字搜索"
          shape="round"
          @search="onSearch"
        />
      </div>

      <!-- 分类 -->
      <div class="relative h-[32px] mt-[12px]">
        <div
          ref="navRef"
          class="tabs px-[12px] overflow-x-auto overflow-y-hidden h-full flex"
        >
          <div
            v-for="(labelItem, labelIndex) in labelList"
            :ref="setLabelItemRef(labelIndex)"
            :key="labelIndex"
            class="px-[10px] py-[6px] rounded-[2px] text-center font-normal leading-[20px] text-nowrap whitespace-nowrap"
            :class="
              queryTemp.eventSourceClass === labelItem.eventSourceClass
                ? 'text-[#1699AD] bg-[#1699AD]/[.1] text-[15px]'
                : 'text-[#767676] bg-white text-[14px]'
            "
            @click="handleLabelClick(labelItem, labelIndex)"
            :aria-selected="
              queryTemp.eventSourceClass === labelItem.eventSourceClass
            "
          >
            <span>{{ labelItem.eventSourceClass }}({{ labelItem.num }})</span>
          </div>
        </div>
      </div>
      <!-- 下拉菜单 -->
      <div class="mt-[12px]">
        <Menu :listQuery="queryTemp" @change="getList"></Menu>
      </div>
    </div>

    <!-- 列表 -->
    <div class="list-wrapper p-[12px]">
      <div class="list-container bg-white rounded-[8px] px-[14px] py-[12px]">
        <!-- 时间筛选 -->
        <date-picker
          v-model:startDate="queryTemp.beginDate"
          v-model:endDate="queryTemp.endDate"
          @change="handleTimeChange"
        ></date-picker>
        <!-- 列表 -->
        <van-list finished-text="没有更多了">
          <card
            v-for="(item, index) in list"
            :key="index"
            :item="item"
            class="mt-[12px]"
            @click="handleCardClick(item)"
          ></card>
        </van-list>
      </div>
    </div>
  </div>
</template>

<style scoped>
.tabs::-webkit-scrollbar {
  display: none;
}
</style>
