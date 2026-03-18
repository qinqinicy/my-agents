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
      :commentListLength="temp.eventCommentList.length"
      @memorandumClick="handleMemorandumClick"
      @pushClick="handlePushClick"
      @attentionStatusChange="handleAttentionStatusChange"
    ></event-detail-card>
    <!--   ************** 点击批示和反馈之前  ************ -->
    <!-- 事件详情 -->
    <template v-if="!showMemorandumDetail">
      <div class="event-details mt-[12px] px-[12px] py-[16px] min-h-max">
        <!-- 大类型按钮组 -->
        <filter-button-group
          bold
          v-model:state="btnGroupState"
          :state-list="btnGroupList"
          @change="handleStateChange"
        ></filter-button-group>
        <!--  ***************** 基础信息  ************** -->
        <template v-if="btnGroupState === 1">
          <div
            class="font-medium text-[15px] text-[#102f2e] leading-[21px] mt-[11px]"
          >
            事件诉求内容
          </div>
          <template v-if="isEmpty(temp.eventInfo.eventSummarize)">
            <van-empty
              :image-size="[300, 180]"
              :image="emptyPng"
              description="暂无数据"
              class="mt-[40px]"
            />
          </template>
          <div
            v-else
            class="text-[14px] text-[#818181] leading-[20px] mt-[10px]"
          >
            {{ temp.eventInfo.eventSummarize }}
            <!-- <template v-if="!isEmpty(temp.eventInfo.eventNumber)">
              （关联单号：<span
                @click="btnGroupState = 3"
                class="text-[#42B9CB]"
                >{{ temp.eventInfo.eventNumber }}</span
              >）
            </template> -->
          </div>
        </template>
        <!--  ******************  END  ***************** -->

        <!--  ********** 如果有批示详情的话  ************ -->
        <template v-if="btnGroupState === 2">
          <instructions-details
            class="mt-[16px]"
            :commentList="temp.eventCommentList"
          ></instructions-details>
        </template>
        <!--  ******************  END  ***************** -->

        <template v-if="btnGroupState === 3">
          <!-- 按钮组 -->
          <filter-button-group
            border
            v-model:state="state"
            :state-list="stateList"
            @change="handleStateChange"
            class="mt-[12px]"
          ></filter-button-group>
          <!--  ****************** 事件信息  ****************** -->
          <template v-if="state === 1">
            <!-- 事件信息 -->
            <template v-if="isEmpty(temp.eventInfo)">
              <van-empty
                :image-size="[300, 180]"
                :image="emptyPng"
                description="暂无数据"
                class="mt-[40px]"
              />
            </template>
            <event-details-group
              v-else
              :temp="temp.eventInfo"
              class="mt-[10px]"
              :list="eventInfo"
            ></event-details-group>
          </template>
          <!--  ******************  END  ***************** -->
          <!--  ****************** 事件流程  ****************** -->
          <template v-if="state === 2 && temp.processFlowList.length > 0">
            <!-- 事件信息 -->
            <process-flow-list
              :commentList="temp.processFlowList"
              class="mt-[10px]"
            ></process-flow-list>
          </template>
          <!--  ******************  END  ***************** -->
          <!--  ****************** 关联信息  ****************** -->
          <template v-if="state === 3">
            <AssociatedCard
              class="mt-[16px]"
              title="关联人员"
              type="associatedPeople"
              :temp="temp.associatedPeopleInfo"
            ></AssociatedCard>
            <AssociatedCard class="mt-[20px]" title="关联事件"></AssociatedCard>
            <AssociatedCard
              class="mt-[20px]"
              title="关联房屋"
              type="associatedTenementInfo"
              :temp="temp.associatedTenementInfo"
            ></AssociatedCard>
            <AssociatedCard
              class="mt-[20px]"
              title="关联法人"
              type="associatedLegal"
              :temp="temp.associatedLegalInfo"
            ></AssociatedCard>
            <AssociatedCard
              class="mt-[20px]"
              title="关联感知"
              type="associatedIot"
              :temp="temp.associatedIotInfo"
            ></AssociatedCard>
          </template>
          <!--  ******************  END  ***************** -->
        </template>
      </div>
      <!--  ********** 事件信息 因为样式单独分开放  ************ -->
      <template
        v-if="btnGroupState === 3 && state === 1 && !isEmpty(temp.eventInfo)"
      >
        <!-- 市民信息 -->

        <div class="event-details mt-[12px] px-[12px] py-[16px]">
          <event-details-group
            title="市民信息"
            :temp="temp.citizenInfo"
            :list="citizensInfo"
            :show-get-info-menu="true"
            :show-up-menu="true"
          ></event-details-group>
        </div>
        <!-- 扩展信息 -->
        <div class="event-details mt-[12px] px-[12px] py-[16px]">
          <event-details-group
            :temp="temp.eventExtendInfo"
            title="扩展信息"
            :list="extension"
            :show-up-menu="true"
          ></event-details-group>
        </div>
        <!-- 满意度信息 -->
        <div class="event-details mt-[12px] px-[12px] py-[16px]">
          <event-details-group
            title="满意度信息"
            :temp="temp.satisfactionInfo"
            :list="satisfaction"
            :show-up-menu="true"
          ></event-details-group>
        </div>
      </template>
      <!--  ******************  END  ***************** -->
    </template>

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
import {
  eventDetail,
  eventAuditHandle,
  attentionOrCancle,
  receiveDpt,
  saveEventComment
} from "@/api/eventDetails";
import { isArray, isEmpty } from "@/utils/validate";
import emptyPng from "@/assets/images/empty.png";
const breadcrumbList = ref(["首页", "事件详情"]);
const btnGroupState = ref(1);
const btnGroupList = ref([
  { title: "基础信息", id: 1 },
  { title: "关联工单", id: 3 }
]);
const stateList = ref([
  { title: "事件详情", id: 1 },
  { title: "处理流程", id: 2 },
  { title: "工单关联", id: 3 }
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

const eventInfo = [
  {
    lable: "工单类型",
    key: "workOrderType",
    type: 1
  },
  {
    lable: "诉求来源",
    key: "appealSource",
    type: 1
  },
  {
    lable: "事件编号",
    key: "eventNumber",
    type: 1
  },
  {
    lable: "事项分类",
    key: "eventClass",
    type: 1
  },
  {
    lable: "当前处置部门",
    key: "currentDisposalDep",
    type: 1
  },
  {
    lable: "流转环节",
    key: "circulationLink",
    type: 1
  },
  {
    lable: "事发地址",
    key: "takeAddress",
    type: 1
  },

  {
    lable: "事发时间",
    key: "appearTime",
    type: 1
  },
  {
    lable: "涉事主体",
    key: "subjectInvolved",
    type: 1
  },
  {
    lable: "事件/诉求内容",
    key: "eventContent",
    type: 2
  },
  {
    lable: "补充信息",
    key: "supplementaryInformation",
    type: 2
  },
  {
    lable: "事发区域",
    key: "incidentArea",
    type: 1
  },
  {
    lable: "所属社区",
    key: "community",
    type: 1
  },
  {
    lable: "所属网格",
    key: "owningGrid",
    type: 1
  },
  {
    lable: "隐患等级",
    key: "dangerLevel",
    type: 1
  },
  {
    lable: "事件来源",
    key: "eventSource",
    type: 1
  }
];
const citizensInfo = [
  {
    lable: "市民称呼",
    key: "name",
    type: 1
  },
  {
    lable: "市民性别",
    key: "sex",
    type: 1
  },
  {
    lable: "市民身份",
    key: "standing",
    type: 1
  },
  {
    lable: "来电号码",
    key: "callingNumber",
    type: 1
  },
  {
    lable: "联系电话",
    key: "contactNumber",
    type: 1
  },
  {
    lable: "证件类型",
    key: "documentType",
    type: 1
  },
  {
    lable: "证件号码",
    key: "documentNumber",
    type: 1
  },
  {
    lable: "是否党员",
    key: "partyMember",
    type: 1,
    filter: true,
    filterMap: {
      1: "是",
      0: "否"
    }
  },
  {
    lable: "是否保密",
    key: "confidentiality",
    type: 1,
    filter: true,
    filterMap: {
      1: "是",
      0: "否"
    }
  }
];

const extension = [
  {
    lable: "是否疑难工单",
    key: "troubleTicket",
    type: 1
  },
  {
    lable: "是否突发事件",
    key: "emergency",
    type: 1,
    filter: true,
    filterMap: {
      1: "是",
      0: "否"
    }
  },
  {
    lable: "突发类型",
    key: "burstType",
    type: 1
  },
  {
    lable: "是否城管类工单",
    key: "urbanManagementWorkOrder",
    type: 1
  },
  {
    lable: "是否领导接电",
    key: "leaderConnection",
    type: 1
  },
  {
    lable: "是否重办单",
    key: "reorder",
    type: 1
  },
  {
    lable: "是否首派责任工单",
    key: "firstDispatchDutyOrder",
    type: 1
  },
  {
    lable: "事项影响情况",
    key: "eventImpact",
    type: 1
  },
  {
    lable: "市民要求回复方式",
    key: "citizensAskReply",
    type: 1
  },
  {
    lable: "回复备注",
    key: "replyNote",
    type: 1
  },
  {
    lable: "市民备注",
    key: "citizenNote",
    type: 1
  },
  {
    lable: "备注地址",
    key: "citizensAskReply",
    type: 1
  },
  {
    lable: "对象分类",
    key: "objectClassification",
    type: 1
  },
  {
    lable: "业务点选",
    key: "serviceClick",
    type: 1
  },
  {
    lable: "事项关键字",
    key: "transactionKey",
    type: 1
  }
];

const satisfaction = [
  {
    lable: "整体满意度",
    key: "overallSatisfaction",
    type: 1
  },
  {
    lable: "评价时间",
    key: "evaluationTime",
    type: 1
  },
  {
    lable: "处置速度",
    key: "disposalSpeed",
    type: 3
  },
  {
    lable: "处置效果",
    key: "treatmentEffect",
    type: 3
  },
  {
    lable: "服务体验",
    key: "serviceExperience",
    type: 3
  },
  {
    lable: "反馈质量",
    key: "feedbackQuality",
    type: 3
  },
  {
    lable: "评价次数",
    key: "evaluationFrequency",
    type: 1
  },
  {
    lable: "评价内容",
    key: "evaluationContent",
    type: 1
  },
  {
    lable: "市民要求回复方式",
    key: "",
    type: 1
  },
  {
    lable: "回复备注",
    key: "",
    type: 1
  },
  {
    lable: "市民备注",
    key: "",
    type: 1
  }
];

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
  temp.eventInfo = res.result.eventInfo || {};
  temp.citizenInfo = res.result.citizenInfo || {};
  temp.satisfactionInfo = res.result.satisfactionInfo || {};
  temp.eventExtendInfo = res.result.eventExtendInfo || {};
  temp.associatedPeopleInfo = res.result.associatedPeopleInfo || {};
  temp.associatedTenementInfo = res.result.associatedTenementInfo || {};
  temp.associatedLegalInfo = res.result.associatedLegalInfo || {};
  temp.associatedIotInfo = res.result.associatedIotInfo || {};
  if (isArray(res.result.eventCommentList)) {
    temp.eventCommentList = [...res.result.eventCommentList];

    if (route.query.bt !== "1" && temp.eventCommentList.length > 0) {
      // 显示批示详情
      btnGroupList.value.splice(1, 0, { title: "批示详情", id: 2 });
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
    attentionStatus: temp.eventInfo.attentionStatus == "0" ? "1" : "0"
  })
    .then(res => {
      eventDetailCard.value.showText =
        temp.eventInfo.attentionStatus == "0" ? "关注成功" : "已取消关注";
      temp.eventInfo.attentionStatus =
        temp.eventInfo.attentionStatus == "0" ? "1" : "0";
      eventDetailCard.value.showNotify = true;
      setTimeout(() => {
        eventDetailCard.value.showNotify = false;
      }, 2000);
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
</script>
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
