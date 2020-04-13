package com.xue.ipass.controller;

import com.xue.ipass.entity.Video;
import com.xue.ipass.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("video")
public class VideoController {

  @Resource
  VideoService videoService;

  /**全表分页查询*/
  @RequestMapping("queryByPage")
  @ResponseBody
  public HashMap<String,Object> queryByPage(Integer page,Integer rows){

      HashMap<String, Object> map = videoService.queryByPage(page, rows);

      return map;
  }

  /**增删改操作*/
  @ResponseBody
  @RequestMapping("edit")
  public Object edit(Video video, String oper) {

      String uid = null;
      if (oper.equals("edit")) {
          videoService.update(video);

      }
      if (oper.equals("add")) {
          System.out.println(video);
          uid = videoService.add(video);

      }
      if (oper.equals("del")) {
          HashMap<String, Object> map = videoService.delete(video);
          return map;

      }

      return uid;
  }

    /**文件上传*/
    @ResponseBody
    @RequestMapping("uploadVideo")
    public void uploadVideos(MultipartFile path, String id, HttpServletRequest request) {
        videoService.uploadVideos(path, id, request);
    }

    /**视频检索操作高亮*/
    @ResponseBody
    @RequestMapping("querySearch")
    public List<Video> querySearch(String content) {
        //调用业务层方法
        List<Video> videos = videoService.querySearchs(content);
        return videos;
    }
}
