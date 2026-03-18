<script setup name="KeyEvents">
import emptyPng from "@/assets/images/empty.png";
import { scrollLeftTo } from "vant/es/tabs/utils.mjs";
import Menu from "./components/Menu/index.vue";
import { parseTime, isArray } from "@/utils/validate";
import Card from "./components/Card/index.vue";
import { dynamicEventList, eventClassNum } from "@/api/keyEvents";
import { keyEventDetailStoreHook } from "@/store/modules/keyEventDetail";
const breadcrumbList = ["首页", "事件列表"];

const getDefaultSearchDates = () => {
  const today = new Date();
  const oneWeekAgo = new Date(today);

  // 设置一周前的日期
  oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

  // 将日期转换为字符串格式，可以根据需要调整格式
  const startDate = oneWeekAgo.toISOString().split("T")[0];
  const endDate = today.toISOString().split("T")[0];

  return { startDate, endDate };
};
const { startDate, endDate } = getDefaultSearchDates();
const queryTemp = reactive({
  pageNo: 1,
  pageSize: 10,
  keyword: "", // 关键词
  normalTypeCode: "", // 事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求
  streetCode: "", // 街道code
  streetName: "", // 街道名称
  communityCode: "", // 社区编号
  gridCode: "", // 网格编号
  orderStatusName: "", // 工单状态
  reportTimeStart: startDate, // 上报开始时间
  reportTimeEnd: endDate, // 上报结束时间
  dataSource: "", // 来源
  eventClassDefine: "",
  typeName: ""
});

const onSearch = val => {
  console.log("搜索", val);
  onRefresh();
  loading.value = true;
  finished.value = false;
  getList();
};

const route = useRoute();
// switch (route.query.fr) {
//   case "0":
//     queryTemp.normalTypeCode = "维稳事件";
//     break;
//   case "1":
//     queryTemp.normalTypeCode = "重点事件";
//     break;
//   default:
//     break;
// }

const labelList = ref([
  {
    label: "维稳事件",
    value: "维稳事件",
    num: "0",
    id: 1
  },
  {
    label: "重点事件",
    value: "重点事件",
    num: "0",
    id: 2
  },
  {
    label: "矛盾纠纷",
    value: "社会风险防范",
    num: "0",
    id: 3
  },
  {
    label: "网格事件",
    value: "网格上报",
    num: "0",
    id: 4
  },
  {
    label: "民生诉求",
    value: "民意速办",
    num: "0",
    id: 5
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
  queryTemp.normalTypeCode = label.value;
  queryTemp.orderStatusName = "";
  queryTemp.typeName = "";
  queryTemp.eventClassDefine = "";
  queryTemp.dataSource = "";
  onRefresh();
  const title = labelItemRefs.value[index];
  const to =
    title.offsetLeft - (navRef.value.offsetWidth - title.offsetWidth) / 2;
  scrollLeftTo(navRef.value, to, 0.3);
  nextTick().then(() => {
    loading.value = true;
    getList();
  });
};

// *******************   列表   ***************************
const list = ref([]);
const router = useRouter();
const error = ref(false);
const loading = ref(false);
const finished = ref(true);
const handleCardClick = item => {
  keyEventDetailStoreHook().setEvent(item);
  router.push({
    path: "/keyEventsDetail",
    query: {
      code: item.eventCode
    }
  });
};
const onRefresh = () => {
  queryTemp.pageNo = 1;
  list.value = [];
  error.value = false;
};
const getList = async () => {
  if (
    queryTemp.normalTypeCode === "维稳事件" ||
    queryTemp.normalTypeCode === "重点事件"
  ) {
    loading.value = false;
    error.value = false;
    finished.value = true;

    return;
  }

  dynamicEventList({
    ...queryTemp,
    streetCode: queryTemp.streetName === "南山区" ? "" : queryTemp.streetCode,
    streetName: queryTemp.streetName === "南山区" ? "" : queryTemp.streetName
  })
    .then(res => {
      loading.value = false;
      error.value = false;

      if (res.result && isArray(res.result.dynamicEvents)) {
        if (queryTemp.pageNo > 1) {
          list.value = [...list.value, ...res.result.dynamicEvents];
        } else {
          list.value = res.result.dynamicEvents;
        }
        if (res.result.dynamicEvents.length < queryTemp.pageSize) {
          finished.value = true;
          console.log(
            "全部加载完毕",
            res.result.dynamicEvents.length,
            queryTemp.pageSize
          );
        } else {
          finished.value = false;
        }
      } else {
        if (queryTemp.pageNo <= 1) {
          list.value = [];
        }
        finished.value = true;
      }
    })
    .catch(() => {
      error.value = true;
      nextTick().then(() => {
        console.log(988);
        loading.value = false;
      });
    });
};
const onLoad = () => {
  queryTemp.pageNo++;
  getList();
};
onMounted(() => {
  switch (route.query.fr) {
    case "0":
      queryTemp.normalTypeCode = "维稳事件";
      break;
    case "1":
      queryTemp.normalTypeCode = "重点事件";
      break;
    default:
      queryTemp.normalTypeCode = "民意速办";

      break;
  }
});
const handleTimeChange = () => {
  onRefresh();
  loading.value = true;
  finished.value = false;
  nextTick().then(() => {
    getList();
  });
};
const handleMenuChange = () => {
  onRefresh();
  loading.value = true;
  finished.value = false;
  getList();
};

// 使用路由守卫来监听每次路由变化
router.beforeEach((to, from, next) => {
  if ("/keyEventsDetail" === from.path) {
    console.log("详情页返回");
  } else if (from.path === "/home") {
    console.log("从首页进入");
    queryTemp.reportTimeStart = startDate; // 上报开始时间
    queryTemp.reportTimeEnd = endDate; // 上报结束时间
    init(to.query.fr);
  }
  next();
});
const init = fr => {
  console.log("init");
  switch (fr) {
    case "0":
      handleLabelClick(
        {
          label: "维稳事件",
          value: "维稳事件",
          num: "0",
          id: 1
        },
        0
      );
      break;
    case "1":
      handleLabelClick(
        {
          label: "重点事件",
          value: "重点事件",
          num: "0",
          id: 2
        },
        0
      );
      break;
    default:
      handleLabelClick(
        {
          label: "民生诉求",
          value: "民意速办",
          num: "0",
          id: 5
        },
        0
      );
      break;
  }
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
          v-model="queryTemp.keyword"
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
              queryTemp.normalTypeCode === labelItem.value
                ? 'text-[#1699AD] bg-[#1699AD]/[.1] text-[15px]'
                : 'text-[#767676] bg-white text-[14px]'
            "
            @click="handleLabelClick(labelItem, labelIndex)"
            :aria-selected="queryTemp.normalTypeCode === labelItem.value"
          >
            <span>{{ labelItem.label }}</span>
          </div>
        </div>
      </div>
      <!-- 下拉菜单 -->
      <div class="mt-[12px]">
        <Menu :listQuery="queryTemp" @change="handleMenuChange"></Menu>
      </div>
    </div>

    <!-- 列表 -->
    <div class="list-wrapper p-[12px]">
      <div class="list-container bg-white rounded-[8px] px-[14px] py-[12px]">
        <!-- 时间筛选 -->
        <date-picker
          v-model:startDate="queryTemp.reportTimeStart"
          v-model:endDate="queryTemp.reportTimeEnd"
          @change="handleTimeChange"
        ></date-picker>
        <!-- 列表 -->
        <van-list
          :immediate-check="false"
          v-model:loading="loading"
          v-model:error="error"
          :finished="finished"
          error-text="请求失败，点击重新加载"
          @load="onLoad"
          :finished-text="queryTemp.pageNo > 1 ? '没有更多了' : ''"
        >
          <template
            v-if="
              list.length <= 0 && !error && !loading && queryTemp.pageNo <= 1
            "
          >
            <van-empty
              :image-size="[300, 180]"
              :image="emptyPng"
              description="暂无数据"
              class="mt-[40px]"
            />
          </template>
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
