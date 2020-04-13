package com.xue.ipass.util;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class AliyunOssUtil {

    private static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static String accessKeyId = "LTAI4FnYoxDLFi526DFNJELf";
    private static String accessKeySecret = "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj";

    private static String bucket="yingx-185";
    /**上传本地文件
     参数
     bucket    存储空间名
     fileName  指定文件名 可以指定上传目录 目录名/文件名
     LocalFile 指定本地文件路径
    * */
    public static void uploadFile(String bucket,String fileName,String LocalFilePath) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, new File(LocalFilePath));

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();

    }


    /**上传本地文件
     参数
     headImg  指定 MultipartFile 类型的文
     fileName  指定文件名 可以指定上传目录 目录名/文件名
     LocalFile 指定本地文件路径
    **/
  public static void uploadFileBytes(String bucket,MultipartFile headImg,String fileName) {

        //转为字节数组
        byte[] bytes = new byte[0];
        try {
            bytes = headImg.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 上传Byte数组。
        ossClient.putObject(bucket, fileName, new ByteArrayInputStream(bytes));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**删除阿里云文件
    * 参数：bucket：存储空间名
    *      fileName：文件名 目录名/文件名
    */
    public static void delete(String bucket,String fileName){

        //创建OSSClient实列
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //删除文件,如需删除文件夹,请将ObjectName设置为对应的文件夹。如果文件夹非空，则需要将文件夹下的作用Object删除后才能删除该文件夹
        ossClient.deleteObject(bucket, fileName);

        //关闭OSSClient
        ossClient.shutdown();
    }

    /**视频截取
     *参数：
     * bucket:存储空间名
     * fileName:文件名 目录名/文件名
     * */
    public void testVideoIntercept(String bucket,String fileName){

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_0,h_0";

        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);

        //输出图片网络路径
        System.out.println(signedUrl);

        //文件上传 阿里云



        // 关闭OSSClient。
        ossClient.shutdown();
        //sClient.shutdown();
    }


    /**视频截取 并上传至阿里云
     * 参数：
     *     buvket:存储空间名
     *     coverName:截取的封面名
     *    fileName:远程文件文件名 可以拼接目录/文件名
     */
    public static void  videoCoverIntercept(String bucket,String fileName,String coverName){

        //创建实列
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //设置视屏截取第一帧操作
        String style = "video/snapshot,t_1000,f_jpg,w_0,h_0";

        //指定过期时间为十分钟
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, fileName, HttpMethod.GET);
        req.setExpiration(expiration);
        req.setProcess(style);
        URL signedUrl = ossClient.generatePresignedUrl(req);

        //输出图片网络路径
        System.out.println(signedUrl);

        //文件上传 阿里云
        // 上传网络流。
        InputStream inputStream = null;
        try {
            inputStream = new URL(signedUrl.toString()).openStream();
            ossClient.putObject(bucket, coverName, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭OSSClient
        ossClient.shutdown();

    }

    /**调用测试方法*/
    public static void main(String[] args) {

        String bucket="yingx-185";  //存储空间名
        String fileName="红金宝车.mp4"; //指定上传文件名 可以指定上传文件目录  photo/
        String LocalFilePath="C:\\Users\\lm132\\Pictures\\video\\小红车.mp4";  //指定本地文件路径

        //调用测试方法
        uploadFile(bucket,fileName,LocalFilePath);
    }

}
