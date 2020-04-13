package com.xue.ipass.controller;

import com.xue.ipass.entity.City;
import com.xue.ipass.entity.Mondel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("echarts")
public class EchartsController {

    @RequestMapping("queryUserNum")
    public  HashMap<String,Object> queryUserNum(){

        //select city name,count(id) value from yx_user where sex = '女' GROUP BY city
        HashMap<String,Object> map = new HashMap<>();

        //根据月份 性别 查询数据
        map.put("month", Arrays.asList("1月","2月","3月","4月","5月","6月"));
        map.put("boys", Arrays.asList(100,210,330,50,450,600));
        map.put("girls", Arrays.asList(150,350,200,500,50,350));

        return map;
    }
    /*http 由页面发起 短连接
    * tcp*/

    @RequestMapping("queryUserMap")
    public ArrayList<Object> queryUserMap(){

        //select concat(month(create_date),'月'),count(id) from yx_user where sex='男' group by month(create_date)
        ArrayList<Object> list = new ArrayList<>();

        //根据性别分组查询 where=男 group by=cityName
        ArrayList<City> boysCities = new ArrayList<>();
        boysCities.add(new City("北京","500"));
        boysCities.add(new City("河南","100"));
        boysCities.add(new City("云南","300"));
        boysCities.add(new City("山西","500"));
        boysCities.add(new City("新疆","80"));
        boysCities.add(new City("美国","700"));
        boysCities.add(new City("东北","200"));


        Mondel boymondel = new Mondel("小男孩", boysCities);

        ArrayList<City> girlsCities = new ArrayList<>();
        girlsCities.add(new City("广西","500"));
        girlsCities.add(new City("广东","100"));
        girlsCities.add(new City("海南","300"));
        girlsCities.add(new City("山西","500"));
        girlsCities.add(new City("新疆","80"));
        girlsCities.add(new City("美国","700"));
        girlsCities.add(new City("东北","200"));


        Mondel girlsmondel = new Mondel("小姑娘", boysCities);

        list.add(boymondel);
        list.add(girlsmondel);

        return list;
    }

}
