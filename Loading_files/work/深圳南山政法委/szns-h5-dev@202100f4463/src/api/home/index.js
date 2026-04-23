import {
  http
} from "@/utils/http";

/**
 * 关注概览
 * @param {*} data 
 * @returns 
 */
export function attentionSum(data) {
  return http.request({
    url: "/event/attentionSum",
    method: "post",
    data
  });
}

/**
 * 首页关注列表
 * @param {*} data 
 * @returns 
 */
export function todayEventAttention(data) {
  return http.request({
    url: "/event/todayEventAttention",
    method: "post",
    data
  });
}

/**
 * 首页关注列表
 * @param {*} data 
 * @returns 
 */
export function userNum(data) {
  return http.request({
    url: "/event/getUserNum",
    method: "post",
    data
  });
}

/**
 * 审核是否有新数据(是否显示红点)
 * @param {*} data 
 * @returns 
 */
export function hasNewEvent(data) {
  return http.request({
    url: "/event/hasNewEvent",
    method: "post",
    data
  });
}

/**
 * 重点人
 * @param {*} data 
 * @returns 
 */
export function keyUser(data) {
  return http.request({
    url: "/index/keyUser",
    method: "post",
    data
  });
}

/**
 * 事辖区动态事项首页统计
 * @param {*} data 
 * @returns 
 */
export function getEventTotal(data) {
  return http.request({
    url: "/index/getEventTotal",
    method: "post",
    data
  });
}