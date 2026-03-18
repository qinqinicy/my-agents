package com.sqx.modules.decision.entity;

import io.swagger.annotations.*;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Data
public class AssociatedEventInfo {

    private String eventClass;

    private String eventDes;

    private String date;

}