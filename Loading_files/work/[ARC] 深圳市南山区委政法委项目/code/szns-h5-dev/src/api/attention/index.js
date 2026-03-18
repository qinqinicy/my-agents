import {
  http
} from "@/utils/http";

/**
 * 我的批示列表
 * @param {*} data 
 * @returns 
 */
export function myCommentList(data) {
  return http.request({
    url: "/event/myCommentList",
    method: "post",
    data
  });
}
/**
 * 我的关注列表
 * @param {*} data 
 * @returns 
 */
export function myAttentionList(data) {
  return http.request({
    url: "/event/myAttentionList",
    method: "post",
    data
  });
}

/**
 * 领导批示
 * @param {*} data 
 * @returns 
 */
export function leaderCommentList(data) {
  return http.request({
    url: "/event/leaderCommentList",
    method: "post",
    data
  });
}