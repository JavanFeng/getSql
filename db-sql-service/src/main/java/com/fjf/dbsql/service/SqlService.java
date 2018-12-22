package com.fjf.dbsql.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月18日
 * @author Jiafeng Feng
 */

public interface SqlService {

    void getSql(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws IOException;

    void getSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws IOException, Exception;

    void getUpdateSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception;

    void getModel(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception;

}
