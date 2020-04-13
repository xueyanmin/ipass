package com.xue.ipass.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

/**图片导出导入*/
public class Photo {


    @Excel(name = "ID")
    private String id;
    @Excel(name = "头像",type = 2)
    private String cover;
    @Excel(name = "年龄")
    private Integer age;
    @Excel(name = "生日",format = "yyyy-MM-dd",width = 20,height = 20)
    private Date bir;
}
