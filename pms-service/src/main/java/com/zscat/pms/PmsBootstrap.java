package com.zscat.pms;

import com.alibaba.dubbo.container.Main;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: zscat
 * @date: 2018/11/7 10:39
 * @description: app 入口
 */
@SpringBootApplication
@Log4j2
@MapperScan(basePackages = "com.zscat.pms.mapper")
@ImportResource(locations = {"/dubbo-provider.xml"})
@EnableTransactionManagement
public class PmsBootstrap {

    public static void main( String[] args ) {
        log.debug("pms-service启动start...");
        SpringApplication.run(PmsBootstrap.class, args);
        //doubbo run
        Main.main(args);
        log.debug("pms-service启动end...");
    }
}