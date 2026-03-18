package com.sqx.modules.decision.dao;

import com.baomidou.mybatisplus.core.mapper.*;
import com.sqx.modules.decision.entity.*;
import com.sqx.modules.decision.entity.third.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    List<AppCodeInfo> selectAppCode(@Param("id") Integer id);

}