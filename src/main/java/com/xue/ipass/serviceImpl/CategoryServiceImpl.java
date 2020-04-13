package com.xue.ipass.serviceImpl;

import com.xue.ipass.annotation.AddCache;
import com.xue.ipass.annotation.AddLog;
import com.xue.ipass.dao.CategoryMapper;
import com.xue.ipass.entity.Category;
import com.xue.ipass.entity.CategoryExample;
import com.xue.ipass.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Resource
    CategoryMapper categoryMapper;

    @AddCache
    /**查询一级类别*/
    @Override
    public HashMap<String, Object> queryByOnePage(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<>();

        /*封装数据*/
        //总条数 records
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo("1");
        Integer records = categoryMapper.selectCountByExample(example);
        map.put("records", records);

        //总页数 total  总条数/每页展示条数 是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);

        //当前页 page
        map.put("page", page);

        //数据 rows
        //rowBounds需要传递参数 忽略条数 获取条数
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Category> category = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", category);
        return map;
    }

    /**查询一级类别下对应的二级类别*/
    @Override
    public HashMap<String, Object> queryByTwoPage(Integer page, Integer rows, String parentId) {

        HashMap<String, Object> map = new HashMap<>();

        /*封装数据*/
        //总条数 records
        CategoryExample example = new CategoryExample();
        example.createCriteria().andParentIdEqualTo(parentId);
        Integer records = categoryMapper.selectCountByExample(example);
        map.put("records", records);

        //总页数 total  总条数/每页展示条数 是否有余数
        Integer total = records % rows == 0 ? records / rows : records / rows + 1;
        map.put("total", total);

        //当前页 page
        map.put("page", page);

        //数据 rows
        //rowBounds需要传递参数 忽略条数 获取条数
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Category> category = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", category);
        return map;
    }

    @AddLog(value = "添加类别")
    /*一级类别与二级类别添加*/
    @Override
    public void add(Category category) {

        //一级类别与二级类别判断添加
        if (category.getParentId() == null) {

            //添加的是一级类别
            category.setLevels("1");
        } else {
            //添加的是二级类别
            category.setLevels("2");
        }
        category.setId(UUID.randomUUID().toString());
        //执行添加
        categoryMapper.insertSelective(category);

    }

    /*一级类别与二级类别修改*/
    @Override
    public void update(Category category) {
        /*修改不用区分一级类别二级类别----级别为空时也可以修给*/
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    //@AddLog(value = "删除类别")
    //一级类别与二级类编的删除操作
    @Override
    public HashMap<String, Object> delete(Category category) {
        HashMap<String, Object> map = new HashMap<>();
        //根据类别对想查询类别信息
        Category cate = categoryMapper.selectOne(category);
        //判断删除的是一级类别还是二级类别
        System.out.println(cate);
        System.out.println(cate.getLevels());
        //System.out.println("判断结果是否等于----1----"+cate.getLevels().equals("1"));
        if (cate.getLevels().equals("1")) {

            //一级类别 判断是否有二级类别 二级类别数量
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category.getId());
            int count = categoryMapper.selectCountByExample(example);
            //有二级类别 返回提示信息

            if (count == 0) {
                //没有 直接删除
                categoryMapper.deleteByPrimaryKey(category);
                map.put("status", "200");
                map.put("message", "删除成功");


            } else {

                //有二级类别 返回提示信息 不能删除
                map.put("status", "400");
                map.put("message", "删除失败，该类别下有子类别");
                System.out.println(map.get("message"));
            }
        } else {

            //二级类别 判断是否有视频、
            //有 不能删除 返回提示信息
            //没有 直接删除
            categoryMapper.deleteByPrimaryKey(category);
            map.put("status", "200");
            map.put("message", "删除成功");
        }
        return map;
    }
}
