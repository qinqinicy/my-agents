<script setup>
const { startDate, endDate } = defineProps({
  startDate: {
    type: String,
    default: ""
  },
  endDate: {
    type: String,
    default: ""
  }
});

const handleDateClick = () => {
  showPicker.value = true;
};

const showPicker = ref(false);

const currentStareDate = ref([]);
const currentEndDate = ref([]);
currentStareDate.value = startDate.split("-");
currentEndDate.value = endDate.split("-");
const emit = defineEmits(["change", "update:startDate", "update:endDate"]);
const onDateConfirm = () => {
  closeDatePicker();
  emit("change", [
    currentStareDate.value.join("-"),
    currentEndDate.value.join("-")
  ]);
  console.log(currentStareDate.value.join("-"), currentEndDate.value.join("-"));
  emit("update:startDate", currentStareDate.value.join("-"));
  emit("update:endDate", currentEndDate.value.join("-"));
};
const closeDatePicker = () => {
  showPicker.value = false;
};
</script>

<template>
  <div
    class="date-picker-container px-[14px] py-[9px] flex items-center"
    @click="handleDateClick"
  >
    <span class="icon"></span>
    <span class="ml-[20px]">{{ startDate }} —— {{ endDate }}</span>
  </div>

  <!-- 时间选择 -->
  <van-popup v-model:show="showPicker" round position="bottom">
    <van-picker-group
      title="选择日期"
      :tabs="['开始日期', '结束日期']"
      @confirm="onDateConfirm"
      @cancel="closeDatePicker"
    >
      <van-date-picker v-model="currentStareDate" />
      <van-date-picker v-model="currentEndDate" />
    </van-picker-group>
  </van-popup>
</template>

<style lang="less" scoped>
.date-picker-container {
  background: linear-gradient(
      180deg,
      rgba(231, 242, 243, 0.47) 0%,
      rgba(255, 255, 255, 0) 100%
    ),
    linear-gradient(
      264deg,
      rgba(255, 255, 255, 0.47) 0%,
      rgba(221, 221, 221, 0.21) 100%
    );
  box-shadow: 2px 2px 4px 1px rgba(185, 201, 201, 0.16);
  border-radius: 8px;
  border: 1px solid;
  border-image: linear-gradient(
      215deg,
      rgba(255, 255, 255, 0),
      rgba(255, 255, 255, 1)
    )
    1 1;
  font-weight: 500;
  font-size: 14px;
  color: #505050;
  line-height: 20px;
  font-style: normal;
  .icon {
    display: block;
    width: 18px;
    height: 18px;
    background: url("@/assets/images/date-picker_1.png") 0px 0px no-repeat;
    background-size: 100%;
  }
}
</style>
