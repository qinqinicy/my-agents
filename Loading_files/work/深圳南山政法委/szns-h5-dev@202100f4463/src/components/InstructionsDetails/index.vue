<script setup>
const { commentList } = defineProps({
  commentList: {
    type: Array,
    default: () => []
  }
});
</script>

<template>
  <div class="instructions-detail-wrapper px-[12px] py-[16px] pl-[35px]">
    <div class="items-container">
      <div
        v-for="(item, index) in commentList"
        :key="item.id"
        class="step relative"
        :class="index < commentList.length - 1 ? ' pb-[25px]' : ''"
      >
        <div class="flex justify-between">
          <span class="title">{{ item.userName }}</span>
          <span class="time">{{ item.createdTime }}</span>
        </div>
        <div class="value mt-[6px]">
          {{ item.comment }}
        </div>
        <div class="circle-container"><span class="step__circle"></span></div>
        <div v-if="index < commentList.length - 1" class="step__line"></div>
      </div>
    </div>
  </div>
</template>

<style lang="less" scoped>
.instructions-detail-wrapper {
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
  .items-container {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    .title {
      font-weight: 500;
      font-size: 15px;
      color: #333333;
      line-height: 21px;
      text-align: left;
      font-style: normal;
    }
    .time {
      font-weight: 400;
      font-size: 14px;
      color: #2b2b2b;
      line-height: 20px;
      text-align: center;
      font-style: normal;
    }
  }
  .circle-container {
    position: absolute;
    top: 8px;
    left: -16px;
    transform: translate(-50%, -50%);
    .step__circle {
      display: block;
      width: 16px;
      height: 16px;
      background: url("@/assets/images/step__circle.png") 0px 0px no-repeat;
      background-size: 100%;
    }
  }
  .step__line {
    width: 2px;
    height: 100%;
    position: absolute;
    top: 8px;
    left: -17px;
    background: #1699ad;
  }
}
</style>
