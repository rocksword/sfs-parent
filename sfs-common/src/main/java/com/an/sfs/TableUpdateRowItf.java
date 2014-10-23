package com.an.sfs;

public interface TableUpdateRowItf extends TableAddRowItf {
    // By column names
    public String[] getUpdateByColumnNames();

    // By column values
    public Object[] getUpdateByColumnValues();

    // By column value types
    public String[] getUpdateByValueTypes();
}
