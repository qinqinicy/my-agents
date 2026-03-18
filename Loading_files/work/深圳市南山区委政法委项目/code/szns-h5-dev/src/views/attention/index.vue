<template>
  <div class="relative">
    <tab />
    <div class="p-[12px]">
      <Breadcrumb :list="breadcrumbList"></Breadcrumb>
    </div>
    <div class="p-[12px]">
      <div class="flex flex-col rounded-[8px] px-[14px] py-[10px] bg-white">
        <div class="title-ground flex justify-between">
          <span class="title flex-1">{{ title }}</span>
          <span
            >共计 <span class="length">{{ list.length }}</span> 条记录</span
          >
        </div>
        <!-- 卡片 -->
        <van-list class="mt-[18px]" finished-text="没有更多了">
          <card
            v-for="(item, index) in list"
            :key="index"
            :item="item"
            class="mb-[12px]"
            @click="handleCardClick(item)"
          ></card>
        </van-list>
      </div>
    </div>
  </div>
</template>

<script setup>
import Tab from "@/components/Tab/index.vue";
import Breadcrumb from "@/components/Breadcrumb/index.vue";
import {
  myCommentList,
  myAttentionList,
  leaderCommentList
} from "@/api/attention";
import { isArray } from "@/utils/validate";
const breadcrumbList = ["首页"];
const title = ref("");

const route = useRoute();
const router = useRouter();
if (route.query.form === "0") {
  breadcrumbList.push("关注概览/领导批示");
  title.value = "领导批示列表";
} else if (route.query.form === "1") {
  breadcrumbList.push("关注概览/我的批示");
  title.value = "我的批示列表";
} else if (route.query.form === "2") {
  breadcrumbList.push("关注概览/我的关注");
  title.value = "我的关注列表";
}

// 列表
const list = ref([]);

// 我的关注列表
const getAttentionList = async () => {
  const res = await myAttentionList({});
  if (isArray(res.result.events)) {
    list.value = [...res.result.events];
  } else {
    list.value = [];
  }
};

// 我的批示列表
const getCommentList = async () => {
  const res = await myCommentList({});
  if (isArray(res.result.events)) {
    list.value = [...res.result.events];
  } else {
    list.value = [];
  }
};

// 领导批示列表
const getLeaderCommentList = async () => {
  const res = await leaderCommentList({});
  if (isArray(res.result.events)) {
    list.value = [...res.result.events];
  } else {
    list.value = [];
  }
};

onMounted(() => {
  if (route.query.form === "0") {
    getLeaderCommentList();
  } else if (route.query.form === "1") {
    getCommentList();
  } else if (route.query.form === "2") {
    getAttentionList();
  }
});
function handleCardClick(item) {
  router.push({
    path: "/eventDetails",
    query: {
      id: item.id,
      bt: 2,
      type: route.query.form === "0" ? 2 : 1,
      fr: route.query.form === "0" ? 3 : route.query.form === "1" ? 4 : 5
    }
  });
}
</script>

<style lang="less" scoped>
@import url("./index.less");
</style>
