package com.fjf.dbsql.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.fjf.dbsql.model.Parameter;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年11月27日
 * @author Jiafeng Feng
 */
@Mapper
public interface SqlDao {

    /**
     * @param dataSource
     *            数据源
     * @return 结果
     */
    @Select("SELECT TABLE_NAME FROM information_schema.TABLES WHERE table_schema=#{dataSource}")
    List<String> getTables(String dataSource);

    /**
     * @param dataSource
     *            数据源
     * @param name
     *            表名
     * @return 结果
     */
    @Select("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = #{dataSource} AND TABLE_NAME = #{name}")
    List<String> getSqlParams(@Param("dataSource") String dataSource, @Param("name") String name);

    /**
     * 获取信息
     * 
     * @param dataSource
     * @param name
     * @return
     */
    @Select("SELECT COLUMN_NAME as tableColumn,DATA_TYPE as dataType,COLUMN_COMMENT as columnComment FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = #{dataSource} AND TABLE_NAME = #{name}")
    List<Parameter> getAllSqlParams(@Param("dataSource") String dataSource, @Param("name") String name);

}
