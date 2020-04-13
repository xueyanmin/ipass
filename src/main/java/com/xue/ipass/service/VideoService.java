package com.xue.ipass.service;

import com.xue.ipass.entity.User;
import com.xue.ipass.entity.Video;
import com.xue.ipass.po.VideoPo;
import com.xue.ipass.vo.VideoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public interface VideoService {

    //分页查询
    HashMap<String, Object> queryByPage(Integer page, Integer rows);

    //添加
     String add(Video video);

    //视屏上传至阿里云
    void uploadVideo(MultipartFile path, String id, HttpServletRequest request);

    void uploadVideos(MultipartFile path, String id, HttpServletRequest request);

    /*上传至阿里云删除操作*/
    HashMap<String,Object> delete(Video video);

    /*上传至阿里云修改操作*/
    void update(Video video);

    List<VideoVo> queryByReleaseTime();

    /**高亮操作数据*/
    List<Video> querySearch(String content);

    /**高亮操作数据*/
    List<Video> querySearchs(String content);

}
