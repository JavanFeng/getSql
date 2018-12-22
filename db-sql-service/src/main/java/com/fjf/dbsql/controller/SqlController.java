package com.fjf.dbsql.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjf.dbsql.service.SqlService;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年11月15日
 * @author Jiafeng Feng
 */
@RestController
@RequestMapping("sql/")
public class SqlController {

    @Autowired
    SqlService sqlService;

    @GetMapping("getSqlParams")
    public void getSql(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws IOException {
        sqlService.getSql(dataSourceConfigName, tableName, dataSourceName, response);
    }

    @GetMapping("getInsertSqlIftest")
    public void getSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        sqlService.getSqlIftest(dataSourceConfigName, tableName, dataSourceName, response);
    }

    @GetMapping("getUpdateSqlIftest")
    public void getUpdateSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        sqlService.getUpdateSqlIftest(dataSourceConfigName, tableName, dataSourceName, response);
    }

    @GetMapping("getModel")
    public void getModel(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        sqlService.getModel(dataSourceConfigName, tableName, dataSourceName, response);
    }

}
