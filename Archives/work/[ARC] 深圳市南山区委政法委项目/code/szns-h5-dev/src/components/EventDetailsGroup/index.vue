<template>
  <div class="info-container">
    <div class="flex justify-between">
      <div>
        <span class="title">{{ title }}</span>
        <template v-if="showGetInfoMenu">
          <span class="getinfo ml-[4px]">点击获取市民信息</span>
        </template>
      </div>
      <template v-if="showUpMenu">
        <div class="right" @click="handleUpClick">
          <span class="up">{{ upState === 1 ? "收起" : "展开" }}</span>
        </div>
      </template>
    </div>
    <div ref="listdom" class="list">
      <div class="item mt-[10px]" v-for="(item, index) in list" :key="index">
        <template v-if="item.type === 2">
          <div class="lable mb-[6px]">{{ item.lable }}</div>
          <div class="value" style="white-space: pre-wrap">
            {{
              temp[item.key] ? temp[item.key].replace(/\\r\\n/g, "\r\n") : ""
            }}
          </div>
        </template>
        <template v-else-if="item.type === 3">
          <div class="flex justify-between">
            <div class="lable mb-[6px]">{{ item.lable }}</div>
            <div class="rate">
              <span
                v-for="index in 5"
                :key="index"
                class="rate-item"
                :class="index <= Number(temp[item.key]) ? 'ok' : ''"
              ></span>
            </div>
          </div>
        </template>
        <template v-else>
          <div class="flex justify-between">
            <div class="lable">{{ item.lable }}</div>
            <div v-if="item.filter" class="value nowrap">
              {{ item.filterMap[temp[item.key]] || "" }}
            </div>
            <div v-else class="value nowrap">
              {{ temp[item.key] || "" }}
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
<script setup>
const { title, list, temp } = defineProps({
  title: {
    type: String,
    default: "事件信息"
  },
  temp: {
    type: Object,
    default: () => {}
  },
  list: {
    type: Array,
    default: () => {
      return [
        {
          lable: "事件编号",
          key: "orderNum",
          type: 1
        },
        {
          lable: "事件类型",
          key: "wordOrderType",
          type: 1
        },
        {
          lable: "事件时间",
          key: "appearTime",
          type: 1
        },
        {
          lable: "事件地址",
          key: "takeAddress",
          type: 1
        },
        {
          lable: "事件/诉求内容",
          key: "eventContent",
          type: 2
        }
      ];
    }
  },
  showGetInfoMenu: {
    type: Boolean,
    default: false
  },
  showUpMenu: {
    type: Boolean,
    default: false
  }
});
let upState = ref(1);
const listdom = ref();
const handleUpClick = () => {
  if (upState.value === 1) {
    listdom.value.style = "height: 0px";
    upState.value = 2;
  } else {
    listdom.value.style = "height: auto";
    upState.value = 1;
  }
};
</script>
<style lang="less" scoped>
.info-container {
  .title {
    font-weight: 500;
    font-size: 15px;
    color: #102f2e;
    line-height: 21px;
    text-align: left;
    font-style: normal;
  }
  .list {
    overflow: hidden;
  }

  .lable {
    font-weight: 400;
    font-size: 14px;
    color: #818181;
    line-height: 20px;
    text-align: left;
    font-style: normal;
  }
  .value {
    font-weight: 400;
    font-size: 14px;
    color: #2b2b2b;
    line-height: 20px;
    text-align: left;
    font-style: normal;
    &.nowrap {
      max-width: 200px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-align: right;
    }
  }
  .getinfo {
    font-weight: 400;
    font-size: 14px;
    color: #1699ad;
    line-height: 20px;
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
  .rate-item {
    display: inline-block;
    width: 22px;
    height: 22px;
    background: url("@/assets/images/event-details-group_2.png") 0px 0px
      no-repeat;
    background-size: 16px 16px;
    &.ok {
      width: 22px;
      height: 22px;
      background-image: url("@/assets/images/event-details-group_1.png");
      background-size: 100%;
      background-position-y: -2px;
    }
  }
}
</style>
