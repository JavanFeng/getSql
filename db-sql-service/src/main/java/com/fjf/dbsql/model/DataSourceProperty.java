package com.fjf.dbsql.model;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月6日
 * @author Jiafeng Feng
 */
public class DataSourceProperty {

    String password;
    String userName;
    String classDriver;
    String url;

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            要设置的 password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            要设置的 userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the classDriver
     */
    public String getClassDriver() {
        return classDriver;
    }

    /**
     * @param classDriver
     *            要设置的 classDriver
     */
    public void setClassDriver(String classDriver) {
        this.classDriver = classDriver;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            要设置的 url
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
