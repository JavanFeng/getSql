package com.fjf.dbsql.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 类描述:切库
 * </p>
 * 
 * @since 2018年12月14日
 * @author Jiafeng Feng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DatabaseChoose {
    /** 选择的数据库名 */
    public String value() default "";

}
