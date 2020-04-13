package com.xue.ipass.dao;

import com.xue.ipass.entity.Admin;

public interface AdminDAO {
    Admin queryByUsername(String Username);

    void queryByUsername();
}
