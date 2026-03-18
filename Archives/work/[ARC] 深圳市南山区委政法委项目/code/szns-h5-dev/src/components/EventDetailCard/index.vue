<template>
  <div class="event-detail-card-wrapper pt-[14px] px-[16px] pb-[12px]">
    <div class="event-detail-card-top">
      <div class="flex mb-[10px] items-center">
        <status-card :status="1" class="mr-[8px]"></status-card>
        <span class="title flex-1 text-ellipsis">{{ temp.name }}</span>
        <source-card :lable="temp.systemSource" class="ml-[8px]"></source-card>
      </div>

      <template v-if="temp.dataSource === '2'">
        <div class="flex mb-[12px]">
          <span class="label">发生地：</span>
          <span class="value flex-1">{{ temp.takeAddress }}</span>
        </div>
        <div class="flex">
          <span class="label">发生时间：</span>
          <span class="value flex-1">{{ temp.appearTime }}</span>
        </div>
      </template>
      <template v-else>
        <div class="flex mb-[12px]">
          <span class="label">上报单位：</span>
          <span class="value flex-1">{{ temp.unitSource }}</span>
        </div>
        <div class="flex mb-[12px]">
          <span class="label">上报人员：</span>
          <span class="value flex-1">{{ temp.senderName }}</span>
        </div>
        <div class="divider mb-[12px]"></div>
        <div class="flex mb-[12px]">
          <span class="label">推送领导：</span>
          <span class="value flex-1">{{ temp.decisionPersonName }}</span>
        </div>
        <div class="flex mb-[12px]">
          <span class="label">抄送领导：</span>
          <span class="value flex-1">{{ temp.readPersonName }}</span>
        </div>
        <div class="flex">
          <span class="label">上报时间：</span>
          <span class="value flex-1">{{ temp.sendTime }}</span>
        </div>
      </template>
    </div>
    <!--  -->
    <template v-if="type === '1'">
      <!-- 定时推送 -->
      <div class="push-container flex justify-between mt-[12px]">
        <span>定时推送</span>
        <van-switch v-model="checked" />
      </div>
      <!-- 推送事件 -->
      <template v-if="checked">
        <div
          class="push-time-container flex flex-col mt-[13px]"
          @click="showPicker = true"
        >
          <div class="flex justify-between">
            <span>推送时间</span>
            <van-icon name="arrow-down" color="rgba(25,31,37,0.28)" />
          </div>
          <div class="timeValue mt-[3px]" :class="showData ? 'time' : ''">
            {{ showData || "请选择" }}
          </div>
        </div>
      </template>
      <!-- 推送按钮 -->
      <div class="flex justify-between mt-[16px]">
        <div
          class="btn primary flex justify-center items-center"
          @click="handlePushClick(1)"
        >
          <span class="push-button-icon mr-[4px]"></span>
          <span>立即推送</span>
        </div>
        <div
          class="btn not-push-button flex justify-center items-center"
          @click="handlePushClick(2)"
        >
          <span class="no-push-button-icon mr-[4px]"></span>
          <span>审核不通过</span>
        </div>
      </div>
    </template>
    <template v-else-if="type === '2' && showMenu">
      <!-- 批示和关注按钮 -->
      <div class="flex justify-between mt-[16px]">
        <div
          class="attention-btn flex justify-center items-center"
          @click="handleMemorandumClick"
        >
          <span class="push-button-icon mr-[4px]"></span>
          <span
            >{{ commentListLength <= 0 ? "" : "增加"
            }}{{ route.query.type === "2" ? "反馈" : "批示" }}</span
          >
        </div>
        <!-- cancel 取消关注样式 -->
        <div
          class="attention-btn flex justify-center items-center"
          @click="handleAttentionBtnClick"
        >
          <span
            class="mr-[4px]"
            :class="
              !temp.attentionStatus || temp.attentionStatus === '0'
                ? 'push-button-icon'
                : 'no-push-button-icon'
            "
          ></span>
          <span>{{
            !temp.attentionStatus || temp.attentionStatus === "0"
              ? "关注"
              : "取消关注"
          }}</span>
        </div>
      </div>
    </template>
  </div>

  <!-- 选择日期 -->
  <van-popup v-model:show="showPicker" position="bottom">
    <van-picker-group
      title=""
      :tabs="['选择日期', '选择时间']"
      next-step-text="下一步"
      @confirm="onConfirm"
      @cancel="showPicker = false"
    >
      <van-date-picker v-model="currentDate" :min-date="minDate" />
      <van-time-picker v-model="currentTime" />
    </van-picker-group>
  </van-popup>

  <!-- 关注成功与否弹框 -->
  <van-overlay
    :custom-style="{ background: 'rgba(0,0,0,0.32)' }"
    :show="showNotify"
  />
  <van-notify
    v-model:show="showNotify"
    background="transparent"
    type="primary"
    position="bottom"
    class="pb-[15px]"
  >
    <div class="w-full pb-[15px] px-[12px]">
      <div
        class="event-detail-card-notify-container w-full flex justify-between items-center px-[14px]"
      >
        <div>{{ showText }}</div>
        <div v-if="showText === '关注成功'" @click="handleSeeClick">去看看</div>
      </div>
    </div>
  </van-notify>
</template>

<script setup>
import StatusCard from "../StatusCard/index.vue";
import SourceCard from "../SourceCard/index.vue";
import { useUserStoreHook } from "@/store/modules/user";
const checked = ref(false);
const minDate = new Date();
const router = useRouter();
const route = useRoute();
const { type, temp, fr } = defineProps({
  type: {
    type: String,
    default: "" // 1 审核中心进入  2 今日关注进入
  },
  temp: {
    type: Object,
    default: () => {
      return {};
    }
  },
  fr: {
    type: String,
    default: ""
  },
  commentListLength: {
    type: Number,
    default: 0
  }
});

// 首页是否显示审核中心
const showMenu = computed(() => {
  const { roleList } = useUserStoreHook().userInfo;
  const isAuditor1 = roleList.some(role => role.roleName.includes("审核员"));

  if (isAuditor1) {
    return fr !== "1" && fr !== "6";
  }

  return true;
});

// 点击选择时间
const showPicker = ref(false);

// 创建一个新的Date对象，表示当前日期
const today = new Date();

const showData = ref("");

// 使用setDate()方法将日期设置为当前日期之后的一天
today.setDate(today.getDate() + 1);
console.log(today);
const currentDate = ref([
  today.getFullYear(),
  today.getMonth() + 1,
  today.getDate()
]);

const currentTime = ref(["9", "00"]);
const onConfirm = () => {
  console.log(`${currentDate.value.join("-")} ${currentTime.value.join(":")}`);
  showData.value = `${currentDate.value.join("-")} ${currentTime.value.join(":")}:00`;
  showPicker.value = false;
};

// 关注和取消关注点击
const showNotify = ref(false);
const showText = ref("关注成功");
defineExpose({
  showNotify,
  showText
});
const handleAttentionBtnClick = async () => {
  emit("attentionStatusChange");
};

// 点击去看看
const handleSeeClick = () => {
  router.push("/attention?form=2");
};
const emit = defineEmits([
  "memorandumClick",
  "pushClick",
  "attentionStatusChange"
]);
// 点击批示
const handleMemorandumClick = () => {
  emit("memorandumClick");
};

const pushTime = computed(
  () => `${currentDate.value.join("-")} ${currentTime.value.join(":")}:00`
);
// 点击立即推送
const handlePushClick = examineStatus => {
  emit("pushClick", {
    examineStatus,
    checked: checked.value,
    pushTime: pushTime.value
  });
};
</script>
<style lang="less">
:root:root {
  --van-switch-width: 42px;
  --van-switch-height: 22px;
  --van-switch-background: #cacaca;
  --van-switch-node-size: 18px;
}
.event-detail-card-notify-container {
  height: 44px;
  background: #1699ad;
  border-radius: 10px;
  font-weight: 400;
  font-size: 14px;
  color: #ffffff;
  line-height: 44px;
  text-align: left;
  font-style: normal;
}
</style>
<style lang="less" scoped>
.event-detail-card-wrapper {
  background: #fff;
  border-radius: 8px;
  .event-detail-card-top {
    font-weight: 400;
    font-size: 14px;
    color: #2b2b2b;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    .title {
      font-weight: 600;
      font-size: 14px;
      color: #102f2e;
      line-height: 20px;
      text-align: left;
      font-style: normal;
    }
    .label {
      font-weight: 400;
      font-size: 14px;
      color: #818181;
      line-height: 20px;
      text-align: left;
      font-style: normal;
    }
    .value {
      display: block;
      max-width: 220px;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .divider {
      height: 1px;
      border: 1px solid #e6e6e6;
    }
  }
  .push-container {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
  }
  .push-time-container {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    .timeValue {
      font-size: 10px;
      color: #505050;
      line-height: 14px;
      &.time {
        font-size: 14px;
        color: #505050;
        line-height: 20px;
      }
    }
  }
  .btn {
    width: 153px;
    height: 32px;
    background: rgba(213, 213, 213, 0.29);
    border-radius: 8px;
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 32px;
    text-align: center;
    font-style: normal;
    &.primary {
      background: rgba(22, 153, 173, 0.13);
      color: #1699ad;
    }
    .push-button-icon {
      display: inline-block;
      width: 17px;
      height: 17px;
      background: url("@/assets/images/push-button-icon.png") 0px 0px no-repeat;
      background-size: 100%;
    }
    .no-push-button-icon {
      display: inline-block;
      width: 17px;
      height: 17px;
      background: url("@/assets/images/not-push-button.png") 0px 0px no-repeat;
      background-size: 100%;
    }
  }
  .pushTime {
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
  }

  .attention-btn {
    width: 153px;
    height: 32px;
    background: #1699ad;
    border-radius: 8px;
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    line-height: 32px;
    text-align: center;
    font-style: normal;
    &.cancel {
      background: rgba(22, 153, 173, 0.1);
      color: #1699ad;
    }
  }
}
</style>
