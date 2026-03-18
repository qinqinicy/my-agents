package com.sqx.modules.decision.entity.event;

import lombok.*;

import java.util.*;

@Data
public class Department {

            private String parentId;
            private String departmentId;
            private String departmentName;
            private String departmentCode;
            private String departmentLevel;
            private String deptType;
            private String abbrName;
            private String parentCode;
            private String idLevel;
            private String status;
            private String district;
            private String districtName;
            private String provice;
            private String city;
            private String area;
            private String sort;
            private String createBy;
            private String remark;
            private String isAuthority;
            private List<Department> children;
}
