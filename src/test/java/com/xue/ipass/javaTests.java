package com.xue.ipass;

import com.xue.ipass.dao.VideoMapper;
import com.xue.ipass.po.VideoPo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class javaTests {

    @Resource
    VideoMapper videoMapper;

    @Test
    public void testqueryss(){
        List<VideoPo> videoPos = videoMapper.queryByReleaseTime();
        videoPos.forEach(videoPo -> System.out.println(videoPo));
    }
}
