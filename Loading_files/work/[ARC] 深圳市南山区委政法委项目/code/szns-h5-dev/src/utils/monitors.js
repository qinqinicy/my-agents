export const detailList = [{
  title: "民生诉求",
  list: [{
      label: "近一个月满意度",
      value: "0%",
      unit: "",
      detail: "",
      isError: false,
      describe: "近一个月满意度低于90%"
    },
    {
      label: "近一个月突出投诉人",
      value: "0",
      unit: "件",
      detail: "",
      isError: false,
      describe: "近一个月投诉次数超过20次的人"
    },
    {
      label: "近一个月突出投诉主体",
      value: "0",
      unit: "件",
      detail: "",
      isError: false,
      describe: "近一个月被投诉次数超过20次的公司主体",
      url: 'https://rioweb.szns.gov.cn/pans/fxdp_online/preview/index.html?_t=1728529056597343#/screen/share/1b46a752ec801e6937484e555e5f3cfc?loading=false&userId=808cc794c01b11eea21f024226f1592f'
    },
    {
      label: "月度事件量态势",
      value: "0%",
      unit: "",
      detail: "",
      isError: false,
      describe: "上个月事件量环比上上个月增加15%"
    }
  ]
},{
    title: "维稳事件",
    list: [{
        label: "近一个月重复",
        value: "0",
        unit: "件",
        detail: "",
        isError: true,
        describe: "近一个月维稳次数超过2次的事件，当超过2次时，每预警一次，风险提醒一次"
      },
      {
        label: "近一个月满意度",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月事件量环比上上个月增加20%"
      }
    ]
  },
  {
    title: "重点事件",
    list: [{
      label: "月度事件量态势",
      value: "0%",
      unit: "",
      detail: "",
      isError: false,
      describe: "上个月事件量环比上上个月增加20%"
    }]
  },
  {
    title: "矛盾纠纷多元化解",
    list: [{
        label: "近一个月突出当事人",
        value: "0",
        unit: "件",
        detail: "",
        isError: false,
        describe: "近一个月发生过矛盾纠纷超过5次的人",
      url: 'https://rioweb.szns.gov.cn/pans/fxdp_online/preview/index.html?_t=172982187499229#/screen/share/a49b10a16b5178d66a1f53fa6c371108?loading=false&userId=808cc794c01b11eea21f024226f1592f'
      },
      {
        label: "近一个月突出当事主体",
        value: "0",
        unit: "件",
        detail: "",
        isError: false,
        describe: "近一个月发生过矛盾纠纷超过10次的公司主体"
      },
      {
        label: "月度突出领域",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月矛盾纠纷事件量增加20%的领域"
      },
      {
        label: "月度事件量态势",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月事件量环比上上个月增加10%"
      }
    ]
  },
  {
    title: "重点人监测",
    list: [{
        label: "近一个月新增量",
        value: "0",
        unit: "人",
        detail: "",
        isError: false,
        describe: "近一个月重点人新增量超过5个"
      },
      {
        label: "未纳管量",
        value: "0",
        unit: "人",
        detail: "",
        isError: false,
        describe: "当前重点人中未纳管的重点关注人员"
      },
      {
        label: "月度未按时走访",
        value: "0",
        unit: "人",
        detail: "",
        isError: false,
        describe: "上个月存在未走访的重点关注人员"
      },
      {
        label: "重点人涉事",
        value: "0",
        unit: "人",
        detail: "",
        isError: false,
        describe: "重点人涉矛盾纠纷、维稳、重点事"
      }
    ]
  },
  {
    title: "人口监测",
    list: [{
        label: "月度人口流入率",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月人口流入率超过10%"
      },
      {
        label: "月度人口流出率",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月人口流出率超过10%"
      },
      {
        label: "月度人口未核查率",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月人口未核查率超过1%",
      },
      {
        label: "月度采集准确率",
        value: "0%",
        unit: "",
        detail: "",
        isError: false,
        describe: "上个月采集准确率低于95%",
      }
    ]
  }
];

export function monitorsReverseFilter(arr) {
  const updatedArr = detailList.map(item => {
    // 复制 item 对象以避免直接修改原始对象  
    const newItem = {
      ...item
    };

    // 更新 newItem.list  
    newItem.list = newItem.list.map(item2 => {
      // 查找 detailList 中是否有匹配的 title  
      const detailItem = arr.find(item1 => item1.title === item.title);
      if (detailItem) {
        // 查找 detailItem.list 中是否有匹配的 label  
        const updatedItem = detailItem.list.find(item3 => item3.label === item2.label);
        if (updatedItem) {
          console.log(updatedItem)
          // 如果有匹配的，使用新的对象替换旧的  
          return {
            ...item2,
            ...updatedItem
          };
        }
      }
      // 如果没有找到匹配的，则返回原始对象  
      return item2;
    });

    return newItem;
  });

  console.log(updatedArr); // 打印更新后的数组  
  return updatedArr; // 返回更新后的数组以便进一步使用  
}

export function monitorsFilter(arr) {
  const updatedArr = arr.map(item => {
    // 复制 item 对象以避免直接修改原始对象  
    const newItem = {
      ...item
    };

    // 更新 newItem.list  
    newItem.list = newItem.list.map(item2 => {
      // 查找 detailList 中是否有匹配的 title  
      const detailItem = detailList.find(item1 => item1.title === item.title);
      if (detailItem) {
        // 查找 detailItem.list 中是否有匹配的 label  
        const updatedItem = detailItem.list.find(item3 => item3.label === item2.label);
        if (updatedItem) {
          console.log(updatedItem)
          // 如果有匹配的，使用新的对象替换旧的  
          return {
            ...updatedItem,
            ...item2
          };
        }
      }
      // 如果没有找到匹配的，则返回原始对象  
      return item2;
    });

    return newItem;
  });

  return updatedArr; // 返回更新后的数组以便进一步使用  
}