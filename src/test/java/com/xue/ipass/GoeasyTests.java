package com.xue.ipass;

import com.alibaba.fastjson.JSON;
import io.goeasy.GoEasy;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class GoeasyTests {

    @Test
    public void testquery() {

        //配置发送休息的必要参数 参数：regionHost.服务器地址。自己的appkey
        GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io", "BC-6a39baaf409a4aa386c0f99e311248f7");

        //配置发送消息 参数：管道名称(自定义) 发送内容
        goEasy.publish("185-yingxChannel", "Hello,185 GoEasy!");
    }

    @Test
    public void testGoEasyUser() {

        //添加用户

        for (int i = 0; i < 10; i++) {

            Random random = new Random();
            //获取随机数 参数50 0<=i<50
            //int i = random.nextInt(50);
            HashMap<String,Object> map = new HashMap<>();

            //根据月份 性别 查询数据  查询用户信息
            map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
            map.put("boys", Arrays.asList(random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500)));
            map.put("girls", Arrays.asList(random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500),random.nextInt(500)));

            //将对象转为json格式字符串
            String content = JSON.toJSONString(map);

            //配置发送休息的必要参数 参数：regionHost.服务器地址。自己的appkey
            GoEasy goEasy = new GoEasy("http://rest-hangzhou.goeasy.io", "BC-6a39baaf409a4aa386c0f99e311248f7");

            //配置发送消息 参数：管道名称(自定义) 发送内容
            goEasy.publish("185-yingxChannel", content);

            try {
                Thread.sleep(100);//设置时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}