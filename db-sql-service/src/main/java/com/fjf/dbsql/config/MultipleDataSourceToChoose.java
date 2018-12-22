package com.fjf.dbsql.config;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.fjf.dbsql.common.constant.Constant;
import com.fjf.dbsql.common.constant.DataSourceConstant;
import com.fjf.dbsql.model.DataSourceProperty;

/**
 * 
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月6日
 * @author Jiafeng Feng
 */
@Component
public class MultipleDataSourceToChoose extends AbstractRoutingDataSource {

    private volatile Object default_datasource;

    private static final Set<String> DATASOURCE_NAMES_LIST = Collections.synchronizedSet(new HashSet<String>(16));

    private static final Map<Object, Object> DATASOURCE_POOL = new ConcurrentHashMap<Object, Object>(16);

    private static final DefaultListableBeanFactory context = new DefaultListableBeanFactory();

    /**
     * @return the default_datasource
     */
    public Object getDefault_datasource() {
        return default_datasource;
    }

    /**
     * @return the datasourceNamesList
     */
    public static Set<String> getDatasourceNamesList() {
        return DATASOURCE_NAMES_LIST;
    }

    @Autowired
    private Environment env;

    /**
     * use the thread bind databasource
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceNameHolder.getDataSourceName();
    }

    /**
     * self
     */
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {

        super.setTargetDataSources(targetDataSources);

    }

    @Override
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    /**
     * self bind datasources
     */
    @Override
    public void afterPropertiesSet() {
        // define and register MyOtherBean
        // 设置默认数据源
        setPoolDefaultTargetDataSource();

        // 设置路由数据源
        setPoolTargetDataSources();

        super.afterPropertiesSet();
    }

    /**
     * 设置所有的可路由的数据源
     */
    private void setPoolTargetDataSources() {
        // get all datasource info needed to set
        if (DATASOURCE_POOL.size() == 0) {
            doInit();
        }

        this.setTargetDataSources(DATASOURCE_POOL);

    }

    /**
     * 获取数据源信息
     */
    private void doInit() {
        try {
            // read names
            getAllDataSourceNames();

            // generate props
            ClassPathResource re = new ClassPathResource(Constant.DATASOUCE_INFO_CONFIG_FILE_NAME);

            InputStream ins = re.getInputStream();

            // 将需要的字段名称写入文件中
            Properties prop = new Properties();
            BufferedInputStream in = new BufferedInputStream(ins);
            // 从输入流中读取属性列表（键和元素对）,防止乱码
            prop.load(new InputStreamReader(in, "utf-8"));
            // 调用 Hashtable 的方法 put。使用 getProperty 方法提供并行性。
            registBeans(prop);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * build
     */
    private void registBeans(Properties prop) {

        DATASOURCE_NAMES_LIST.forEach(prefix -> {
            String classDriver = prop.getProperty(DataSourceConstant.getClassDriver(prefix));
            String url = prop.getProperty(DataSourceConstant.getUrl(prefix));
            String userName = prop.getProperty(DataSourceConstant.getUserName(prefix));
            String password = prop.getProperty(DataSourceConstant.getPassword(prefix));
            // register
            GenericBeanDefinition beanOtherDef = new GenericBeanDefinition();
            beanOtherDef.setBeanClass(BasicDataSource.class);
            context.registerBeanDefinition(prefix, beanOtherDef);
            // using MyBean instance
            BasicDataSource dataSource = (BasicDataSource) context.getBean(prefix);
            dataSource.setDriverClassName(classDriver);
            dataSource.setUrl(url);
            dataSource.setUsername(userName);
            dataSource.setPassword(password);
            DATASOURCE_POOL.put(prefix, dataSource);
        });
    }

    /**
     * read names
     * 
     */
    private void getAllDataSourceNames() throws Exception {
        InputStream ins = null;
        BufferedReader read = null;
        try {
            // read the file
            ClassPathResource re = new ClassPathResource(Constant.DATASOUCE_NAME_CONFIG_FILE_NAME);
            ins = re.getInputStream();

            read = new BufferedReader(new InputStreamReader(ins));
            String name;
            while ((name = read.readLine()) != null) {
                DATASOURCE_NAMES_LIST.add(name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                ins.close();
            }
            if (read != null) {
                read.close();
            }
        }
    }

    /**
     * 设置默认数据源
     */
    private void setPoolDefaultTargetDataSource() {

        if (default_datasource == null) {
            synchronized (MultipleDataSourceToChoose.class) {
                if (default_datasource == null) {
                    // register
                    GenericBeanDefinition beanOtherDef = new GenericBeanDefinition();
                    beanOtherDef.setBeanClass(BasicDataSource.class);
                    context.registerBeanDefinition("defaultDataSource-155", beanOtherDef);
                    // using MyBean instance
                    BasicDataSource dataSource = (BasicDataSource) context.getBean("defaultDataSource-155");
                    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
                    dataSource.setUrl(env.getProperty("spring.datasource.url"));
                    dataSource.setUsername(env.getProperty("spring.datasource.username"));
                    dataSource.setPassword(env.getProperty("spring.datasource.password"));
                    dataSource.setInitialSize(5);
                    dataSource.setMaxIdle(10);
                    dataSource.setMinIdle(5);
                    dataSource.setMaxTotal(15);
                    dataSource.setMaxWaitMillis(60000);
                    dataSource.setValidationQuery("SELECT 1");
                    dataSource.setRemoveAbandonedOnBorrow(true);
                    dataSource.setRemoveAbandonedTimeout(30);
                    dataSource.setLogAbandoned(false);
                    this.setDefaultTargetDataSource(dataSource);
                }
            }

        }

    }

    public static void addDataSource(DataSourceProperty prop, String dataSourceName) throws Exception {

        // 判斷是否有重名
        if (DATASOURCE_POOL.containsKey(dataSourceName)) {
            throw new Exception("already has this name of datasource in the targetSource");
        }

        GenericBeanDefinition beanOtherDef = new GenericBeanDefinition();
        beanOtherDef.setBeanClass(BasicDataSource.class);
        context.registerBeanDefinition(dataSourceName, beanOtherDef);
        // using MyBean instance
        BasicDataSource dataSource = (BasicDataSource) context.getBean(dataSourceName);
        dataSource.setDriverClassName(prop.getClassDriver());
        dataSource.setUrl(prop.getUrl());
        dataSource.setUsername(prop.getUserName());
        dataSource.setPassword(prop.getPassword());

        DATASOURCE_POOL.put(dataSourceName, dataSource);
        DATASOURCE_NAMES_LIST.add(dataSourceName);
    }

    public static void delDataSource(String dataSourceName) throws Exception {

        DATASOURCE_POOL.remove(dataSourceName);
        DATASOURCE_NAMES_LIST.remove(dataSourceName);
    }

    public void refreshTargetDataSrouce() throws Exception {
        synchronized (DATASOURCE_POOL) {
            afterPropertiesSet();
        }
    }

}