<script setup>
import { keyEventDetailStoreHook } from "@/store/modules/keyEventDetail";
import {
  attentionOrCancle,
  saveDynamicEventComment,
  commentListAttention
} from "@/api/keyEvents";
import EventDetailCard from "./components/EventDetailCard/index.vue";
import FilterButtonGroup from "@/components/FilterButtonGroup/index.vue";
import EventDetailsGroup from "@/components/EventDetailsGroup/index.vue";
import InstructionsDetails from "@/components/InstructionsDetails/index.vue";
import { isEmpty, isArray } from "@/utils/validate";
const breadcrumbList = ref(["首页", "事件列表", "事件详情"]);
const temp = ref({});
const btnGroupState = ref(2);
const btnGroupList = ref([
  { title: "批示详情", id: 1, show: false },
  { title: "事件详情", id: 2, show: true }
]);
const state = ref(1);
// const stateList = ref([{ title: "事件详情", id: 1 }]);

const detailList = [
  {
    lable: "事件编号",
    key: "eventCode",
    type: 1
  },
  {
    lable: "事件类型",
    key: "normalTypeCode",
    type: 1
  },
  {
    lable: "事件时间",
    key: "reportTime",
    type: 1
  },
  {
    lable: "事件地址",
    key: "happenAddress",
    type: 1
  },
  {
    lable: "事件/诉求内容",
    key: "eventDesc",
    type: 2
  }
];

const eventDetailCard = ref(null);
// 点击关注之后
const handleattentionStatusChange = status => {
  attentionOrCancle({
    eventCode: temp.value.eventCode,
    dynamicEvent: { ...temp.value },
    attentionStatus:
      !temp.value.attentionStatus || temp.value.attentionStatus == "0"
        ? "1"
        : "0"
  })
    .then(res => {
      eventDetailCard.value.showText =
        !temp.value.attentionStatus || temp.value.attentionStatus == "0"
          ? "关注成功"
          : "已取消关注";
      temp.value.attentionStatus =
        !temp.value.attentionStatus || temp.value.attentionStatus == "0"
          ? "1"
          : "0";
      eventDetailCard.value.showNotify = true;
      setTimeout(() => {
        eventDetailCard.value.showNotify = false;
      }, 1000);
    })
    .catch(() => {});
};

// 批示按钮点击
const showMemorandumDetail = ref(false);
const memorandumDetailTitle = "批示建议";
const memorandumDetailPlaceholder = "请输入您的批示建议";
const commonInstructionsList = [
  "请尽快处理",
  "重点跟进处理进度",
  "事态严谨需要加强管理"
];
const handleMemorandumClick = () => {
  showMemorandumDetail.value = true;
};

const message = ref("");

// 点击常用回复
const handleCommonInstructionsItemClick = value => {
  message.value = value;
};
// 反馈操作
const handleSaveEventComment = () => {
  if (isEmpty(message.value)) {
    showFailToast("请输入建议");
    return;
  }
  // if (isEmpty(fieldCode.value)) {
  //   showFailToast("请选择接收单位");
  //   return;
  // }
  showLoadingToast({
    message: "提交中...",
    forbidClick: true
  });
  saveDynamicEventComment({
    eventCode: temp.value.eventCode,
    dynamicEvent: { ...temp.value },
    comment: message.value,
    commentOrFeedBack: 1
  }).then(() => {
    showSuccessToast("提交成功");
    showMemorandumDetail.value = false;
    init();
  });
};

onMounted(() => {
  temp.value = {
    ...keyEventDetailStoreHook().event,
    attentionStatus: "0",
    eventCommentList: []
  };
  init();
});
const init = () => {
  commentListAttention({
    ...temp.value
  }).then(res => {
    if (isEmpty(res.result)) {
      temp.value.attentionStatus = "0";
      temp.value.eventCommentList = [];
    } else {
      console.log(res.result.attentionStatus, 999);
      temp.value.attentionStatus = res.result.attentionStatus || "0";

      if (isArray(res.result.eventCommentList)) {
        temp.value.eventCommentList = [...res.result.eventCommentList];
        if (res.result.eventCommentList.length > 0) {
          btnGroupList.value[0].show = true;
          nextTick(() => {
            btnGroupState.value = 1;
          });
        }
      } else {
        temp.value.eventCommentList = [];
      }
    }
  });
};
</script>

<template>
  <div class="p-[12px]">
    <Breadcrumb :list="breadcrumbList"></Breadcrumb>
  </div>
  <div class="event-details-wrapper flex flex-col p-[12px] pt-0">
    <!-- 事件卡片 -->
    <event-detail-card
      ref="eventDetailCard"
      :temp="temp"
      @memorandumClick="handleMemorandumClick"
      @attentionStatusChange="handleattentionStatusChange"
    ></event-detail-card>
    <!--   ************** 点击批示和反馈之前  ************ -->
    <!-- 事件详情 -->
    <div v-show="!showMemorandumDetail">
      <div class="event-details mt-[12px] px-[12px] py-[16px] min-h-max">
        <!-- 大类型按钮组 -->
        <filter-button-group
          bold
          v-model:state="btnGroupState"
          :state-list="btnGroupList.filter(v => v.show)"
        ></filter-button-group>
        <instructions-details
          v-show="btnGroupState === 1 && temp.eventCommentList.length > 0"
          class="mt-[16px]"
          :commentList="temp.eventCommentList"
        ></instructions-details>
        <div v-show="btnGroupState === 2">
          <!-- 按钮组 -->
          <!--  <filter-button-group
            border
            v-model:state="state"
            :state-list="stateList"
            class="mt-[12px]"
          ></filter-button-group> -->
          <event-details-group
            :temp="temp"
            :list="detailList"
            class="mt-[10px]"
          ></event-details-group>
        </div>
      </div>
    </div>
    <!--   ************** 批示按钮点击之后  ************ -->

    <template v-if="showMemorandumDetail">
      <div
        class="event-details memorandum-detail mt-[12px] px-[12px] pt-[8px] pb-[55px]"
      >
        <div class="title">{{ memorandumDetailTitle }}</div>
        <van-field
          v-model="message"
          rows="4"
          autosize
          type="textarea"
          :placeholder="memorandumDetailPlaceholder"
          show-word-limit
          class="mt-[17px]"
        />
        <!-- 常用批示 -->
        <div class="common-instructions flex mt-[16px]">
          <div class="label mr-[16px]">常用批示：</div>
          <div class="flex-1 flex flex-wrap justify-between">
            <div
              v-for="(item, index) in commonInstructionsList"
              :key="index"
              class="bg-[#42B9CB]/[.1] rounded-[4px] px-[10px] py-[2px] mb-[16px]"
              @click="handleCommonInstructionsItemClick(item)"
            >
              {{ item }}
            </div>
          </div>
        </div>
        <!-- 确认取消按钮 -->
        <div class="mt-[30px] flex justify-between">
          <div
            class="w-[153px] h-[32px] bg-[#D5D5D5]/[.29] rounded-[8px] text-center leading-[32px] text-[#818181]"
            @click="showMemorandumDetail = false"
          >
            取消
          </div>
          <div
            class="w-[153px] h-[32px] bg-[#42B9CB] rounded-[8px] text-center leading-[32px] text-white"
            @click="handleSaveEventComment"
          >
            确定
          </div>
        </div>
      </div>
    </template>
    <!--   ************** END  ************ -->
  </div>
</template>
<style lang="less">
.event-details-wrapper {
  .van-field {
    background: #f6f9fa;
  }
}
</style>
<style lang="less" scoped>
@import url("./index.less");
</style>
