import {
  defineStore
} from "pinia";
import {
  store
} from "@/store";
export const useUserStore = defineStore({
  id: "user",
  state: () => ({
    // 缓存页面 keepAlive
    token: sessionStorage.getItem("authorization") || '',
    // token: '',
    userInfo: {
      "token": "",
      "userId": "",
      "userName": "",
      "departmentName": "",
      "departmentCode": "",
      "roleList": [],
      "roleDataList": [],
      "exp": 0
    }
  }),
  actions: {
    setToken(token) {
      this.token = token
    },
    //  刷新token
    refreshToken() {
      window.parent.postMessage({
        key: 'RefreshToken'
      }, "*")
    },
    // 退出登陆
    logOut() {
      window.parent.postMessage({
        key: 'LogOut'
      }, "*")
    },
    setUserInfo(user) {
      this.userInfo = {
        ...user
      }
    }
  }
});

export function useUserStoreHook() {
  return useUserStore(store);
}