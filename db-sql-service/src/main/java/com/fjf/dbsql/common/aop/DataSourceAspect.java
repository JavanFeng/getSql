package com.fjf.dbsql.common.aop;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.fjf.dbsql.common.annotation.DatabaseChoose;
import com.fjf.dbsql.config.DataSourceNameHolder;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月18日
 * @author Jiafeng Feng
 */
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut(value = "@within(com.fjf.dbsql.common.annotation.DatabaseChoose)||@annotation(com.fjf.dbsql.common.annotation.DatabaseChoose)")
    public void chooseDataSourcePointCut() {

    }

    @Around(value = "chooseDataSourcePointCut()")
    public Object beforeChooseDataSourcePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        // 切入的方法名称
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        DatabaseChoose annotation = method.getAnnotation(DatabaseChoose.class);
        // set datasource name
        DataSourceNameHolder.setDataSourceName(getValue(joinPoint, methodSignature, annotation.value()));

        Object proceed = joinPoint.proceed();
        return proceed;

    }

    @After(value = "chooseDataSourcePointCut()")
    public void afterChooseDataSourcePointCut() throws Exception {
        // clean datasource name
        DataSourceNameHolder.cleanDataSourceName();
    }

    /**
     * get value
     * 
     * @param joinPoint
     * @param methodSignature
     */
    private String getValue(ProceedingJoinPoint joinPoint, MethodSignature methodSignature, String value) {
        if (StringUtils.isEmpty(value)) {
            return StringUtils.EMPTY;
        }

        if (!value.startsWith("#")) {
            return StringUtils.EMPTY;
        }

        String name = value.substring(1);
        int i = -1;
        String[] parameters = methodSignature.getParameterNames();
        for (int j = 0; j < parameters.length; j++) {
            String parameter = parameters[j];
            if (parameter.equals(name)) {
                i = j;
                break;
            }

        }
        Object[] values = joinPoint.getArgs();

        if (i != -1) {
            Object object = values[i];
            if (object == null) {
                return StringUtils.EMPTY;
            }
            return object.toString();
        }

        return StringUtils.EMPTY;
    }
}
