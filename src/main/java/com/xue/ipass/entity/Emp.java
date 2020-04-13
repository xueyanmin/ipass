package com.xue.ipass.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.Date;

@Document(indexName = "yingxs",type = "emp")
@Data
@NoArgsConstructor
@AllArgsConstructor

/**单表数据导出导入测试*/
public class Emp {

    @Id
    @Excel(name = "ID")
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    @Excel(name = "名字")
    private String name;

    @Field(type = FieldType.Integer)
    @Excel(name = "年龄")
    private Integer age;

    @Field(type = FieldType.Date)
    @Excel(name = "生日",format = "yyyy-MM-dd",width = 20,height = 20)
    private Date bir;
 }
