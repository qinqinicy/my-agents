<template>
  <div
    class="process-flow-wrapper bg-[#F6F9FA] rounded-[8px] px-[12px] pt-[16px] pl-[29px]"
  >
    <div class="item-container">
      <div
        v-for="(item, index) in commentList"
        :key="item.id"
        class="relative pb-[16px]"
      >
        <div class="title flex justify-between">
          <span class="flex-1 pr-[6px]">{{ item.disposalDep }}处理</span>
          <span class="flex-1 text-right text-nowrap whitespace-nowrap">{{
            item.createdTime
          }}</span>
        </div>
        <div
          v-for="keyItem in list"
          :key="keyItem.key"
          class="justify-between mt-[10px]"
          :class="keyItem.key === 'fileUrl' ? '' : 'flex'"
        >
          <span class="flex-1 pr-[6px]">{{ keyItem.label }}</span>

          <template v-if="keyItem.key === 'fileUrl' && item[keyItem.key]">
            <div class="w-full mt-[10px]">
              <van-image
                v-for="(imgItem, imgIndex) in item[keyItem.key].split(',')"
                :key="imgIndex"
                width="78"
                height="78"
                :src="imgItem"
                radius
                class="mr-[12px]"
                @click="
                  handleImagePreview(item[keyItem.key].split(','), imgIndex)
                "
              />
            </div>
          </template>
          <template v-else>
            <span class="flex-1 text-right">{{ item[keyItem.key] }}</span>
          </template>
        </div>
        <div class="circle-container"><span class="step__circle"></span></div>
        <div v-if="index < commentList.length - 1" class="step__line"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
const { commentList } = defineProps({
  commentList: {
    type: Array,
    default: () => []
  }
});
const list = [
  {
    label: "处理部门",
    key: "disposalDep"
  },
  {
    label: "处理人",
    key: "processingPersonnel"
  },
  {
    label: "处理意见",
    key: "personnelOpinion"
  },
  {
    label: "处理后附件",
    key: "fileUrl"
  }
];
const handleImagePreview = (arr, index) => {
  showImagePreview({
    images: arr,
    startPosition: index
  });
};
</script>

<style lang="less" scoped>
.process-flow-wrapper {
  .item-container {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    .title {
      color: #2b2b2b;
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
