<template>
  <div class="event-detail-card-wrapper pt-[14px] px-[16px] pb-[12px]">
    <div class="event-detail-card-top">
      <div class="flex mb-[10px] items-center">
        <status-card
          :status="temp.order_status_name"
          class="mr-[8px]"
        ></status-card>
        <span class="title flex-1 text-ellipsis">{{ temp.title }}</span>
        <source-card
          :lable="temp.normalTypeCode"
          class="ml-[8px]"
        ></source-card>
      </div>
      <div class="flex mb-[12px]">
        <span class="label">发生地：</span>
        <span class="value flex-1">{{ temp.happenAddress }}</span>
      </div>
      <div class="flex">
        <span class="label">发生时间：</span>
        <span class="value flex-1">{{ temp.reportTime }}</span>
      </div>
    </div>
    <!-- 批示和关注按钮 -->
    <div class="flex justify-between mt-[16px]">
      <div
        class="attention-btn flex justify-center items-center"
        @click="handleMemorandumClick"
      >
        <span class="push-button-icon mr-[4px]"></span>
        <span>{{ commentListLength <= 0 ? "" : "增加" }}批示</span>
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
  </div>
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
import StatusCard from "@/views/keyEvents/components/StatusCard/index.vue";
import SourceCard from "@/views/keyEvents/components/SourceCard/index.vue";
const router = useRouter();
const { temp } = defineProps({
  temp: {
    type: Object,
    default: () => {
      return {};
    }
  },
  commentListLength: {
    type: Number,
    default: 0
  }
});

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
const emit = defineEmits(["memorandumClick", "attentionStatusChange"]);
// 点击批示
const handleMemorandumClick = () => {
  emit("memorandumClick");
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
