import {
  defineStore
} from "pinia";
import {
  store
} from "@/store";
export const keyEventDetailStore = defineStore({
  id: "keyEventDetail",
  state: () => ({
    // 缓存页面 keepAlive
    event: JSON.parse(localStorage.getItem("event")) || {}
  }),
  actions: {
    setEvent(event) {
      this.event = {...event}
      localStorage.setItem("event", JSON.stringify(event))
    },
    // 退出登陆
    clearEvent() {
      localStorage.clear('event')
    }
  }
});

export function keyEventDetailStoreHook() {
  return keyEventDetailStore(store);
}