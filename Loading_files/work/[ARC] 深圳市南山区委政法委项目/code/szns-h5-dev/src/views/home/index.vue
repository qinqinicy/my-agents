<script setup name="Home">
import hImage2 from "@/assets/images/home_2.png";
import hImage3 from "@/assets/images/home_3.png";
import hImage4 from "@/assets/images/home_4.png";
import Tab from "@/components/Tab/index.vue";
import {
  attentionSum,
  todayEventAttention,
  userNum,
  hasNewEvent,
  keyUser,
  getEventTotal
} from "@/api/home";
import { isArray, isEmpty, parseTime } from "@/utils/validate";
import AreaPicker from "@/components/AreaPicker/index.vue";
import FocusOnChecking from "@/components/FocusOnChecking/index.vue";
import { monitorskey } from "@/api/monitors";
import { useUserStoreHook } from "@/store/modules/user";
const router = useRouter();
const loopData0 = ref([
  {
    lanhutext0: "领导批示",
    lanhutext1: "0",
    lanhuimage0: hImage2
  },
  {
    lanhutext0: "我的批示",
    lanhutext1: "0",
    lanhuimage0: hImage3
  },
  {
    lanhutext0: "我的关注",
    lanhutext1: "0",
    lanhuimage0: hImage4
  }
]);

// 关注概览
const getAttentionSum = async () => {
  const res = await attentionSum({});
  loopData0.value[0].lanhutext1 = isEmpty(res.result.leaderNum)
    ? 0
    : res.result.leaderNum;
  loopData0.value[1].lanhutext1 = isEmpty(res.result.myCommentNum)
    ? 0
    : res.result.myCommentNum;
  loopData0.value[2].lanhutext1 = isEmpty(res.result.myAttentionNum)
    ? 0
    : res.result.myAttentionNum;
};
// 今日关注列表
let loopData1 = ref([]);

// 今日关注列表
const handleSuccessReq = async () => {
  const res = await todayEventAttention({ today: "5" });
  if (isArray(res.result.events)) {
    loopData1.value = [...res.result.events];
  } else {
    loopData1.value = [];
  }
  // showSuccessToast("请求成功");
  // showList.push(...list);
};

// 人口数字统计
const userNumObject = reactive({
  change: 0,
  todayCaiHe: 0,
  total: 0
});
const getUserNum = async () => {
  const res = await userNum({
    departmentCode: showAreaTemp.value.departmentCode
  });
  updateTime.value = res.result.date;
  userNumObject.change = res.result.change || 0;
  userNumObject.todayCaiHe = res.result.todayCaiHe || 0;
  userNumObject.total = res.result.total || 0;
};
const init = () => {
  handleSuccessReq();
  getAttentionSum();
  getMonitorskey();
  if (!isEmpty(showAreaTemp.value.departmentCode)) {
    getUserNum();
    getEventTodayCal();
    getKeyUser();
  }
};

onActivated(() => {
  init();
});
//  是否显示关注概括提示红点
const showBadge = ref(false);
const getHasNewEvent = () => {
  hasNewEvent().then(res => {
    showBadge.value = res.result.haveNewEvent;
  });
};
var t = null;
onMounted(() => {
  getHasNewEvent();
  t = setInterval(() => {
    if (showMenu) {
      getHasNewEvent();
    }
  }, 30000);
});
onBeforeUnmount(() => {
  if (t) {
    clearInterval(t);
    t = null;
  }
});
// 今日关注类型
const handleLoopData1Type = type => {
  let map = {
    1: {
      bgColor: "#FFF7E7",
      textColor: "#FF7821",
      text: "维稳"
    },
    2: {
      bgColor: "#E7F7FF",
      textColor: "#218AFF",
      text: "网格"
    },
    3: {
      bgColor: "#E7FFF0",
      textColor: "#19D021",
      text: "综治"
    }
  };
  return map[2];
};

// 下拉刷新
const refreshStatus = ref(false);

const onRefresh = () => {
  setTimeout(() => {
    init();
    refreshStatus.value = false;
  }, 1000);
};

// 点击审核中心
function handleAuditCenterClick() {
  router.push("/auditCenter");
}

// 点击今日关注查看更多
const handleMoreClick = () => {
  router.push({
    path: "/todayFocus",
    query: {
      bc: 1
    }
  });
};

// 点击今日关注列表
const handleLoopData1ItemClick = item => {
  router.push({
    path: "/eventDetails",
    query: {
      id: item.id,
      bt: 2,
      fr: 1
    }
  });
};
// 点击今日关注列表
const handleLoopData0ItemClick = index => {
  router.push({
    path: "/attention",
    query: {
      form: index
    }
  });
};

// 点击人口总量
const handleTotalPopulationClick = () => {
  window.location.href =
    "https://rioweb.szns.gov.cn/pans/fxdp_online/preview/index.html?_t=1729750920208640#/screen/share/3f89de36fddae3b379b0acb34cc40312?loading=false&userId=808cc794c01b11eea21f024226f1592f";
};
// 地区下拉选择
const showPicker = ref(false);
const handleAreClick = () => {
  showPicker.value = true;
};
const onConfirm = selectedOptions => {
  showAreaTemp.value = { ...selectedOptions[0] };
  nextTick(() => {
    getUserNum();
    getEventTodayCal();
    getKeyUser();
  });
};
const showAreaTemp = ref({
  departmentName: "",
  departmentCode: ""
});

// 重点监测点击查看更多
const handleCheckMoreClick = () => {
  router.push({
    path: "/monitors",
    query: {
      bc: 1
    }
  });
};

// 重点监测数据
const fxNum = ref(0);
const monitorsList = ref([]);
const getMonitorskey = () => {
  monitorskey({
    isError: "shi"
  })
    .then(res => {
      fxNum.value = res.result.fxNum || 0;
      if (isArray(res.result.detailList)) {
        monitorsList.value = [...res.result.detailList];
      } else {
        monitorsList.value = [];
      }
    })
    .catch(() => {
      fxNum.value = 0;
      monitorsList.value = [];
    });
};

const updateTime = ref(parseTime(new Date()));

// 今日事件总量点击
const handleTotalEventClick = index => {
  router.push({
    path: "/keyEvents",
    query: {
      fr: index
    }
  });
};

// 首页是否显示审核中心
const showMenu = computed(() => {
  return useUserStoreHook().userInfo.roleList.some(role =>
    role.roleName.includes("审核员")
  );
});

// 首页事件总量统计
const getEventTodayCal = () => {
  // eventTodayCal({ departmentCode: showAreaTemp.value.departmentCode })
  //   .then(res => {
  //     todayEventObject.todayEventTotal = res.result.todayEventTotal || 0;
  //     todayEventObject.todayKeyEventTotal = res.result.todayKeyEventTotal || 0;
  //     todayEventObject.todayMaintainEventTotal =
  //       res.result.todayMaintainEventTotal || 0;
  //   })
  //   .catch(() => {
  //     todayEventObject.todayEventTotal = 0;
  //     todayEventObject.todayKeyEventTotal = 0;
  //     todayEventObject.todayMaintainEventTotal = 0;
  //   });
  getEventTotal({
    currentDay: parseTime(new Date(), "{y}-{m}-{d}"),
    streetCode:
      showAreaTemp.value.departmentName === "南山区"
        ? ""
        : showAreaTemp.value.departmentCode
  })
    .then(res => {
      todayEventObject.todayEventTotal = res.result.total;
    })
    .catch(() => {
      todayEventObject.todayEventTotal = 0;
    });
};

// 事件数字统计
const todayEventObject = reactive({
  todayEventTotal: 0,
  todayKeyEventTotal: 0,
  todayMaintainEventTotal: 0
});

// 重点人数字统计
const zdrtj = ref({
  zdzrs: 0,
  byzfrs: 0,
  zdrbyzjl: 0
});

const getKeyUser = () => {
  keyUser({ departmentCode: showAreaTemp.value.departmentCode }).then(res => {
    if (!isEmpty(res.result)) {
      zdrtj.value = { ...res.result };
    }
  });
};
</script>

<template>
  <div class="home-wrapper van-safe-area-bottom relative">
    <tab />
    <div class="home-content">
      <van-pull-refresh
        class="p-[12px]"
        v-model="refreshStatus"
        success-text="刷新成功"
        @refresh="onRefresh"
      >
        <!-- 关注概括 -->
        <div class="group_1 flex flex-col p-[12px] pb-[20px] mb-[12px]">
          <div class="block_1 flex justify-between">
            <div class="img-t_1 flex flex-row">
              <!-- <img
            class="thumbnail_1 mt-[2px]"
            referrerpolicy="no-referrer"
            src="~@/assets/images/home_1.png"
          /> -->
              <span class="text-group_1">关注概览</span>
            </div>
            <van-badge
              v-if="showMenu"
              color=" #FF6249"
              :dot="showBadge"
              :offset="[-2, 2]"
              @click="handleAuditCenterClick"
            >
              <div class="box_2" />
            </van-badge>
          </div>
          <div class="list_1 flex justify-between mt-[12px]">
            <div
              class="image-text_2 flex flex-row justify-between pt-[8px] pr-[6px] pb-[3px] pl-[9px]"
              v-for="(item, index) in loopData0"
              :key="index"
              @click="handleLoopData0ItemClick(index)"
            >
              <div class="text-group_2 flex flex-col">
                <span class="text_8" v-html="item.lanhutext0"></span>
                <span class="text_9" v-html="item.lanhutext1"></span>
              </div>
              <img
                class="thumbnail_2"
                referrerpolicy="no-referrer"
                :src="item.lanhuimage0"
              />
            </div>
          </div>
        </div>
        <!-- 今日关注 -->
        <div class="group_1 flex flex-col p-[12px] mb-[12px]">
          <div class="block_1 flex justify-between">
            <div class="img-t_1 flex flex-row content-center">
              <!-- <img
            class="thumbnail_3 mt-[2px]"
            referrerpolicy="no-referrer"
            src="~@/assets/images/home_5.png"
          /> -->
              <span class="text-group_1">今日关注</span>
            </div>
            <div class="group_2 mt-[4px]" @click="handleMoreClick">
              查看更多
            </div>
          </div>
          <template v-if="loopData1.length <= 0">
            <div
              class="empty-box mt-[13px] py-[14px] flex justify-center items-center"
            >
              <img
                class="thumbnail_5 mr-[20px]"
                referrerpolicy="no-referrer"
                src="~@/assets/images/home_8.png"
              />
              <div class="image-text_3">
                <span>暂无需要关注的事件～</span>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="list_1 mt-[4px]">
              <div
                class="box_3 flex flex-row justify-between py-[8px]"
                v-for="(item, index) in loopData1"
                :key="index"
                @click="handleLoopData1ItemClick(item)"
              >
                <div
                  class="text-group_3 flex-1 flex flex-col overflow-hidden mr-[20px]"
                >
                  <span
                    class="overflow-hidden text-nowrap whitespace-nowrap text-ellipsis"
                    >{{ item.name }}</span
                  >
                </div>
                <div
                  v-if="item.systemSource"
                  class="text-group_4"
                  :style="{
                    color: handleLoopData1Type(item.systemSource).textColor,
                    backgroundColor: handleLoopData1Type(item.systemSource)
                      .bgColor
                  }"
                >
                  {{ item.systemSource }}
                </div>
              </div>
            </div>
          </template>
        </div>
        <!-- 重点监测 -->
        <div class="group_1 flex flex-col p-[12px] mb-[12px]">
          <div class="block_1 flex justify-between">
            <div class="img-t_1 flex flex-row content-center">
              <!-- <img
            class="thumbnail_3 mt-[2px]"
            referrerpolicy="no-referrer"
            src="~@/assets/images/home_5.png"
          /> -->
              <span class="text-group_1">重点监测</span>
            </div>
            <div class="group_2 mt-[4px]" @click="handleCheckMoreClick">
              查看更多
            </div>
          </div>
          <template v-if="fxNum <= 0">
            <div
              class="empty-box py-[14px] mt-[13px] flex items-center pl-[70px]"
            >
              <img
                class="w-[30px] h-[30px] mr-[20px]"
                referrerpolicy="no-referrer"
                src="~@/assets/images/home_9.png"
              />
              <div class="image-text_3">
                <span>暂无指标存在风险</span>
              </div>
            </div>
          </template>
          <template v-else>
            <!-- 提示 -->
            <div
              class="notice-bar bg-[#FF6249]/[.09] rounded-[2px] px-[16px] py-[5px] mt-[10px] text-[#FF6249] text-[12px] leading-17px relative flex items-center"
            >
              <van-icon
                class-prefix="icon van-icon"
                color="#FF6249"
                name="bell"
              />
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
              ></focus-on-checking>
            </div>
          </template>
        </div>
      </van-pull-refresh>
    </div>
    <!-- 选择地区弹框 -->
    <AreaPicker v-model:show="showPicker" @confirm="onConfirm"></AreaPicker>
  </div>
</template>
<style lang="less" scoped>
@import url("./index.less");
</style>
