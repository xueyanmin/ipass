package com.xue.ipass;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.Test;



/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/

    /**新版短信验证码*/
    public class SendSms {


        public static  void sendMsg() {

            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FnYoxDLFi526DFNJELf", "uIgnjo3nbcSzkGp5yl8y6WZJwZHbkj");
            IAcsClient client = new DefaultAcsClient(profile);

            CommonRequest request = new CommonRequest();
            request.setMethod(MethodType.POST);
            request.setDomain("dysmsapi.aliyuncs.com");
            request.setVersion("2017-05-25");
            request.setAction("SendSms");
            request.putQueryParameter("RegionId", "cn-hangzhou");
            request.putQueryParameter("PhoneNumbers", "13753852236,15721642242");
            request.putQueryParameter("SignName", "小山村");
            request.putQueryParameter("TemplateCode", "SMS_187225819");
            request.putQueryParameter("TemplateParam", "{\"code\":\"123456\"}");
            try {
                CommonResponse response = client.getCommonResponse(request);
                System.out.println(response.getData());
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }


        public static void main(String[] args) {
            sendMsg();
        }
    }

