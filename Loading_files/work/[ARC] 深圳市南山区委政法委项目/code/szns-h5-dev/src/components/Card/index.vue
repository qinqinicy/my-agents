<template>
  <div class="card-wrapper flex flex-col py-[14px] px-[16px]">
    <div class="flex mb-[10px]">
      <status-card :status="item.handleStatus" class="mr-[4px]"></status-card>
      <span class="title flex-1">{{ item.name }}</span>
      <source-card :lable="item.systemSource" class="ml-[8px]"></source-card>
    </div>
    <div class="flex mb-[4px]">
      <span class="label">推送领导：</span>
      <span class="value flex-1">{{ item.decisionPersonName }}</span>
    </div>
    <div class="flex mb-[4px]">
      <span class="label">上报时间：</span>
      <span class="value flex-1">{{ item.sendTime }}</span>
    </div>
    <!-- <div class="flex mb-[4px]">
      <span class="label">发生地址：</span>
      <span class="value flex">{{ item.takePlace }}</span>
    </div> -->
  </div>
</template>
<script setup>
import StatusCard from "../StatusCard/index.vue";
import SourceCard from "../SourceCard/index.vue";
const { item } = defineProps({
  item: {
    type: Object,
    default: () => {
      return {
        name: "",
        eventNumber: "",
        sendTime: "",
        decisionPersonNam: "",
        subjectAddress: "",
        handleStatus: "0"
      };
    }
  }
});
function statusFilter(status) {
  const statusMap = {
    1: "处理中",
    2: "未处理",
    3: "已处理"
  };
  return statusMap[status];
}
</script>
<style lang="less">
.card-wrapper {
  width: 100%;
  background: linear-gradient(
      180deg,
      rgba(231, 242, 243, 0.47) 0%,
      rgba(255, 255, 255, 0) 100%
    ),
    linear-gradient(
      271deg,
      rgba(255, 255, 255, 0.06) 0%,
      rgba(221, 221, 221, 0.5) 100%
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
  font-weight: 400;
  font-size: 14px;
  color: #2b2b2b;
  line-height: 20px;
  text-align: left;
  font-style: normal;
  .title {
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    text-wrap: nowrap;
    white-space: nowrap;
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
    max-width: 210px;
    overflow: hidden;
    text-overflow: ellipsis;
    text-wrap: nowrap;
    white-space: nowrap;
  }
}
</style>
