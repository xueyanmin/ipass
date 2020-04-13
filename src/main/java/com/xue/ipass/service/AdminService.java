package com.xue.ipass.service;

import com.xue.ipass.entity.Admin;

import java.util.HashMap;

public interface AdminService {

    //登陆
    HashMap<String, Object> login(Admin admin, String enCode);

}
