package com.an.sfs.po;

import com.an.sfs.TableAddRowItf;
import com.an.sfs.TableNameItf;

public class StockPo implements TableNameItf, TableAddRowItf, Comparable<StockPo> {
    public static final String TABLE_NAME = "stock";
    private int id;
    // 600000 000001
    private String code;

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getAddColumnNames() {
        return new String[] { COL_CODE };
    }

    @Override
    public Object[] getAddColumnValues() {
        return new Object[] { code };
    }

    @Override
    public String[] getAddValueTypes() {
        return new String[] { "str" };
    }

    public StockPo() {
    }

    @Override
    public int compareTo(StockPo o) {
        return code.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "StockPo [id=" + id + ", code=" + code + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
