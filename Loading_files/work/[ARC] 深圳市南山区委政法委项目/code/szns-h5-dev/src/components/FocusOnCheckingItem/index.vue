<template>
  <div
    class="item-container pl-[12px] pr-[20px] py-[8px] bg-[#F6F9FA] rounded-[8px] flex items-center justify-between"
    :class="temp.isError === '是' ? 'error' : ''"
  >
    <div class="left flex items-center">
      <div class="label mr-[8px]" @click="handleItemClick()">
        {{ temp.label }}
      </div>
      <!-- <van-icon name="question-o" @click="handleQuestionClick" /> -->
      <van-popover :placement="placementTop ? 'top-start' : 'bottom-start'">
        <div
          class="w-[180px] pl-[10px] pr-[6px] py-[10px] font-normal text-[12px] text-[#818181] leading-17px"
        >
          <div class="title mb-[3px] text-[#1699AD]">预警规则</div>
          <div>{{ temp.ruler }}</div>
        </div>
        <template #reference>
          <van-icon name="question-o" />
        </template>
      </van-popover>
    </div>
    <div
      class="detail flex-1 px-[8px] overflow-hidden focus-on-checking-item-detail-container"
    >
      <van-popover :placement="placementTop ? 'top-end' : 'bottom-end'">
        <div
          class="w-[180px] pl-[10px] pr-[6px] py-[10px] font-normal text-[12px] text-[#818181] leading-17px"
        >
          <div class="title text-[#1699AD]">{{ temp.detail }}</div>
          <!-- <div>{{ temp.ruler }}</div> -->
        </div>
        <template #reference>
          <span
            class="block w-full text-ellipsis text-nowrap whitespace-nowrap overflow-hidden"
            >{{ temp.detail }}</span
          >
        </template>
      </van-popover>
    </div>

    <div class="value" @click="handleItemClick()">
      <span
        :style="{
          fontFamily: 'DINAlternate, DINAlternate'
        }"
        >{{ temp.value }}</span
      >
      <span v-show="temp.unit" class="unit ml-[2px]">{{ temp.unit }}</span>
    </div>
  </div>
</template>
<script setup>
const showPopover = ref(false);
const handleQuestionClick = () => {
  showDialog({
    title: "预警规则",
    message: temp.describe,
    confirmButtonColor: "#1699ad"
  });
};
const { temp, placementTop } = defineProps({
  placementTop: {
    type: Boolean,
    default: false
  },
  temp: {
    type: Object,
    required: true,
    default: () => {
      return {
        label: "近一月重复",
        value: "100",
        unit: "件",
        detail: "",
        isError: false,
        describe:
          "预警规则：近一个月维稳次数超过2次的事件，当超过2次时，每预警一次，风险提醒一次"
      };
    }
  }
});
const handleItemClick = () => {
  if (temp.url) {
    window.location.href = temp.url;
  }
};
</script>
<style>
.van-popover--light .van-popover__content {
  background: #ffffff;
  box-shadow: 0px 0px 4px 0px rgba(204, 201, 201, 0.5);
}
.focus-on-checking-item-detail-container .van-popover__wrapper {
  width: 100%;
  display: block;
}
</style>
<style lang="less" scoped>
.label {
  font-weight: 500;
  font-size: 14px;
  color: rgba(7, 21, 20, 0.6);
  line-height: 20px;
}
.value {
  font-weight: bold;
  font-size: 20px;
  color: #1699ad;
  line-height: 24px;
  text-align: center;
  font-style: normal;
}
.detail {
  font-weight: 400;
  font-size: 14px;
  color: #42b9cb;
  line-height: 20px;
}
.unit {
  font-weight: 400;
  font-size: 12px;
  color: #bdbdbd;
  line-height: 20px;
}
.item-container.error {
  background: rgba(255, 35, 0, 0.02);
  border-radius: 8px;
  border: 1px solid rgba(255, 35, 0, 0.28);
  .value {
    color: #ff2300;
    font-weight: bold;
  }
  .unit {
    color: #ff2300;
  }
}
</style>
