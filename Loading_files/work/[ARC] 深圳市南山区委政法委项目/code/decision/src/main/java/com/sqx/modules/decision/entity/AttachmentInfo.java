package com.sqx.modules.decision.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data

public class AttachmentInfo {
    @Id
    private Integer id;

    @Column(name = "matter_uuid")
    private String matterUuid;

    @Column(name = "order_num")
    private String orderNum;

    @Column(name = "event_uuid")
    private String eventUuid;

    @Column(name = "attachment_id")
    private String attachmentId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_suffix")
    private String fileSuffix;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "current_file_url")
    private String currentFileUrl;



}