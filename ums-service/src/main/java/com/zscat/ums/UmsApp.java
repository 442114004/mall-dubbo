package com.zscat.ums;

import com.alibaba.dubbo.container.Main;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: zemin.liu
 * @date: 2018/11/7 10:39
 * @description: app 入口
 */
@SpringBootApplication
@Log4j2
@MapperScan(basePackages = "com.zscat.ums.mapper")
@ImportResource(locations = {"/dubbo-provider.xml","/dubbo-consumer.xml"})
@EnableTransactionManagement
public class UmsApp {

    public static void main( String[] args ) {
        log.debug("ums-service启动start...");
        SpringApplication.run(UmsApp.class, args);
        //doubbo run
        Main.main(args);
        log.debug("ums-service启动end...");
    }
}