<template>
  <div class="p-[12px]">
    <Breadcrumb :list="breadcrumbList"></Breadcrumb>
  </div>
  <div class="event-details-wrapper flex flex-col p-[12px] pt-0">
    <!-- 事件卡片 -->
    <event-detail-card
      ref="eventDetailCard"
      :temp="temp.eventInfo"
      :type="route.query.bt"
      :fr="route.query.fr"
      :commentListLength="temp.eventCommentList.length"
      @memorandumClick="handleMemorandumClick"
      @pushClick="handlePushClick"
      @attentionStatusChange="handleAttentionStatusChange"
    ></event-detail-card>
    <!--   ************** 点击批示和反馈之前  ************ -->
    <!-- 事件详情 -->
    <div v-show="!showMemorandumDetail && !previewAttachments">
      <div class="event-details mt-[12px] px-[12px] py-[16px] min-h-max">
        <!-- 大类型按钮组 -->
        <filter-button-group
          bold
          v-model:state="btnGroupState"
          :state-list="btnGroupList.filter(v => v.show)"
          @change="handleStateChange"
        ></filter-button-group>
        <!--  ***************** 基础信息  ************** -->
        <div v-show="btnGroupState === 1">
          <div
            class="font-medium text-[15px] text-[#102f2e] leading-[21px] mt-[11px]"
          >
            事件诉求内容
          </div>
          <van-empty
            v-show="isEmpty(temp.eventInfo.eventSummarize)"
            :image-size="[300, 180]"
            :image="emptyPng"
            description="暂无数据"
            class="mt-[40px]"
          />
          <div
            v-show="!isEmpty(temp.eventInfo.eventSummarize)"
            class="text-[14px] text-[#818181] leading-[20px] mt-[10px]"
            style="white-space: pre-wrap"
          >
            {{
              temp.eventInfo.eventSummarize
                ? temp.eventInfo.eventSummarize.replace(/\\r\\n/g, "\r\n")
                : ""
            }}
          </div>

          <!--  ********** 关联附件  ************ -->
          <template
            v-if="
              temp.eventInfo.attachmentInfos &&
              temp.eventInfo.attachmentInfos.length > 0
            "
          >
            <div
              class="font-medium text-[15px] text-[#102f2e] leading-[21px] mt-[11px] mb-[9px]"
            >
              关联附件
            </div>
            <attachment-group
              v-for="attachmentItem in temp.eventInfo.attachmentInfos"
              class="mb-[12px]"
              :key="attachmentItem.id"
              @click="handleAttachmentClick(attachmentItem)"
              :temp="attachmentItem"
            ></attachment-group>
          </template>
        </div>
        <!--  ******************  END  ***************** -->

        <!--  ********** 如果有批示详情的话  ************ -->
        <instructions-details
          v-show="btnGroupState === 2"
          class="mt-[16px]"
          :commentList="temp.eventCommentList"
        ></instructions-details>
        <!--  ******************  END  ***************** -->

        <div v-show="btnGroupState === 3">
          <!-- 按钮组 -->
          <filter-button-group
            border
            v-model:state="state"
            :state-list="stateList"
            @change="handleStateChange"
            class="mt-[12px]"
          ></filter-button-group>
          <!--  ****************** 事件信息  ****************** -->
          <div v-show="state === 1">
            <!-- 事件信息 -->
            <van-empty
              v-show="isEmpty(temp.eventInfo)"
              :image-size="[300, 180]"
              :image="emptyPng"
              description="暂无数据"
              class="mt-[40px]"
            />
            <event-details-group
              v-show="!isEmpty(temp.eventInfo)"
              :temp="temp.eventInfo"
              class="mt-[10px]"
            ></event-details-group>
          </div>
          <!--  ******************  END  ***************** -->
          <!--  ****************** 事件流程  ****************** -->
          <!-- 事件信息 -->
          <process-flow-list
            v-show="state === 2 && temp.processFlowList.length > 0"
            :commentList="temp.processFlowList"
            class="mt-[10px]"
          ></process-flow-list>
          <!--  ******************  END  ***************** -->
        </div>
      </div>
    </div>

    <!--   ************** END  ************ -->

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

        <!-- 接收单位 -->
        <!-- <div class="flex mt-[16px]">
          <div class="label mr-[10px]">接收单位：</div>
          <div
            class="receiving-unit-value bg-[#F4F4F4] rounded-[4px] w-[243px] flex justify-between items-center py-[6px] pl-[18px] pr-[9px]"
            @click="handleReceivingUnitClick"
          >
            <span>{{ fieldValue }}</span>
            <span class="receiving-unit-value-icon"></span>
          </div>
        </div> -->
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

    <!--点击预览附件 -->
    <template v-if="previewAttachments">
      <div class="p-[12px]">
        <Breadcrumb
          :list="previewAttachmentsBreadcrumb"
          customBack
          @back="onBack"
        ></Breadcrumb>
      </div>
      <template
        v-if="previewAttachmentsItem.fileSuffix.toLowerCase() === 'pdf'"
      >
        <div
          class="demo bg-[#f1f7f9]"
          ref="demo"
          style="width: 100%; min-height: 200px"
        ></div>
      </template>
      <template
        v-if="previewAttachmentsItem.fileSuffix.toLowerCase() === 'docx'"
      >
        <vue-office-docx
          :src="`${previewAttachmentsItem.currentFileUrl}/decisfile/files${previewAttachmentsItem.currentFileUrl}`"
        />
      </template>
    </template>
  </div>

  <!-- 接收单位气泡弹出框 -->
  <van-popup v-model:show="showReceivingUnitPopover" round position="bottom">
    <van-picker
      :columns="columns"
      :columns-field-names="customFieldName"
      @cancel="showReceivingUnitPopover = false"
      @confirm="onConfirm"
    />
  </van-popup>
</template>
<script setup name="EventDetails">
import EventDetailCard from "@/components/EventDetailCard/index.vue";
import FilterButtonGroup from "@/components/FilterButtonGroup/index.vue";
import EventDetailsGroup from "@/components/EventDetailsGroup/index.vue";
import InstructionsDetails from "@/components/InstructionsDetails/index.vue";
import ProcessFlowList from "@/components/ProcessFlowList/index.vue";
import AttachmentGroup from "@/components/AttachmentGroup/index.vue";
import {
  eventDetail,
  eventAuditHandle,
  attentionOrCancle,
  receiveDpt,
  saveEventComment
} from "@/api/eventDetails";
import { isArray, isEmpty } from "@/utils/validate";
import emptyPng from "@/assets/images/empty.png";
import Pdfh5 from "pdfh5";
import "pdfh5/css/pdfh5.css";

//引入VueOfficeDocx组件
import VueOfficeDocx from "@vue-office/docx";
//引入相关样式
import "@vue-office/docx/lib/index.css";
const breadcrumbList = ref(["首页", "事件详情"]);
const btnGroupState = ref(1);
const btnGroupList = ref([
  { title: "基础信息", id: 1, show: true },
  { title: "批示详情", id: 2, show: false },
  { title: "关联工单", id: 3, show: false }
]);
const stateList = ref([
  { title: "事件详情", id: 1 }
  // TODO: 2024-11-4 处理流程，前端去掉
  // { title: "处理流程", id: 2 }
]);
const route = useRoute();
const state = ref(1);
const handleStateChange = state => {
  console.log(state);
};

switch (route.query.fr) {
  case "1":
    break;
  case "2":
    breadcrumbList.value.splice(1, 0, "审核中心");
    break;
  case "3":
    breadcrumbList.value.splice(1, 0, "领导批示");
    break;
  case "4":
    breadcrumbList.value.splice(1, 0, "我的批示");
    break;
  case "5":
    breadcrumbList.value.splice(1, 0, "我的关注");
    break;
  case "6":
    breadcrumbList.value.splice(1, 0, "今日关注");
    break;
  default:
    break;
}

// 批示按钮点击
const showMemorandumDetail = ref(false);
const memorandumDetailTitle =
  route.query.type === "2" ? ref("反馈内容") : ref("批示建议");
const memorandumDetailPlaceholder =
  route.query.type === "2"
    ? ref("请输入您的反馈内容")
    : ref("请输入您的批示建议");
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

// 接收单位点击
const showReceivingUnitPopover = ref(false);
const columns = ref([]);
const customFieldName = {
  text: "departmentName",
  value: "departmentCode"
};
const getReceiveDpt = async () => {
  const res = await receiveDpt();
  if (isArray(res.result.districtList)) {
    columns.value = [...res.result.districtList];
  } else {
    columns.value = [];
  }
};
const fieldValue = ref("");
const fieldCode = ref("");
const handleReceivingUnitClick = () => {
  showReceivingUnitPopover.value = true;
};
const onConfirm = ({ selectedOptions }) => {
  showReceivingUnitPopover.value = false;
  fieldValue.value = selectedOptions[0].departmentName;
  fieldCode.value = selectedOptions[0].departmentCode;
};

onMounted(() => {
  getEventDetail();
  // getReceiveDpt();
});

const temp = reactive({
  eventInfo: {},
  citizenInfo: {},
  associatedIotInfo: {},
  associatedLegalInfo: {},
  associatedPeopleInfo: {},
  associatedTenementInfo: {},
  eventCommentList: [],
  eventExtendInfo: {},
  processFlowList: [],
  satisfactionInfo: {}
});

const getEventDetail = async () => {
  const res = await eventDetail({ id: route.query.id });
  if (res.result.eventInfo && !isArray(res.result.eventInfo.attachmentInfos)) {
    res.result.eventInfo.attachmentInfos = [];
  }
  temp.eventInfo = res.result.eventInfo || {};
  temp.citizenInfo = res.result.citizenInfo || {};
  temp.satisfactionInfo = res.result.satisfactionInfo || {};
  temp.eventExtendInfo = res.result.eventExtendInfo || {};
  temp.associatedPeopleInfo = res.result.associatedPeopleInfo || {};
  temp.associatedTenementInfo = res.result.associatedTenementInfo || {};
  temp.associatedLegalInfo = res.result.associatedLegalInfo || {};
  temp.associatedIotInfo = res.result.associatedIotInfo || {};
  if (
    !isEmpty(res.result.eventInfo) &&
    (res.result.eventInfo.matterType === "1" ||
      res.result.eventInfo.matterType === 1)
  ) {
    // btnGroupList.value.push({ title: "关联工单", id: 3 });
    btnGroupList.value[2].show = true;
  }
  if (isArray(res.result.eventCommentList)) {
    temp.eventCommentList = [...res.result.eventCommentList];

    if (route.query.bt !== "1" && temp.eventCommentList.length > 0) {
      // 显示批示详情
      btnGroupList.value[1].show = true;
      nextTick(() => {
        btnGroupState.value = 2;
      });
    }
  } else {
    temp.eventCommentList = [];
  }
  if (isArray(res.result.processFlowList)) {
    temp.processFlowList = [...res.result.processFlowList];
  } else {
    temp.processFlowList = [];
  }
};

// 点击推送之后
const handlePushClick = async data => {
  const temp = {
    id: route.query.id,
    examineStatus: data.examineStatus
  };
  if (data.checked) {
    temp.pushTime = data.pushTime;
  }
  try {
    showLoadingToast({
      message: "加载中...",
      forbidClick: true
    });
    await eventAuditHandle({ ...temp });
    console.log("成功");
    if (window.history.state.back) history.back();
    else router.replace("/");
  } catch {
    console.log("失败");
  }
};

const eventDetailCard = ref(null);
console.log(eventDetailCard);
// 点击关注之后
const handleAttentionStatusChange = status => {
  attentionOrCancle({
    eventId: route.query.id,
    attentionStatus:
      !temp.eventInfo.attentionStatus || temp.eventInfo.attentionStatus == "0"
        ? "1"
        : "0"
  })
    .then(res => {
      eventDetailCard.value.showText =
        !temp.eventInfo.attentionStatus || temp.eventInfo.attentionStatus == "0"
          ? "关注成功"
          : "已取消关注";
      temp.eventInfo.attentionStatus =
        !temp.eventInfo.attentionStatus || temp.eventInfo.attentionStatus == "0"
          ? "1"
          : "0";
      eventDetailCard.value.showNotify = true;
      setTimeout(() => {
        eventDetailCard.value.showNotify = false;
      }, 1000);
    })
    .catch(() => {});
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
  saveEventComment({
    eventId: route.query.id,
    receiveCode: null,
    receiveUnit: null,
    comment: message.value,
    commentOrFeedBack: route.query.type === "2" ? 2 : 1
  }).then(() => {
    showSuccessToast("提交成功");
    showMemorandumDetail.value = false;
    getEventDetail();
  });
};

// 附件点击
var pdfDom = null;
const previewAttachmentsBreadcrumb = ref([]);
const previewAttachmentsItem = ref({});
const previewAttachments = ref(false);
const demo = ref();
const handleAttachmentClick = item => {
  console.log(item.currentFileUrl);
  if (!item.currentFileUrl) {
    showFailToast("链接不存在");
    return;
  }
  previewAttachmentsItem.value = { ...item };
  if (item.fileSuffix.toLowerCase() === "pdf") {
    previewAttachments.value = true;
    previewAttachmentsBreadcrumb.value = [item.fileName];
    nextTick().then(() => {
      console.log(item);
      pdfDom = new Pdfh5(demo.value, {
        pdfurl:
          import.meta.env.VITE_PUBLIC_PATH +
          "/decisfile/files" +
          item.currentFileUrl,
        pageNum: false
        // background: {
        //   color: "#f1f7f9"
        // }
      });
    });
  } else if (item.fileSuffix.toLowerCase() === "docx") {
    previewAttachments.value = true;
    previewAttachmentsBreadcrumb.value = [item.fileName];
  } else if (
    item.fileSuffix.toLowerCase() === "png" ||
    item.fileSuffix.toLowerCase() === "jpg" ||
    item.fileSuffix.toLowerCase() === "jpeg"
  ) {
    showImagePreview({
      images: [
        import.meta.env.VITE_PUBLIC_PATH +
          "/decisfile/files" +
          item.currentFileUrl
      ],
      closeable: true
    });
  } else {
    showFailToast("暂不支持该类型预览");
  }
};

const onBack = () => {
  console.log(99);
  if (pdfDom) {
    pdfDom.destroy();
    pdfDom = null;
  }
  previewAttachments.value = false;
};
</script>
<style lang="less">
.event-details-wrapper {
  .van-field {
    background: #f6f9fa;
  }
}
.demo {
  .pdfViewer {
    padding: 0;
  }
}
</style>
<style lang="less" scoped>
@import url("./index.less");
.demo {
  background: #f1f7f9;
  .pdfViewer {
    padding: 0;
  }
}
</style>
