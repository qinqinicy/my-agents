import {
  http
} from "@/utils/http";

/**
 * 获取用户信息
 * @param {*} data 
 * @returns 
 */
export function getUserInfo(data) {
  return http.request({
    url: "/event/getUserInfo",
    method: "post",
    data
  });
}