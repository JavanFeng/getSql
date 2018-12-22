package com.fjf.dbsql.config;

/**
 * <p>
 * 类描述: 数据源
 * </p>
 * 
 * @since 2018年12月14日
 * @author Jiafeng Feng
 */
public class DataSourceNameHolder {
    /** 数据库holder */
    private final static ThreadLocal<String> DATASOURCENAMEHOLDER = ThreadLocal.withInitial(() -> "");

    public static String getDataSourceName() {
        return DATASOURCENAMEHOLDER.get();
    }

    public static void setDataSourceName(String datasourceName) {
        DATASOURCENAMEHOLDER.set(datasourceName);
    }

    public static void cleanDataSourceName() {
        DATASOURCENAMEHOLDER.remove();
    }
}
