<template>
  <van-dropdown-menu swipe-threshold="4">
    <!-- 区域 -->
    <van-dropdown-item :title="areaTitle" ref="incidentAreaRef">
      <van-picker
        :columns="columns"
        :columns-field-names="customFieldName"
        @cancel="closeAreaDropDown"
        @confirm="onConfirm"
      />
    </van-dropdown-item>
    <!-- 处理状态 -->
    <van-dropdown-item
      v-model="temp.handleStatus"
      :options="handleStatusOption"
      @change="handleChange"
    />
    <!-- 涉及领域 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '维稳事件'"
      v-model="temp.relatedField"
      :options="relatedFieldOption"
      @change="handleChange"
    />
    <!-- 紧急标签 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '重点事件'"
      v-model="temp.emergencyLable"
      :options="emergencyLableOption"
      @change="handleChange"
    />
    <!-- 风险等级 -->
    <van-dropdown-item
      v-if="
        temp.eventSourceClass === '重点事件' ||
        temp.eventSourceClass === '维稳事件'
      "
      v-model="temp.dangerLevel"
      :options="dangerLevelOption"
      @change="handleChange"
    />
    <!-- 区域分布 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '维稳事件'"
      v-model="temp.areaDistribution"
      :options="areaDistributionOption"
      @change="handleChange"
    />
    <!-- 主办单位 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '维稳事件'"
      v-model="temp.organizer"
      :options="organizerOption"
      @change="handleChange"
    />
    <!-- 事件来源 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '民生诉求'"
      v-model="temp.appealSource"
      :options="eventSourceOption"
      @change="handleChange"
    />
    <!-- 事件类型 -->
    <van-dropdown-item
      v-if="temp.eventSourceClass === '网格事件'"
      v-model="temp.eventType"
      :options="eventTypeOption"
      @change="handleChange"
    />
    <!-- 事件分类 -->
    <van-dropdown-item
      v-if="
        temp.eventSourceClass === '矛盾纠纷' ||
        temp.eventSourceClass === '网格事件' ||
        temp.eventSourceClass === '民生诉求'
      "
      v-model="temp.eventClass"
      :options="eventClassOption"
      @change="handleChange"
    />
  </van-dropdown-menu>
</template>

<script setup>
import { receiveDpt } from "@/api/eventDetails";
import { selectSelect } from "@/api/keyEvents";
import { isArray } from "@/utils/validate";
const { listQuery } = defineProps({
  listQuery: {
    type: Object,
    default: () => {
      return {
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
      };
    }
  }
});
const temp = defineModel("listQuery");
// 状态
const handleStatusOption = [
  { text: "全部状态", value: null },
  { text: "未处理", value: "0" },
  { text: "已处理", value: "2" }
];
// 紧急标签
const emergencyLableOption = [
  { text: "全部标签", value: null },

  { text: "一般件", value: "一般件" },
  { text: "紧急件", value: "紧急件" },
  { text: "督办件", value: "督办件" }
];

// 风险等级
const dangerLevelOption = [
  { text: "风险等级", value: null },

  { text: "重大风险", value: "重大风险" },
  { text: "较大风险", value: "较大风险" },
  { text: "一般风险", value: "一般风险" },
  { text: "低风险", value: "低风险" },
  { text: "无风险", value: "无风险" }
];
// 区域分布
const areaDistributionOption = [
  { text: "全部区域", value: null },
  { text: "区内", value: "区内" },
  { text: "区外", value: "区外" }
];
// 事件类型
const eventTypeOption = [
  { text: "全部类型", value: null },
  { text: "一般事件", value: "一般事件" },
  { text: "即采即办", value: "即采即办" }
];
// 涉及领域
const relatedFieldOption = ref([{ text: "全部领域", value: null }]);
// 事件分类
const eventClassOption = ref([{ text: "全部分类", value: null }]);
// 事件来源
const eventSourceOption = ref([{ text: "全部来源", value: null }]);
// 主办单位
const organizerOption = ref([{ text: "全部单位", value: null }]);

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
  emit("change");
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
  getSelectSelect();
});

const getSelectSelect = () => {
  selectSelect().then(res => {
    if (isArray(res.result.relatedField)) {
      relatedFieldOption.value = [
        { text: "全部领域", value: null },
        ...res.result.relatedField.map(v => {
          return { text: v, value: v };
        })
      ];
    }

    if (isArray(res.result.eventClass)) {
      eventClassOption.value = [
        { text: "全部分类", value: null },
        ...res.result.eventClass.map(v => {
          return { text: v, value: v };
        })
      ];
    }

    if (isArray(res.result.eventSource)) {
      eventSourceOption.value = [
        { text: "全部来源", value: null },
        ...res.result.eventSource.map(v => {
          return { text: v, value: v };
        })
      ];
    }

    if (isArray(res.result.organizer)) {
      organizerOption.value = [
        { text: "全部单位", value: null },
        ...res.result.organizer.map(v => {
          return { text: v, value: v };
        })
      ];
    }
  });
};
const emit = defineEmits(["change"]);
const handleChange = () => {
  emit("change");
};
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
