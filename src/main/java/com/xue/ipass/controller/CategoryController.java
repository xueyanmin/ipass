package com.xue.ipass.controller;

import com.xue.ipass.entity.Category;
import com.xue.ipass.service.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Resource
    CategoryService categoryService;

    @RequestMapping("queryByOnePage")
    public HashMap<String,Object>queryByOnePage(Integer page,Integer rows){
        HashMap<String, Object> map = categoryService.queryByOnePage(page, rows);

        return map;
    }

    @RequestMapping("queryByTwoPage")
    public HashMap<String,Object>queryByTwoPage(Integer page,Integer rows,String parentId){
        HashMap<String, Object> map = categoryService.queryByTwoPage(page, rows,parentId);

        return map;
    }

    @RequestMapping("edit")
    @ResponseBody
    public Object edit(Category category, String oper){
        String uid=null;

        if(oper.equals("add")){

            categoryService.add(category);
        }
        if(oper.equals("edit")){

            categoryService.update(category);
        }
        if(oper.equals("del")){

            HashMap<String, Object> map = categoryService.delete(category);
            System.out.println(map.get("message"));
            return map;

        }
        return null;
    }

}
