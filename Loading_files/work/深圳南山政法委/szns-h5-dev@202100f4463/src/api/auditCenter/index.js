import {
  http
} from "@/utils/http";
/** * 我的批示列表 * @param {*} params *
@returns */
export function eventAuditList(data) {
  return http.request({
    url: "/event/eventAuditList",
    method: "post",
    data
  });
}