import {
  defineMock
} from "vite-plugin-mock-dev-server";
import Mock from "mockjs";

export default defineMock([{
    url: "/dev-api/list/get",
    delay: 500,
    body: {
      code: 200,
      message: "OK",
      result: Mock.mock({
        "list|10": [{
          "id|+1": 1
        }]
      })
    }
  },
  {
    url: "/dev-api/list/error",
    delay: 500,
    body: {
      code: 40010,
      message: "ERROR",
      result: null
    }
  },
  {
    url: "dev-api/event/getUserInfo",
    delay: 500,
    body: {
      "code": 0,
      "msg": "success",
      "result": {
        "result": {
          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLluoTlt6johb4iLCJpc3MiOiJ6bnYiLCJlbnYiOiJoZCIsInVzZXJOYW1lIjoi5bqE5beo6IW-IiwibG9naW5LaW5kIjowLCJleHAiOjE3Mjc0MzEwMjEsInVzZXJJZCI6IjQ3OTY5NGU5NDA4NjAyZWUzZTQ3ZGI3NzZhOWRiMTNjIiwiaWF0IjoxNzI3NDIzODIxfQ.mqSSTqddGjZhtLrZub_NZwQmSP_LfeuzRqBgPG01mkc",
          "userId": "d7b2465b2b624429ab214098a2d923a7",
          "userName": "吴锡涛",
          "departmentName": "南山区/区直机关、事业单位/区委政法委/信息化科",
          "departmentCode": "440305-003-015-004",
          "roleList": [{
              "userId": "d7b2465b2b624429ab214098a2d923a7",
              "roleId": "36",
              "roleCode": "HaiZhiAdmin",
              "roleName": "决策分析管理员"
            },
            {
              "userId": "d7b2465b2b624429ab214098a2d923a7",
              "roleId": "68",
              "roleCode": "JCFXQzhengfawei",
              "roleName": "【决策分析】区政法委"
            }
          ],
          "roleDataList": [{
              "precinctCode": "440305",
              "precinctName": "南山区"
            },
            {
              "precinctCode": "440305",
              "precinctName": "南山区"
            }
          ],
          "exp": 0
        }
      }
    }
  },
  {
    url: "dev-api/event/a1",
    delay: 500,
    body: {
      "code": 0,
      "msg": "success",
      result:{
        result: {
          events: [
            {
              id: 18,
              name: "食药市监/消费维权/教育机构消费维权（含预付款消费）",
              eventNumber: "3724011815510822501",
              appearTime: "2024-02-27 11:15:56",
              subjectAddress: null,
              handleStatus: "1",
              eventSource: null,
              incidentArea: "南山区",
              appealSource: null,
              workOrderType: null,
              eventClass: null,
              currentDisposalDep: null,
              circulationLink: null,
              takeAddress: "松白路5078锴龙大厦",
              subjectInvolved: null,
              takePlace: "松白路5078锴龙大厦",
              eventContent: null,
              supplementaryInformation: null,
              community: null,
              communityCode: null,
              dangerLevel: null,
              lable: "综治事件",
              pushTime: null,
              examineStatus: "1"
            },
            {
              id: 20,
              name: "文体旅游/旅游管理/景区管理",
              eventNumber: "3723050309410168801",
              appearTime: "2023-06-14 18:19:18",
              subjectAddress: null,
              handleStatus: "1",
              eventSource: null,
              incidentArea: "南山区",
              appealSource: null,
              workOrderType: null,
              eventClass: null,
              currentDisposalDep: null,
              circulationLink: null,
              takeAddress: "福前路10号福前路一巷-7号楼",
              subjectInvolved: null,
              takePlace: "福前路10号福前路一巷-7号楼",
              eventContent: null,
              supplementaryInformation: null,
              community: null,
              communityCode: null,
              dangerLevel: null,
              lable: "综治事件",
              pushTime: null,
              examineStatus: "1"
            },
            {
              id: 17,
              name: "综治信访维稳/关于单用途商业预付卡的投诉/零售业、住宿和餐饮业、居民服务业单用途商业预付卡消费纠纷",
              eventNumber: "3724060620501894701",
              appearTime: "2023-12-14 17:20:30",
              subjectAddress: null,
              handleStatus: "0",
              eventSource: null,
              incidentArea: "南山区",
              appealSource: null,
              workOrderType: null,
              eventClass: null,
              currentDisposalDep: null,
              circulationLink: null,
              takeAddress: "芳园路",
              subjectInvolved: null,
              takePlace: "芳园路",
              eventContent: null,
              supplementaryInformation: null,
              community: null,
              communityCode: null,
              dangerLevel: null,
              lable: "维稳事件",
              pushTime: null,
              examineStatus: "1"
            },
            {
              id: 19,
              name: "交通运输/道路管理/非市政道路养护、维修、管理",
              eventNumber: "3724031809524899101",
              appearTime: "2023-03-06 16:34:26",
              subjectAddress: null,
              handleStatus: "1",
              eventSource: null,
              incidentArea: "南山区",
              appealSource: null,
              workOrderType: null,
              eventClass: null,
              currentDisposalDep: null,
              circulationLink: null,
              takeAddress: "广东省深圳市南光高速下白芒路路口该条道路上",
              subjectInvolved: null,
              takePlace: "广东省深圳市南光高速下白芒路路口该条道路上",
              eventContent: null,
              supplementaryInformation: null,
              community: null,
              communityCode: null,
              dangerLevel: null,
              lable: "市通报",
              pushTime: null,
              examineStatus: "1"
            }
          ]
        }
      }
    }
  }
]);