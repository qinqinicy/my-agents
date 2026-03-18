<template>
  <div class="associated-card-container">
    <div class="title-group flex justify-between items-center">
      <div class="flex items-center">
        <span class="group_1 mr-[4px]"></span>
        <span class="title">{{ title }}</span>
      </div>
      <div class="right" @click="handleUpClick">
        <span class="up">{{ upState === 1 ? "收起" : "展开" }}</span>
      </div>
    </div>
    <div ref="contentContainer" class="overflow-hidden h-0">
      <div
        class="content-container px-[16px] py-[14px] mt-[10px] bg-[#F6F9FA] rounded-[8px]"
      >
        <!-- 关联人物 -->
        <template v-if="type === 'associatedPeople'">
          <div class="flex">
            <span class="title">{{ temp.peopleName }}</span>
            <span class="ml-[15px] text-[#505050]">{{ temp.peopleAge }}</span>
          </div>
          <div class="flex mt-[9px]">
            <span>{{ temp.peoplePhone }}</span>
            <span v-show="temp.peopleidCard" class="mx-[12px]">|</span>
            <span>{{ temp.peopleidCard }}</span>
          </div>
        </template>

        <!-- 关联房屋 -->
        <template v-if="type === 'associatedTenementInfo'">
          <div class="flex">
            <span class="title">{{ temp.name }}</span>
          </div>
          <div class="flex mt-[4px]">
            <span>房屋编码：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.code }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>所属区域：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.region }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>房屋地址：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.address }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>楼栋长：</span>
            <span
              class="overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.chief }}</span
            >
            <span v-show="temp.chiefPhone" class="mx-[10px]">|</span>
            <span
              class="overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.chiefPhone }}</span
            >
          </div>
        </template>

        <!-- 关联法人 -->
        <template v-if="type === 'associatedLegal'">
          <div class="flex">
            <span class="title">{{ temp.name }}</span>
          </div>
          <div class="flex mt-[4px]">
            <span>法人登记名称：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.registeredName }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>法人注册地址：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.address }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>实际经营地址：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.businessAddress }}</span
            >
          </div>
          <div class="flex mt-[4px]">
            <span>企业联系电话：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.phone }}</span
            >
          </div>
        </template>
        <!-- 物联感知 -->
        <template v-if="type === 'associatedIot'">
          <div class="flex">
            <span class="title">lD:{{ temp.perceptionId }}</span>
          </div>
          <div class="flex mt-[4px]">
            <span>摄像头位置：</span>
            <span
              class="flex-1 overflow-hidden break-words inline-block text-[#2B2B2B]"
              >{{ temp.address }}</span
            >
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
const { title, type, temp } = defineProps({
  title: {
    type: String,
    default: "关联人员"
  },
  type: {
    type: String,
    default: "associatedPeople" // associatedPeople-关联人物   associatedIot-物联感知 associatedTenementInfo-关联房屋 associatedLegal-关联法人
  },
  temp: {
    type: Object,
    default: () => {
      return {};
    }
  }
});
const upState = ref(2);
const contentContainer = ref();
const handleUpClick = () => {
  if (upState.value === 1) {
    contentContainer.value.style = "height: 0px";
    upState.value = 2;
  } else {
    contentContainer.value.style = "height: auto";
    upState.value = 1;
  }
};
</script>

<style lang="less" scoped>
.associated-card-container {
  .group_1 {
    display: inline-block;
    width: 3px;
    height: 16px;
    background: #1699ad;
    border-radius: 2px;
  }
  .title {
    font-weight: 500;
    font-size: 15px;
    color: #102f2e;
    line-height: 21px;
    text-align: left;
    font-style: normal;
  }
  .up {
    font-weight: 400;
    font-size: 14px;
    color: #1699ad;
    line-height: 20px;
    text-align: left;
    font-style: normal;
  }
  .content-container {
    font-weight: 400;
    font-size: 15px;
    color: #818181;
    line-height: 21px;
    text-align: left;
    font-style: normal;
    .title {
      font-weight: 500;
      font-size: 15px;
      color: #2b2b2b;
      line-height: 21px;
      text-align: left;
      font-style: normal;
    }
  }
}
</style>
