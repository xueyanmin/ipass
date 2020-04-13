package com.xue.ipass.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoVo {

        //返回前台的数据
        private String id;
        private String videoTitle;
        private String description;
        private String path;
        private String cover;
        private Date uploadTime;
        private Integer likeCount;

        private String cateName;

        private String headImg;

    }
