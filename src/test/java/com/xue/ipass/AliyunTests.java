package com.xue.ipass;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class AliyunTests {

    String endpoint = "http://oss-cn-beijing.aliyuncs.com";

    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    String accessKeyId = "LTAI4FnYoxDLFi526DFNJELf";
    String accessKeySecret = "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj";

   /**以下代码用于创建存储空间*/
   @Test
    public void createBucket(){

       // Endpoint以杭州为例，其它Region请按实际情况填写。
       String endpoint = "http://oss-cn-beijing.aliyuncs.com";
       // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
       String accessKeyId = "LTAI4FnYoxDLFi526DFNJELf";
       String accessKeySecret = "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj";
       String bucketName = "yingx-asasasa";

       // 创建OSSClient实例。
       OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

       // 创建存储空间。
       ossClient.createBucket(bucketName);

       // 关闭OSSClient。
       ossClient.shutdown();
   }

   /**以下代码用于上传文件到阿里云*/
    @Test
    public void uploadFile() {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FnYoxDLFi526DFNJELf";
        String accessKeySecret = "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj";

        String bucket="yingx-185";  //存储空间名
        String fileName="小红车.mp4"; //指定上传文件名 可以指定上传文件目录  photo/
        String LocalFile="C:\\Users\\lm132\\Pictures\\video\\小红车.mp4";  //指定本地文件路径

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, new File(LocalFile));

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
       // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

         // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**以下代码用于列举所有的存储空间*/
    @Test
    public void queryBucket(){

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
       // String accessKeyId = "<yourAccessKeyId>";
        //String accessKeySecret = "<yourAccessKeySecret>";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 列举存储空间。
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    @Test
    public void download(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        //String accessKeyId = "<yourAccessKeyId>";
        //String accessKeySecret = "<yourAccessKeySecret>";
        String bucketName = "yingx-185";
        String objectName = "金色小猫.jpg";
        String LocalFile="C:\\Users\\lm132\\Pictures\\Saved Pictures"+objectName;  //指定本地文件路径

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

         // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(LocalFile));

        // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**以下代码用于删除阿里云数据操作*/
    @Test
    public void delete(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        //String accessKeyId = "<yourAccessKeyId>";
        // String accessKeySecret = "<yourAccessKeySecret>";
        String bucketName = "yingx-185";
        String objectName = "金色小猫.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

           // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName);

       // 关闭OSSClient。
        ossClient.shutdown();

    }

    /**以下代码用于上传文件流*/
    @Test
    public void test() throws FileNotFoundException {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
       //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
       // String accessKeyId = "<yourAccessKeyId>";
        //String accessKeySecret = "<yourAccessKeySecret>";

        String bucket="yingx-185";  //存储空间名
        String fileName="红车.mp4"; //指定上传文件名 可以指定上传文件目录  photo/
        String LocalFile="C:\\Users\\lm132\\Pictures\\video\\小红车.mp4";  //指定本地文件路径

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = new FileInputStream(LocalFile);
        ossClient.putObject(bucket, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    /**视屏截取第一帧封面*/
    @Test
    public void testVideoIntercept(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        //String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        //String accessKeyId = "<yourAccessKeyId>";
        //String accessKeySecret = "<yourAccessKeySecret>";
        String bucketName = "yingx-185";
        String objectName = "video/1585920744327-小红车.mp4";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 设置视频截帧操作。
        String style = "video/snapshot,t_1000,f_jpg,w_0,h_0";

        // 指定过期时间为10分钟。
        Date expiration = new Date(new Date().getTime() + 1000 * 60 * 10 );
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucketName, objectName, HttpMethod.GET);
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
            ossClient.putObject(bucketName, "aaa.jpg", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭OSSClient。
        ossClient.shutdown();
        //sClient.shutdown();
    }

}
