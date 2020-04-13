package com.xue.ipass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    private String id;
    private String adminName;
    private Date date;
    private String operation;
    private String status;
}
