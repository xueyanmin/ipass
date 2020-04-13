package com.xue.ipass.dao;


import com.xue.ipass.entity.Video;
import com.xue.ipass.po.VideoPo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface VideoMapper extends Mapper<Video> {

    List<VideoPo> queryByReleaseTime();

}