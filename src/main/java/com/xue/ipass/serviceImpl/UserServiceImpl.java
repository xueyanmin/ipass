package com.xue.ipass.serviceImpl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.xue.ipass.annotation.AddCache;
import com.xue.ipass.annotation.AddLog;
import com.xue.ipass.annotation.DelCache;
import com.xue.ipass.dao.UserMapper;
import com.xue.ipass.entity.User;
import com.xue.ipass.entity.UserExample;
import com.xue.ipass.service.UserService;
import com.xue.ipass.util.AliyunOssUtil;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @AddCache
    /**分页工具栏查询用户*/
    @Override
    public HashMap<String, Object> queryByPage(Integer page, Integer rows) {

        HashMap<String, Object> map = new HashMap<>();

        /*封装数据*/
        //总条数 records
        UserExample example = new UserExample();

        Integer records = userMapper.selectCountByExample(example);
        map.put("records",records);

        //总页数 total  总条数/每页展示条数 是否有余数
        Integer total = records % rows==0? records/rows:records/rows+1;
        map.put("total",total);

        //当前页 page
        map.put("page",page);


        //数据 rows
        //rowBounds需要传递参数 忽略条数 获取条数 //offset:偏移量
        RowBounds rowBounds = new RowBounds((page-1)*rows,rows);
        List<User> users = userMapper.selectByRowBounds(new User(), rowBounds);
        map.put("rows",users);
        System.out.println("业务层map:="+map);
        return map;
    }

    @Override
    public String add(User user) {
        String uid = UUID.randomUUID().toString();
        user.setId(uid);
        user.setStatus("1");
        user.setCreateDate(new Date());
     userMapper.insert(user);

     return uid;
    }

    //添加操作修给图片路径
    @Override
    public void uploadUser(MultipartFile headImg, String id, HttpServletRequest request) {

        /*根据相对路径获取绝对路径*/
        String realPath = request.getSession().getServletContext().getRealPath("/upload/photo");
        /*判断地址*/
        File file = new File(realPath);
        if (!file.exists()){
            /*地址不存在就创建*/
            file.mkdirs();
        }
        /*根据地址获取文件名*/
        String filename = headImg.getOriginalFilename();
        System.out.println("图片文件名："+filename);
        /*为文件名拼错*/
        String newName = new Date().getTime()+"-"+filename;
        System.out.println("拼接——后的文件名"+newName);

        try {
            /*上传文件*/
            headImg.transferTo(new File(realPath,newName));

            /*修改图片*/
            UserExample example = new UserExample();
            System.out.println("这个是-------？？"+example);
            /*修改条件*/
            example.createCriteria().andIdEqualTo(id);
            /*修改设置*/
            User user = new User();
            user.setHeadImg(newName);
            /*将修改后的图片上传*/
            System.out.println("修改后的地址："+user+"没修改的地址"+example);
            userMapper.updateByExampleSelective(user,example);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @DelCache
    @Override
    public void update(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delete(User user) {
        userMapper.delete(user);
    }

    @Override
    public void uploadUserAliyun(MultipartFile headImg, String id, HttpServletRequest request) {

        //将文件转为byte[]数组
        byte[] bytes =null;
        try {
           bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件名
        String filename = headImg.getOriginalFilename();
        String newName = new Date().getTime()+"-"+filename;


        //1:将文件上传至阿里云

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FnYoxDLFi526DFNJELf";
        String accessKeySecret = "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj";
        String bucket="yingx-185";  //存储空间名
        String fileName=newName; //指定上传文件名 可以指定上传文件目录  photo/



        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();

        //图片信息的修改
        /*修改的条件*/
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(id);

        /*修改设置*/
        User user = new User();
        //user.setHeadImg(newName);//设置本地路径修给结果
        user.setHeadImg("http://yingx-185.oss-cn-beijing.aliyuncs.com/"+newName);//设置网络路径修给结果
        //http://yingx-185.oss-cn-beijing.aliyuncs.com/1585738285693-6.jpg

        //修给结果
        userMapper.updateByExampleSelective(user,example); //本地路径修给喝过

    }

    @Override
    public void uploadUserAliyuns(MultipartFile headImg, String id, HttpServletRequest request) {

        //获取文件名
        String filename = headImg.getOriginalFilename();
        String newName = new Date().getTime()+"-"+filename;

        //1:将文件上传至阿里云
        AliyunOssUtil.uploadFileBytes("yingx-185",headImg,newName);

        //2:图片信息的修改
        /*修改的条件*/
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo(id);

        /*修改设置*/
        User user = new User();
        user.setHeadImg(newName);
        //修给结果
        userMapper.updateByExampleSelective(user,example);

    }

}
