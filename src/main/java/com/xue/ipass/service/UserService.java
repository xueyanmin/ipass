package com.xue.ipass.service;

import com.xue.ipass.entity.User;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface UserService {

  //分页查询
  HashMap<String,Object> queryByPage(Integer page, Integer rows);

   public String add(User user);

   void uploadUser(MultipartFile headImg, String id, HttpServletRequest request);

   void update(User user);
   void delete(User user);

   //上传到阿里云
    void uploadUserAliyun(MultipartFile headImg, String id, HttpServletRequest request);

    //上传到阿里云s
    void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request);


}
