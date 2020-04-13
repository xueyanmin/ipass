package com.xue.ipass;
import com.xue.ipass.dao.AdminDAO;
import com.xue.ipass.dao.UserMapper;
import com.xue.ipass.entity.Admin;
import com.xue.ipass.entity.User;
import com.xue.ipass.entity.UserExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class IpassApplicationTests {

    /*@Resource
    AdminDAO adminDAO;

    @Resource
    UserMapper userMapper;

    @Test
    public void select(){
        List<User> users = userMapper.selectAll();
        users.forEach(user -> System.out.println(user));
    }

    @Test
    public void testQuery(){

    }*/

    /*@Test
    public void testquery(){
        //条件对象
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo("1");
        List<User> users = userMapper.selectByExample(example);
        for (User user : users) {
            System.out.println(user);
        }


        }
        @Test
        public void save(){


            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo("3");

            //User user = new User("3", "zcn", "zcn", "zcn", "zcn", "zcn", null, new Date());
            //userMapper.insert(user);

            User user = new User();
            user.setId("3");
            user.setUsername("王宝强");
            user.setStatus("正常");
            //userMapper.updateByExample(user,example);
            userMapper.updateByExampleSelective(user,example);

    }

    @Test
    public void testQueryCount(){
        UserExample userExample = new UserExample();
        long l = userMapper.countByExample(userExample);
        System.out.println(l);

    }
    @Test
    public void delete(){
        UserExample example = new UserExample();
        example.createCriteria().andIdEqualTo("3");
        userMapper.deleteByExample(example);
    }

    @Test
    public void query(){
        List<User> users = userMapper.queryByUsername();
        users.forEach(user -> System.out.println(user));
    }
*/

    /*@Test
   public  void contextLoads() {
        Admin admin = adminDAO.queryByUsername("2");
        System.out.println(admin);
    }*/

}
