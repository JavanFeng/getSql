package com.fjf.dbsql.model;

/**
 * <p>
 * 类描述:
 * </p>
 * 
 * @since 2018年12月20日
 * @author Jiafeng Feng
 */
public class Parameter {

    String tableColumn;

    String dataType;

    String columnComment;

    /**
     * @return the columnComment
     */
    public String getColumnComment() {
        return columnComment;
    }

    /**
     * @param columnComment
     *            要设置的 columnComment
     */
    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    /**
     * @return the tableColumn
     */
    public String getTableColumn() {
        return tableColumn;
    }

    /**
     * @param tableColumn
     *            要设置的 tableColumn
     */
    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * @param dataType
     *            要设置的 dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

}
