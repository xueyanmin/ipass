package com.xue.ipass.serviceImpl;

import com.xue.ipass.dao.AdminDAO;
import com.xue.ipass.entity.Admin;
import com.xue.ipass.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Resource
    HttpSession session;

    @Resource
    AdminDAO adminDAO;

    @Override
    public HashMap<String, Object> login(Admin admin, String enCode) {

        HashMap<String, Object> map = new HashMap<>();

        /*1：获取存储验证码*/
        Object imageCode = session.getAttribute("imageCode");
        /*2：判断验证码*/
        if (imageCode.equals(enCode)){
            /*3:验证码正确 获取用户名*/
            Admin admins = adminDAO.queryByUsername(admin.getUsername());
            /*4:判断用户名*/
            if (admins != null){
                /*5：用户名正确 获取用户密码 判断用户密码*/
                if (admins.getPassword().equals(admin.getPassword())){

                    /*用户密码正确 登陆成功 存储用户信息*/
                    session.setAttribute("admins",admin);
                    map.put("status","200");
                    map.put("message","登陆成功");
                }else {
                    map.put("status","400");
                    map.put("message","用户密码错误");
                }
            }else {
                map.put("status","400");
                map.put("message","用户不存在");
            }
        }else {
            map.put("status","400");
            map.put("message","验证码错误");
        }

        //将数据返回
        return map;
    }
}
