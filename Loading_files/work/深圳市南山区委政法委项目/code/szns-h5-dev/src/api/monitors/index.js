import {
  http
} from "@/utils/http";

/**
 * 关注概览
 * @param {*} data 
 * @returns 
 */
export function monitorskey(data) {
  return http.request({
    url: "/index/key",
    method: "post",
    data
  });
}