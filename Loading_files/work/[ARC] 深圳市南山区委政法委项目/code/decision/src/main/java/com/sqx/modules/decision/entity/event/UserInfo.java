package com.sqx.modules.decision.entity.event;

import lombok.*;

import java.util.*;

@Data
public class UserInfo {

    private String token;

    private String userId;

    private String userName;

    private String phone;

    private String departmentName;

    private String departmentCode;


    private List<DataInfo> dataList;

    private List<UserRole> roleList;

    private List<RoleData> roleDataList;

    private long exp;

}
