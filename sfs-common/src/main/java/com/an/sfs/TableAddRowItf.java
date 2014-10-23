package com.an.sfs;

public interface TableAddRowItf {
    // TABLE NAME
    public String getTableName();

    // Column names to be updated
    public String[] getAddColumnNames();

    // Update column values
    public Object[] getAddColumnValues();

    // Update column value types
    public String[] getAddValueTypes();
}