package com.fjf.dbsql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @since 2018年2月2日 上午10:26:33
 * @author Feng jiafeng
 * @desc 启动类
 */
@SpringBootApplication
public class DbSqlApplication {

    private final static Logger LOG = LoggerFactory.getLogger(DbSqlApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(DbSqlApplication.class, args);
        LOG.info("获取sql服务启动成功");
    }
}
