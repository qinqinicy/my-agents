<template>
  <router-view />
</template>
<script setup>
import { useUserStoreHook } from "@/store/modules/user";
import { getUserInfo } from "@/api/user";
import { isEmpty, isArray } from "@/utils/validate";
// 默认获取父页面本地存储token
if (window.parent.sessionStorage.getItem("Authorization")) {
  useUserStoreHook().setToken(
    window.parent.sessionStorage.getItem("Authorization")
  );
}
if (window.parent.sessionStorage.getItem("authorization")) {
  useUserStoreHook().setToken(
    window.parent.sessionStorage.getItem("authorization")
  );
}
// 获取token
window.addEventListener("message", event => {
  if (event.data.key === "Token") {
    useUserStoreHook().setToken(event.data.value);
  }
});

// 获取用户信息
getUserInfo().then(res => {
  if (!isEmpty(res.result)) {
    const {
      departmentCode,
      departmentName,
      exp,
      roleDataList,
      roleList,
      token,
      userId,
      userName
    } = res.result;
    const temp = {
      departmentCode,
      departmentName,
      exp,
      roleDataList: [],
      roleList: [],
      token,
      userId,
      userName
    };
    if (isArray(roleDataList)) {
      temp.roleDataList = [...roleDataList];
    }
    if (isArray(roleList)) {
      temp.roleList = [...roleList];
    }
    useUserStoreHook().setUserInfo(temp);
  }
});
</script>
<style></style>
