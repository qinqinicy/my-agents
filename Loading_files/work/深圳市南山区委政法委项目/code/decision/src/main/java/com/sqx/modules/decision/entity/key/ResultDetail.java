package com.sqx.modules.decision.entity.key;

import java.util.*;

@lombok.Data
public class ResultDetail {

    private Integer total_size;
    private List<String> file_list;
    private List<List<String>> values;
}
