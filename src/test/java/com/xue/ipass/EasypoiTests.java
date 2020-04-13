package com.xue.ipass;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.xue.ipass.entity.Emp;
import com.xue.ipass.entity.Photo;
import com.xue.ipass.entity.Teacher;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EasypoiTests {

  @Test
  public void EasyPoiExport(){

      //数据查询 List<Object>
      ArrayList<Emp> emps = new ArrayList<>();
      emps.add(new Emp("1","小黑",12,new Date()));
      emps.add(new Emp("2","机机先生",12,new Date()));
      emps.add(new Emp("3","小蛋黄",12,new Date()));
      emps.add(new Emp("4","小黑s",12,new Date()));

      //导除的参数 参数：标题 工作表名
      ExportParams exportParams = new ExportParams("计算机一班学生", "学生");
      //生成Excel 导出 参数：1导出参数对象 2要导出的对象 3要导出的数据集合
      Workbook workbook = ExcelExportUtil.exportExcel(exportParams,Emp .class, emps);

      try {
          //导出
          workbook.write(new FileOutputStream(new File("D://185-poi.xls")));

          //释放资源
          workbook.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**多表数据导出测试*/
  @Test
  public void testExports(){

      //数据查询 List<Object>
      ArrayList<Emp> emps = new ArrayList<>();
      emps.add(new Emp("1","小黑",12,new Date()));
      emps.add(new Emp("2","机机先生",12,new Date()));
      emps.add(new Emp("3","小蛋黄",12,new Date()));
      emps.add(new Emp("4","小黑s",12,new Date()));


      Teacher teacher1 = new Teacher("1", "suns", 39, emps);
      Teacher teacher2 = new Teacher("2", "小明老师", 39, emps);

      //导出老师与学生信息表
      ArrayList<Teacher> teachersList = new ArrayList<>();
      teachersList.add(teacher1);
      teachersList.add(teacher2);

      //导除的参数 参数：标题 工作表名
      ExportParams exportParams = new ExportParams("计算机一班学生", "学生");

      //生成Excel 导出 参数：1导出参数对象 2要导出的对象 3要导出的数据集合
      Workbook workbook = ExcelExportUtil.exportExcel(exportParams,Teacher.class, teachersList);

      try {
          //导出
          workbook.write(new FileOutputStream(new File("D://185-poi.xls")));

          //释放资源
          workbook.close();
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    /**多表数据导入测试*/
  @Test
  public void testInport(){

      //配置导入相关参数
      ImportParams params = new ImportParams();
      params.setTitleRows(1);  //表格标题行数 默认0
      params.setHeadRows(2);   //表头所占行 表头行数,默认1

      try {
          FileInputStream fileInputStream = new FileInputStream(new File("D://185-poi.xls"));
          List<Teacher> list = ExcelImportUtil.importExcel(fileInputStream,Teacher.class, params);
          for (Teacher teacher : list) {
              System.out.println("----------教师数据："+teacher);

              List<Emp> empList = teacher.getEmpList();
              for (Emp emp : empList) {
                  System.out.println("---------学生数据："+emp);
              }
          }

      } catch (Exception e) {
          e.printStackTrace();
      }
  }

  /**图片导出*/
    @Test
    public void testExportPhoto(){


        //数据查询 List<Object>
        ArrayList<Photo> photos = new ArrayList<>();
        photos.add(new Photo("1","src/main/webapp/upload/photo/1585831748144-6.jpg",12,new Date()));
        photos.add(new Photo("2","C:\\Users\\lm132\\Pictures\\Saved Pictures\\6.jpg",12,new Date()));
        photos.add(new Photo("3","src/main/webapp/upload/photo/1585831748144-6.jpg",12,new Date()));
        photos.add(new Photo("4","src/main/webapp/upload/photo/1585898685683-汽车介绍.jpg",12,new Date()));



        //导出的参数 参数：标题 工作表名
        ExportParams exportParams = new ExportParams("应学APP用户据", "数据1");

        //生成Excel 导出 参数：1导出参数对象 2要导出的对象 3要导出的数据集合
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,Photo.class, photos);

        try {
            //导出
            workbook.write(new FileOutputStream(new File("D://185-user.xls")));

            //释放资源
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**图片导入测试*/
    @Test
    public void testInportUser(){

        //配置导入相关参数
        ImportParams params = new ImportParams();
        params.setTitleRows(1);  //表格标题行数 默认0
        params.setHeadRows(1);   //表头所占行 表头行数,默认1

        try {
            FileInputStream fileInputStream = new FileInputStream(new File("D://185-user.xls"));
            List<Photo> list = ExcelImportUtil.importExcel(fileInputStream,Photo.class, params);

            for (Photo photo : list) {
                System.out.println(photo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**图片导入测试*/
    @Test
    public void testInportUsers(){

        try {
            ImportParams params = new ImportParams();
            params.setNeedSave(true);
            params.setTitleRows(1);  //表格标题行数 默认0
            params.setHeadRows(1);   //表头所占行 表头行数,默认1

            FileInputStream fileInputStream = new FileInputStream(new File("D://185-user.xls"));

            List<Photo> result = ExcelImportUtil.importExcel(fileInputStream, Photo.class, params);
            for (int i = 0; i < result.size(); i++) {
                System.out.println(ReflectionToStringBuilder.toString(result.get(i)));
            }

           // Assert.assertTrue(result.size() == 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
     }
    }
