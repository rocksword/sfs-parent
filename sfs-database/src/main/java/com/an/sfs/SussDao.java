package com.an.sfs;

import java.util.List;

public interface SussDao {
    // Clear table data
    /**
     * @param tableName
     * @return
     */
    public boolean clearTable(String tableName);

    /**
     * @param rowList
     * @return
     */
    public boolean addRowList(List<TableAddRowItf> rowList);

    /**
     * @param rowList
     * @return
     */
    public boolean updateRowList(List<TableUpdateRowItf> rowList);
}
