package com.xue.ipass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.xue.ipass.dao")
//@MapperScan("com.xue.ipass.dao")
@SpringBootApplication
public class IpassApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpassApplication.class, args);
    }

}
