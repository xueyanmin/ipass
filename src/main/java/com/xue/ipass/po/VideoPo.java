package com.xue.ipass.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoPo {

    private String id;
    private String vTitle;
    private String vBrief;
    private String vPath;
    private String vCover;
    private Date vPublishDate;

    private String cateName;

    private String headImg;

}
