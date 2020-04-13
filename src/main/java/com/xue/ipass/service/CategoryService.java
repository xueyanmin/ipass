package com.xue.ipass.service;

import com.xue.ipass.entity.Category;

import java.util.HashMap;

public interface CategoryService {


    //查询一级类别
    HashMap<String,Object>queryByOnePage(Integer page,Integer rows);

    //查询一级类别对应的二级类别
    HashMap<String,Object> queryByTwoPage(Integer page,Integer rows,String parentId);

    /*一级类别与二级类别添加*/
    public void add(Category category);

    /*一级类别与二级类别修改*/
    public void update(Category category);

    /*一级类别与二级类别删除*/
    HashMap<String ,Object> delete(Category category);
}
