package com.xue.ipass.app;

import com.xue.ipass.common.CommonResult;
import com.xue.ipass.service.VideoService;
import com.xue.ipass.util.AliyunSendPhoneUtil;
import com.xue.ipass.vo.VideoVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("app")
public class AppInterfaceController {

    @Resource
    VideoService videoService;

    /** 1:发送短信验证码接口*//*
    @RequestMapping("getPhoneCode")
    public HashMap<String, Object> getPhoneCode(String phone){

        HashMap<String, Object> map = new HashMap<>();

        //生成随机数
            String random = AliyunSendPhoneUtil.getRandom(6);
            System.out.println("存储验证码："+random);
            //发送验证码
            String message = AliyunSendPhoneUtil.sendCode(phone, random);
            System.out.println("验证码发送："+message);

            if (message.equals("发送成功")){
                map.put("status",100);
                map.put("message","发送成功");
                map.put("data",phone);
            }else {
                map.put("status",104);
                map.put("message","发送失败"+message);
                map.put("data",null);
            }
        return map;
    }*/

    /**
     * 2:发送短信验证码接口
     */
    @RequestMapping("getPhoneCode")
    public CommonResult getPhoneCode(String phone) {

        HashMap<String, Object> map = new HashMap<>();

        //生成随机数
        String random = AliyunSendPhoneUtil.getRandom(6);
        System.out.println("存储验证码：" + random);
        //发送验证码
        String message = AliyunSendPhoneUtil.sendCode(phone, random);
        System.out.println("验证码发送：" + message);

        if (message.equals("发送成功")) {
            return new CommonResult().success("100", "发送成功", phone);

        } else {
            return new CommonResult().failed("100", "发送失败" + message, null);

        }
    }

    /**首页查询视频信息接口*/
    /*@RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime() {
        //查询数据
        try {
            List<VideoVo> videoVos = videoService.queryByReleaseTime();

            return new CommonResult().success(videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }*/
    @RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime(){
        try {
            //查询数据
            List<VideoVo> videoVos = videoService.queryByReleaseTime();
            return new CommonResult().success(videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }

}

