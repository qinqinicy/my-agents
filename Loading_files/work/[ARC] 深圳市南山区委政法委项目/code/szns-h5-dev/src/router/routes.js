import Layout from "@/layout/index.vue";
import Home from "@/views/home/index.vue";

const routes = [{
  path: "/",
  name: "root",
  component: Layout,
  redirect: {
    name: "Home"
  },
  children: [{
      path: "home",
      name: "Home",
      component: Home,
      meta: {
        title: "首页"
      }
    },
    {
      path: "auditCenter",
      name: "AuditCenter",
      component: () => import("@/views/auditCenter/index.vue"),
      meta: {
        title: "首页",
        noCache: true // 审核中心
      }
    },
    {
      path: "eventDetails",
      name: "EventDetails",
      component: () => import("@/views/eventDetails/index.vue"),
      meta: {
        title: "事件详情", // 审核中心
        level: 2,
        noCache: true
      }
    },
    {
      path: "todayFocus",
      name: "TodayFocus",
      component: () => import("@/views/todayFocus/index.vue"),
      meta: {
        title: "事件列表", // 审核中心
        level: 2,
        noCache: true
      }
    },
    {
      path: "monitors",
      name: "Monitors",
      component: () => import("@/views/monitors/index.vue"),
      meta: {
        title: "首页", // 审核中心
        level: 2,
        noCache: true
      }
    },

    {
      path: "attention",
      name: "Attention",
      component: () => import("@/views/attention/index.vue"),
      meta: {
        title: "首页",
        noCache: true // 审核中心
      }
    },
    {
      path: "keyEvents",
      name: "KeyEvents",
      component: () => import("@/views/keyEvents/index.vue"),
      meta: {
        title: "事件列表",
      }
    },
    {
      path: "keyEventsDetail",
      name: "KeyEventsDetail",
      component: () => import("@/views/keyEventsDetail/index.vue"),
      meta: {
        title: "事件详情", // 审核中心
        level: 2,
        noCache: true
      }
    }
  ]
}];

export default routes;