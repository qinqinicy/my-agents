package com.sqx.modules.decision.dao;

import com.baomidou.mybatisplus.core.mapper.*;
import com.sqx.modules.decision.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

@Mapper
public interface AttachmentInfoMapper extends BaseMapper<AttachmentInfo> {

    List<AttachmentInfo> selectAttachmentUnHandle();

}