export const orderStatusName = {
  "": '全部状态',
  '归档': '归档',
  '办结': '办结',
  '职能局处理': '职能局处理',
  '派单员审核': '派单员审核',
  '退单审核': '退单审核',
  '草稿': '草稿',
  '作废': '作废',
  '审批': '审批',
  '暂存': '暂存',
  '已完成': '已完成',
  '新建': '新建',
  '化解成功': '化解成功',
  '待受理': '待受理',
  '化解中': '化解中',
  '已登记': '已登记',
  '不予受理': '不予受理',
  '处理中': '处理中',
  '退回': '退回',
  '关闭工单': '关闭工单',
  '修改': '修改',
  '挂起': '挂起',
  '差评申诉': '差评申诉',
  '未锁定': '未锁定',
  '热线机构复核': '热线机构复核',
  '部门审核': '部门审核',
}

export const dataSource = { 
  "": '全部来源',

  '深圳市消防智能接处警系统':	'深圳市消防智能接处警系统',
  '市民生诉求':	'市民生诉求',
  'wgysb':	'网格员上报',
  '深圳市应急管理局数据共享服务系统':	'深圳市应急管理局数据共享服务系统',
  '妇幼关爱':	'妇幼关爱',
  '妇幼关爱-区自采自办':	'妇幼关爱-区自采自办',
  '区人民网':	'区人民网',
  '妇幼关爱市妇联来件':	'妇幼关爱-市妇联来件',
  '区妇联自采':	'区妇联自采',
  '党群G+':	'党群G+',
  '服务大厅':	'服务大厅',
  '公众上报':	'公众上报',
  '基层工作端':	'基层工作端',
  '其他':	'其他',
  '区诉求登记':	'区诉求登记',
  '平安南山':	'平安南山',
}
export const eventClassDefine= {
  '': '全部类型',
  '一般事件': '一般事件',
  '即采即办': '即采即办'
}
export const typeName= {
  '': '全部分类',
}

export const selectMap = {
  '维稳事件': ['streetCode', 'orderStatusName', 'typeName'],
  '重点事件': ['streetCode', 'orderStatusName', 'typeName'],
  '社会风险防范': ['streetCode', 'orderStatusName', 'typeName'],
  '网格上报': ['streetCode', 'orderStatusName','eventClassDefine', 'typeName'],
  '民意速办': ['streetCode', 'orderStatusName', 'dataSource', 'typeName'],

}

export function ObjectToArray(object) {
  return Object.keys(object).map(key => {
    return { text: object[key], value: key };
  })
}