<script setup>
import Item from "./item.vue";
import { isEmpty, isArray } from "@/utils/validate";
import {
  selectMap,
  orderStatusName,
  dataSource,
  eventClassDefine,
  typeName,
  ObjectToArray
} from "@/views/keyEvents/utils/";
import { receiveDpt } from "@/api/eventDetails";
const { listQuery } = defineProps({
  listQuery: {
    type: Object,
    default: () => {
      return {
        pageNo: 1,
        pageSize: 10,
        keyword: "", // 关键词
        normalTypeCode: "", // 事件五种分类：维稳事件、重点事件、矛盾纠纷、网格事件、民生诉求
        streetCode: "", // 街道code
        streetName: "", // 街道名称
        communityCode: "", // 社区编号
        gridCode: "", // 网格编号
        orderStatusName: "", // 工单状态
        reportTimeStart: parseTime(new Date(), "{y}-{m}-{d}"), // 上报开始时间
        reportTimeEnd: parseTime(new Date(), "{y}-{m}-{d}"), // 上报结束时间
        dataSource: "", // 来源
        eventClassDefine: "",
        typeName: ""
      };
    }
  }
});
watch(
  () => listQuery.normalTypeCode,
  async newValue => {
    if (!isEmpty(newValue)) {
      selectList.value = selectMap[newValue];
    }
  }
);
onMounted(() => {
  getReceiveDpt();
});

const orderStatusNameColumns = ObjectToArray(orderStatusName);
const eventClassDefineColumns = ObjectToArray(eventClassDefine);
const dataSourceColumns = ObjectToArray(dataSource);
const typeNameColumns = ObjectToArray(typeName);
const customFieldName = ref({
  text: "text",
  value: "value",
  children: "children"
});
const showPicker = ref(false);
const activeKey = ref("");
const handleItemClick = key => {
  activeKey.value = key;
  showPicker.value = true;
  nextTick().then(() => {
    pickerSelect.value = [listQuery[key]];
  });
};
const onConfirm = ({ selectedOptions }) => {
  switch (activeKey.value) {
    case "streetCode":
      listQuery.streetCode = selectedOptions[0].departmentCode;
      listQuery.streetName = selectedOptions[0].departmentName;
      break;
    default:
      listQuery[activeKey.value] = selectedOptions[0].value;
      break;
  }
  handleChange();

  showPicker.value = false;
};
const emit = defineEmits(["change"]);
const handleChange = () => {
  emit("change");
};
// 搜索条件
const selectList = ref([]);

// 区域列表
const districtList = ref([
  {
    departmentName: "全部区域",
    departmentCode: ""
  }
]);
const getReceiveDpt = async () => {
  receiveDpt()
    .then(res => {
      if (
        isArray(res.result.districtList) &&
        res.result.districtList.length > 0
      ) {
        districtList.value = [...res.result.districtList];
        listQuery.streetCode = res.result.districtList[0].departmentCode;
        listQuery.streetName = res.result.districtList[0].departmentName;
      }

      handleChange();
    })
    .catch(() => {});
};

const pickerSelect = ref([]);
</script>

<template>
  <div class="relative h-[18px] mt-[12px]">
    <div
      class="px-[6px] overflow-x-auto overflow-y-hidden h-full flex font-normal text-[13px] text-[#505050] leading-[18px]"
    >
      <div
        v-for="(item, index) in selectList"
        :key="index"
        class="flex items-center px-[10px] flex-1 justify-center"
        @click="handleItemClick(item)"
      >
        <template v-if="item === 'streetCode'">
          <Item
            :item="item"
            :value="
              districtList.filter(v => v.departmentCode === listQuery[item])[0]
                .departmentName
            "
          ></Item>
        </template>
        <template v-else-if="item === 'orderStatusName'">
          <Item :item="item" :value="orderStatusName[listQuery[item]]"></Item>
        </template>
        <template v-else-if="item === 'eventClassDefine'">
          <Item :item="item" :value="eventClassDefine[listQuery[item]]"></Item>
        </template>
        <template v-else-if="item === 'dataSource'">
          <Item :item="item" :value="dataSource[listQuery[item]]"></Item>
        </template>
        <template v-else-if="item === 'typeName'">
          <Item :item="item" :value="typeName[listQuery[item]]"></Item>
        </template>
      </div>
    </div>
  </div>
  <van-popup v-model:show="showPicker" round position="bottom">
    <van-picker
      v-model="pickerSelect"
      v-show="activeKey === 'streetCode'"
      :columns="districtList"
      :columns-field-names="{
        text: 'departmentName',
        value: 'departmentCode'
      }"
      @cancel="showPicker = false"
      @confirm="onConfirm"
    />
    <van-picker
      v-model="pickerSelect"
      v-show="activeKey === 'orderStatusName'"
      :columns="orderStatusNameColumns"
      @cancel="showPicker = false"
      @confirm="onConfirm"
    />
    <van-picker
      v-model="pickerSelect"
      v-show="activeKey === 'eventClassDefine'"
      :columns="eventClassDefineColumns"
      @cancel="showPicker = false"
      @confirm="onConfirm"
    />
    <van-picker
      v-model="pickerSelect"
      v-show="activeKey === 'dataSource'"
      :columns="dataSourceColumns"
      @cancel="showPicker = false"
      @confirm="onConfirm"
    />
    <van-picker
      v-model="pickerSelect"
      v-show="activeKey === 'typeName'"
      :columns="typeNameColumns"
      @cancel="showPicker = false"
      @confirm="onConfirm"
    />
  </van-popup>
</template>
