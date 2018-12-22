package com.fjf.dbsql.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fjf.dbsql.common.annotation.DatabaseChoose;
import com.fjf.dbsql.dao.SqlDao;
import com.fjf.dbsql.model.Parameter;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月18日
 * @author Jiafeng Feng
 */
@Service
public class SqlServiceImpl implements SqlService {

    /** quotation_marker */
    private static final char QUOTATION_MARKER = '"';
    /** SPACE */
    private static final String SPACE = " ";
    @Autowired
    SqlDao dao;

    /**
     * @param dataSource
     * @return
     */
    private List<String> getTables(String dataSource) {
        return dao.getTables(dataSource);
    }

    @Override
    @DatabaseChoose("#dataSourceConfigName")
    public void getSql(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws IOException {
        List<String> tableNames = null;

        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(dataSourceName)) {
            tableNames = Arrays.asList(tableName);
        } else {
            tableNames = getTables(dataSourceName);
        }

        printSql(tableNames, dataSourceName, response);
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        // 2.设置文件头
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("哈哈.", "UTF-8"));

    }

    /**
     * 打印sql
     */
    private void printSql(List<String> tableNames, String dataSource, HttpServletResponse response) throws IOException {
        List<String> list = new ArrayList<String>(tableNames.size());

        ServletOutputStream outputStream = response.getOutputStream();
        //
        char tableAlias = 'A';
        for (String name : tableNames) {
            List<String> params = dao.getSqlParams(dataSource, name);

            // params
            StringBuilder builder = new StringBuilder();
            builder.append("<sql id=");
            builder.append(QUOTATION_MARKER);
            builder.append(name);
            builder.append("_sql_");
            builder.append(tableAlias);
            builder.append("\">");
            builder.append(System.lineSeparator());

            for (String param : params) {
                builder.append(tableAlias);
                builder.append(".");
                builder.append(param);
                builder.append(",");
            }
            builder.delete(builder.length() - 1, builder.length());

            builder.append(System.lineSeparator());
            builder.append("</sql>");
            builder.append(System.lineSeparator());
            builder.append(System.lineSeparator());
            outputStream.write(builder.toString().getBytes());
            list.add(builder.toString());
            tableAlias += 1;
        }

        outputStream.flush();
    }

    @Override
    @DatabaseChoose("#dataSourceConfigName")
    public void getSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        List<String> tableNames = null;

        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(dataSourceName)) {
            tableNames = Arrays.asList(tableName);
        } else {
            tableNames = getTables(dataSourceName);
        }

        printSqlIftest(tableNames, dataSourceName, response);
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        // 2.设置文件头
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("哈哈.", "UTF-8"));

    }

    private void printSqlIftest(List<String> tableNames, String dataSourceName, HttpServletResponse response) throws Exception {

        ServletOutputStream outputStream = response.getOutputStream();
        //
        for (String name : tableNames) {
            List<Parameter> params = dao.getAllSqlParams(dataSourceName, name);

            StringBuilder builder = new StringBuilder();
            builder.append("<insert id=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append(" parameterType=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append(">");
            builder.append(System.lineSeparator());
            builder.append("INSERT INTO");
            builder.append(System.lineSeparator());
            builder.append("<include refid=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append("></include>");
            builder.append(System.lineSeparator());
            builder.append("<trim prefix=");
            builder.append(QUOTATION_MARKER);
            builder.append('(');
            builder.append(QUOTATION_MARKER);
            builder.append(" suffix=");
            builder.append(QUOTATION_MARKER);
            builder.append(")");
            builder.append(QUOTATION_MARKER);
            builder.append(" suffixOverrides=");
            builder.append(QUOTATION_MARKER);
            builder.append(",");
            builder.append(QUOTATION_MARKER);
            builder.append(">");
            builder.append(System.lineSeparator());

            for (Parameter param : params) {
                if (param.getTableColumn().equals("id")) {
                    continue;
                }
                buildInsertSqlPrefix(builder, param);
            }
            builder.append("</trim>");
            builder.append("<trim prefix=");
            builder.append(QUOTATION_MARKER);
            builder.append("VALUES(");
            builder.append(QUOTATION_MARKER);
            builder.append(" suffix=");
            builder.append(QUOTATION_MARKER);
            builder.append(")");
            builder.append(QUOTATION_MARKER);
            builder.append(" suffixOverrides=");
            builder.append(QUOTATION_MARKER);
            builder.append(",");
            builder.append(QUOTATION_MARKER);
            builder.append(">");
            builder.append(System.lineSeparator());
            for (Parameter param : params) {
                if (param.getTableColumn().equals("id")) {
                    continue;
                }
                buildInsertSqlValues(builder, param);
            }
            builder.append("</trim>");
            builder.append(System.lineSeparator());
            builder.append("</insert>");
            builder.append(System.lineSeparator());
            builder.append(System.lineSeparator());
            builder.append(System.lineSeparator());
            outputStream.write(builder.toString().getBytes());
        }

        outputStream.flush();
    }

    /**
     * @param builder
     * @param param
     */
    private void buildInsertSqlValues(StringBuilder builder, Parameter param) {
        if (param.getDataType().equals("varchar") || param.getDataType().equals("text")) {
            insertIfTestVarcharOrTextValues(builder, param);
        } else {
            insertIfTestElseValues(builder, param);
        }

    }

    /**
     * @param builder
     * @param param
     */
    private void insertIfTestElseValues(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("!=null");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append("#{");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("}");
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());
    }

    /**
     * @param builder
     * @param param
     */
    private void insertIfTestVarcharOrTextValues(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("!=null and ");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("!=''");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append("#{");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("}");
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());

    }

    /**
     * @param builder
     * @param param
     */
    private void buildInsertSqlPrefix(StringBuilder builder, Parameter param) {

        if (param.getDataType().equals("varchar") || param.getDataType().equals("text")) {
            insertIfTestVarcharOrText(builder, param);
        } else {
            insertIfTestElse(builder, param);
        }

    }

    /**
     * 
     */
    private void insertIfTestVarcharOrText(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" != null and ");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" !='' ");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append(param.getTableColumn());
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());
    }

    private void insertIfTestElse(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" !=null ");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append(param.getTableColumn());
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());
    }

    public static String getCamelParams(String param) {

        if (!param.contains("_")) {
            return param;
        }

        StringBuffer sb = new StringBuffer(16);
        Pattern p = Pattern.compile("_(\\w)");
        java.util.regex.Matcher m = p.matcher(param);
        while (m.find()) {
            m.appendReplacement(sb, m.group(1).toUpperCase());
        }
        m.appendTail(sb);

        return sb.toString();
    }

    @Override
    @DatabaseChoose("#dataSourceConfigName")
    public void getUpdateSqlIftest(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        List<String> tableNames = null;

        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(dataSourceName)) {
            tableNames = Arrays.asList(tableName);
        } else {
            tableNames = getTables(dataSourceName);
        }

        printUpdateIftestSql(tableNames, dataSourceName, response);
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        // 2.设置文件头
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("哈哈.", "UTF-8"));

    }

    private void printUpdateIftestSql(List<String> tableNames, String dataSourceName, HttpServletResponse response) throws IOException {

        ServletOutputStream outputStream = response.getOutputStream();
        //
        for (String name : tableNames) {
            List<Parameter> params = dao.getAllSqlParams(dataSourceName, name);

            StringBuilder builder = new StringBuilder();
            builder.append("<update id=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append(" parameterType=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append(">");
            builder.append(System.lineSeparator());
            builder.append("UPDATE");
            builder.append(System.lineSeparator());
            builder.append("<include refid=");
            builder.append(QUOTATION_MARKER);
            builder.append(SPACE);
            builder.append(QUOTATION_MARKER);
            builder.append("></include>");
            builder.append(System.lineSeparator());
            builder.append("<set>");
            builder.append(System.lineSeparator());

            for (Parameter param : params) {
                if (param.getTableColumn().equals("id")) {
                    continue;
                }
                buildUpdateSql(builder, param);
            }
            builder.append("</set>");
            builder.append("WHERE id = #{id}");
            builder.append(System.lineSeparator());
            builder.append("</update>");

            outputStream.write(builder.toString().getBytes());
        }

        outputStream.flush();
    }

    private void buildUpdateSql(StringBuilder builder, Parameter param) {
        if (param.getDataType().equals("varchar") || param.getDataType().equals("text")) {
            updateIfTestVarcharOrText(builder, param);
        } else {
            updateIfTestElse(builder, param);
        }
    }

    private void updateIfTestElse(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" !=null ");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append(param.getTableColumn());
        builder.append("=");
        builder.append("#{");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("}");
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());
    }

    private void updateIfTestVarcharOrText(StringBuilder builder, Parameter param) {
        builder.append("<if test=");
        builder.append(QUOTATION_MARKER);
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" !=null and ");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(" !='' ");
        builder.append(QUOTATION_MARKER);
        builder.append(">");
        builder.append(System.lineSeparator());
        builder.append(param.getTableColumn());
        builder.append("=");
        builder.append("#{");
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append("}");
        builder.append(',');
        builder.append(System.lineSeparator());
        builder.append("</if>");
        builder.append(System.lineSeparator());

    }

    @Override
    @DatabaseChoose("#dataSourceConfigName")
    public void getModel(String dataSourceConfigName, String tableName, String dataSourceName, HttpServletResponse response) throws Exception {
        List<String> tableNames = null;

        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(dataSourceName)) {
            tableNames = Arrays.asList(tableName);
        } else {
            tableNames = getTables(dataSourceName);
        }

        printGetModel(tableNames, dataSourceName, response);
        // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");

        // 2.设置文件头
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("哈哈.", "UTF-8"));

    }

    private void printGetModel(List<String> tableNames, String dataSourceName, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();
        //
        for (String name : tableNames) {
            List<Parameter> params = dao.getAllSqlParams(dataSourceName, name);

            StringBuilder builder = new StringBuilder();
            builder.append("================ ");
            builder.append(getModelName(name));
            builder.append(" ================");
            builder.append(System.lineSeparator());

            for (Parameter param : params) {
                buildModelParams(builder, param);
            }

            outputStream.write(builder.toString().getBytes());
        }

        outputStream.flush();
    }

    private void buildModelParams(StringBuilder builder, Parameter param) {
        // comment
        builder.append("/** ");
        builder.append(param.getColumnComment());
        builder.append(" */");

        builder.append(System.lineSeparator());
        ifDateAppendForMate(param, builder);
        builder.append("public");
        builder.append(getJavaType(param));
        builder.append(getCamelParams(param.getTableColumn()));
        builder.append(";");
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());

    }

    /**
     * @param param
     */
    private void ifDateAppendForMate(Parameter param, StringBuilder build) {
        if (param.getDataType().toLowerCase().equals("datetime")) {
            build.append("@DateTimeFormat(pattern = ");
            build.append(QUOTATION_MARKER);
            build.append("yyyy-MM-dd HH:mm:ss");
            build.append(QUOTATION_MARKER);
            build.append(", iso = ISO.DATE)");
            build.append(System.lineSeparator());
        }

    }

    /**
     * 仅常用的
     */
    private Object getJavaType(Parameter param) {
        String lowerCase = param.getDataType().toLowerCase();
        if (lowerCase.equals("datetime")) {
            return " Date ";
        }

        if (lowerCase.equals("varchar") || lowerCase.equals("text")) {
            return " String ";
        }

        if (lowerCase.equals("int") || lowerCase.equals("integer") || lowerCase.equals("tinyint")) {
            return " Integer ";
        }

        if (lowerCase.equals("bigint")) {
            return " Long ";
        }
        return " NotKnow ";
    }

    private Object getModelName(String name) {
        String substring = name.substring(name.indexOf("j_") + 1);
        String camelParams = getCamelParams(substring);
        if (Character.isUpperCase(camelParams.charAt(0))) {
            return camelParams;
        } else {
            return StringUtils.capitalize(camelParams);
        }
    }

}
