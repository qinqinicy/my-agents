<template>
  <van-dropdown-menu swipe-threshold="4">
    <van-dropdown-item :title="areaTitle" ref="incidentAreaRef">
      <van-picker
        :columns="columns"
        :columns-field-names="customFieldName"
        @cancel="closeAreaDropDown"
        @confirm="onConfirm"
      />
    </van-dropdown-item>
    <van-dropdown-item v-model="temp.handleStatus" :options="option2" />
    <van-dropdown-item v-model="temp.appealSource" :options="option3" />
    <van-dropdown-item :title="dateTitle" ref="selectTimeRef">
      <van-picker-group
        title="选择日期"
        :tabs="['开始日期', '结束日期']"
        cancel-button-text="重置"
        @confirm="onTimeConfirm"
        @cancel="cancelTimeDropDown"
      >
        <van-date-picker v-model="startDate" />
        <van-date-picker v-model="endDate" />
      </van-picker-group>
    </van-dropdown-item>
  </van-dropdown-menu>
</template>

<script setup>
import { receiveDpt } from "@/api/eventDetails";
import { isArray } from "@/utils/validate";
const { listQuery } = defineProps({
  listQuery: {
    type: Object,
    default: () => {
      return {
        appealSource: null,
        examineStatus: 0,
        name: null,
        handleStatus: null,
        incidentArea: null,
        beginDate: null,
        endDate: null
      };
    }
  }
});
const temp = defineModel("listQuery");
// 状态
const option2 = [
  { text: "全部状态", value: null },
  { text: "未处理", value: "0" },
  { text: "处理中", value: "1" },
  { text: "已处理", value: "2" }
];
const option3 = [
  { text: "全部来源", value: null },

  { text: "综治事件", value: "综治事件" },
  { text: "维稳事件", value: "维稳事件" },
  { text: "网格事件", value: "网格事件" },
  { text: "民生诉求", value: "民生诉求" },
  { text: "AI预警", value: "AI预警" },
  { text: "市通报", value: "市通报" }
];

// *******************    选择时间    ********************
const dateTitle = ref("选择时间");
const today = new Date();

// 选择时间
const startDate = ref([
  today.getFullYear(),
  today.getMonth() + 1,
  today.getDate()
]);
const endDate = ref([
  today.getFullYear(),
  today.getMonth() + 1,
  today.getDate()
]);

const onTimeConfirm = () => {
  dateTitle.value = `${startDate.value.join("/")} - ${endDate.value.join("/")}`;
  console.log(startDate, endDate, "999");
  // listQuery.beginDate = new Date(startDate.value.join("-"));
  listQuery.beginDate = startDate.value.join("-");
  // listQuery.endDate = new Date(endDate.value.join("-"));
  listQuery.endDate = endDate.value.join("-");
  closeTimeDropDown();
};

const cancelTimeDropDown = () => {
  dateTitle.value = `选择时间`;
  listQuery.beginDate = null;
  // listQuery.endDate = new Date(endDate.value.join("-"));
  listQuery.endDate = null;
  closeTimeDropDown();
};

// 关闭时间下拉
const selectTimeRef = ref(null);
const closeTimeDropDown = () => {
  if (selectTimeRef.value) {
    selectTimeRef.value.toggle();
  }
};
// *******************    end    ********************

// *******************    选择地区    ********************
const areaTitle = ref("选择区域");
const customFieldName = {
  text: "departmentName",
  value: "departmentCode"
};
const incidentAreaRef = ref(null);
const closeAreaDropDown = () => {
  if (incidentAreaRef.value) {
    incidentAreaRef.value.toggle();
  }
};
const onConfirm = ({ selectedOptions }) => {
  console.log(selectedOptions);
  listQuery.incidentArea =
    selectedOptions[0].departmentName === "全部区域"
      ? null
      : selectedOptions[0].departmentName;
  closeAreaDropDown();
  areaTitle.value = selectedOptions[0].departmentName;
  // showReceivingUnitPopover.value = false;
  // fieldValue.value = selectedOptions[0].departmentName;
  // fieldCode.value = selectedOptions[0].departmentCode;
};

const columns = ref([]);
const getReceiveDpt = async () => {
  const res = await receiveDpt();
  if (isArray(res.result.districtList)) {
    columns.value = [
      {
        departmentName: "全部区域",
        departmentCode: null
      },
      ...res.result.districtList
    ];
  } else {
    columns.value = [];
  }
};
// *******************    end    ********************
onMounted(() => {
  getReceiveDpt();
});
</script>

<style>
:root:root {
  --van-dropdown-menu-height: 18px;
  --van-dropdown-menu-background: transparent;
  --van-dropdown-menu-shadow: none;
  --van-dropdown-menu-title-font-size: 13px;
  --van-dropdown-menu-title-text-color: #505050;
  --van-dropdown-menu-title-active-text-color: #1699ad;
  --van-dropdown-menu-title-padding: 4px;
  --van-dropdown-menu-title-line-height: 18px;
  --van-dropdown-menu-option-active-color: #1699ad;
}
</style>
