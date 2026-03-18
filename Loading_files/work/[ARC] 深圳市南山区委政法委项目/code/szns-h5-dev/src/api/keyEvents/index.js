import { http } from "@/utils/http";

export function eventList(data) {
  return http.request({
    url: "/index/eventList",
    method: "post",
    data
  });
}

/**
 * 事件分类统计
 * @param {*} data 
 * @returns 
 */
export function eventClassNum(data) {
  return http.request({
    url: "/index/eventClassNum",
    method: "post",
    data
  });
}

/**
 * 首页事件总量统计
 * @param {*} data 
 * @returns 
 */
export function eventTodayCal(data) {
  return http.request({
    url: "/index/eventTodayCal",
    method: "post",
    data
  });
}

/**
 * 事件下拉框
 * @param {*} data 
 * @returns 
 */
export function selectSelect(data) {
  return http.request({
    url: "/index/selectSelect",
    method: "post",
    data
  });
}

/**
 * 辖区动态调用第三方接口---事件列表
 * @param {*} data 
 * @returns 
 */
export function dynamicEventList(data) {
  return http.request({
    url: "/index/dynamicEventList",
    method: "post",
    data
  });
}

/**
 * 事件关注操作
 * @param {*} data 
 * @returns 
 */
export function attentionOrCancle(data) {
  return http.request({
    url: "/index/attentionOrCancle",
    method: "post",
    data
  });
}

/**
 * 辖区动态批示
 * @param {*} data 
 * @returns 
 */
export function saveDynamicEventComment(data) {
  return http.request({
    url: "/index/saveDynamicEventComment",
    method: "post",
    data
  });
}

/**
 * 辖区动态获取批示列表
 * @param {*} data 
 * @returns 
 */
export function commentListAttention(data) {
  return http.request({
    url: "/index/commentListAttention",
    method: "post",
    data
  });
}