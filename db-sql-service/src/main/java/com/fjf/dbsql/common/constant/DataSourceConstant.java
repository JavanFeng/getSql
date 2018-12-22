package com.fjf.dbsql.common.constant;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月17日
 * @author Jiafeng Feng
 */
public class DataSourceConstant {

    private static String classDriver = "spring.datasource.{0}driver-class-name";

    private static String url = "spring.datasource.{0}url";

    private static String userName = "spring.datasource.{0}username";

    private static String password = "spring.datasource.{0}password";

    /**
     * @return the classDriver
     */
    public static String getClassDriver(String prefix) {
        return MessageFormat.format(classDriver, getPrefix(prefix));
    }

    /**
     * @return the url
     */
    public static String getUrl(String prefix) {
        return MessageFormat.format(url, getPrefix(prefix));
    }

    /**
     * @return the userName
     */
    public static String getUserName(String prefix) {
        return MessageFormat.format(userName, getPrefix(prefix));
    }

    /**
     * @return the password
     */
    public static String getPassword(String prefix) {
        return MessageFormat.format(password, getPrefix(prefix));
    }

    /**
     * 
     */
    private DataSourceConstant(String prefix) {
        super();
    }

    /**
     * 获取前缀
     */
    private static Object getPrefix(String prefix) {
        if (StringUtils.isEmpty(prefix)) {
            return StringUtils.EMPTY;
        } else {
            return prefix + ".";
        }
    }
}
