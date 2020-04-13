package com.xue.ipass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "yx_category")
@Data
@NoArgsConstructor
@AllArgsConstructor

/**implements Serializable:实现序列化*/
public class Category implements Serializable {
    @Id
    private String id;

    @Column(name= "cate_name")
    private String cateName;


    private String levels;

    @Column(name ="parent_id")
    private String parentId;
}