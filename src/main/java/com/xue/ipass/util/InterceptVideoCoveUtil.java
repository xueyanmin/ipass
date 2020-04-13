package com.xue.ipass.util;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
/*import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;*/
/*import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;*/

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
*
*         <!--截取视频第一帧-->
        <dependency>
            <groupId>org.bytedeco</groupId>
            <artifactId>javacpp-presets</artifactId>
            <version>1.4.3</version>
        </dependency>
        <dependency>
            <groupId>org.openpnp</groupId>
            <artifactId>opencv</artifactId>
            <version>3.4.2-1</version>
        </dependency>
        <dependency>
            <groupId>org.bytedeco</groupId>
            <artifactId>javacpp</artifactId>
            <version>1.4.3</version>
        </dependency>
        <dependency>
            <groupId>org.bytedeco</groupId>
            <artifactId>javacv-platform</artifactId>
            <version>1.4.3</version>
        </dependency>
* -截取视频第一帧测试项目*/

/**/
public  class InterceptVideoCoveUtil {
/*

    */
/**
     * 获取指定视频的帧并保存为图片至指定目录
     * @param filePath 视频存放的地址
     * @param targerFilePath 截图存放的地址
     * @param targetFileName 截图保存的文件名称
     * @return
     * @throws Exception
     *//*

   public static boolean executeCodecs(String filePath, String targerFilePath, String targetFileName) throws Exception {
        try{
            FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
            ff.start();
            String rotate =ff.getVideoMetadata("rotate");
            Frame f;
            int i = 0;
            while (i <1) {
                f =ff.grabImage();
                opencv_core.IplImage src = null;
                if(null !=rotate &&rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter =new OpenCVFrameConverter.ToIplImage();
                    src =converter.convert(f);
                    f =converter.convert(rotate(src, Integer.valueOf(rotate)));
                }
                doExecuteFrame(f,targerFilePath,targetFileName);
                i++;
            }
            ff.stop();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


     */
/*旋转角度的*//*


    public static opencv_core.IplImage rotate(opencv_core.IplImage src, int angle) {
        opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, angle);
        return img;
    }


    public static void doExecuteFrame(Frame f, String targerFilePath, String targetFileName) {

        if (null ==f ||null ==f.image) {
            return;
        }
        Java2DFrameConverter converter =new Java2DFrameConverter();
        String imageMat ="jpg";
        String FileName =targerFilePath + File.separator +targetFileName +"." +imageMat;
        BufferedImage bi =converter.getBufferedImage(f);
        System.out.println("width:" + bi.getWidth());//打印宽、高
        System.out.println("height:" + bi.getHeight());
        File output =new File(FileName);
        try {
            ImageIO.write(bi,imageMat,output);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    */
/*视频截取第一帧*//*

    public static void main(String[] args) {

    */
/**@param filePath 视频存放的地址
     * @param targerFilePath 截图存放的地址
     * @param targetFileName 截图保存的文件名称*//*


    String filePath="C:\\Users\\lm132\\Pictures\\video\\小品.mp4";
    String targerFilePath="C:\\Users\\lm132\\Pictures\\video";
    String targetFileName="小品";
        try {
            //截取
            executeCodecs(filePath,targerFilePath,targetFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

}
