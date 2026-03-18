import {
  http
} from "@/utils/http";

export function eventDetail(data) {
  return http.request({
    url: "/event/eventDetail",
    method: "post",
    data
  });
}

/**
 * 事件审核操作
 * @param {*} data 
 * @returns 
 */
export function eventAuditHandle(data) {
  return http.request({
    url: "/event/eventAuditHandle",
    method: "post",
    data
  });
}


/**
 * 事件审核操作
 * @param {*} data 
 * @returns 
 */
export function attentionOrCancle(data) {
  return http.request({
    url: "/event/attentionOrCancle",
    method: "post",
    data
  });
}


/**
 * 部门反馈列表
 * @param {*} data 
 * @returns 
 */
export function receiveDpt(data) {
  return http.request({
    url: "/event/receiveDpt",
    method: "post",
    data
  });
}

/**
 * 保存时间反馈
 * @param {*} data 
 * @returns 
 */
export function saveEventComment(data) {
  return http.request({
    url: "/event/saveEventComment",
    method: "post",
    data
  });
}