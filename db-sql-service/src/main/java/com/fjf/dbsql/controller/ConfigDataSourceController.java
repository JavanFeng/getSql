package com.fjf.dbsql.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjf.dbsql.config.MultipleDataSourceToChoose;
import com.fjf.dbsql.dao.SqlDao;
import com.fjf.dbsql.model.DataSourceProperty;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年11月15日
 * @author Jiafeng Feng
 */
@RestController
@RequestMapping("datasource/config")
public class ConfigDataSourceController {

    @Autowired
    SqlDao dao;

    @Autowired
    MultipleDataSourceToChoose choose;

    @GetMapping("/add")
    public String addDataSourceInfo(DataSourceProperty prop, String dataSourceName) throws Exception {
        // add
        MultipleDataSourceToChoose.addDataSource(prop, dataSourceName);
        choose.refreshTargetDataSrouce();

        return "success";
    }

    @GetMapping("/del")
    public String delDataSourceInfo(String dataSourceName) throws Exception {

        // del
        MultipleDataSourceToChoose.delDataSource(dataSourceName);
        choose.refreshTargetDataSrouce();

        return "success";
    }

    @GetMapping("/list")
    public Set<String> getAllDataSourceName() {
        return MultipleDataSourceToChoose.getDatasourceNamesList();
    }

}
