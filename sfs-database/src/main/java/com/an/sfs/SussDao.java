package com.an.sfs;

import java.util.List;

import com.an.sfs.po.ShareholderPo;
import com.an.sfs.po.StockPo;

public interface SussDao {
    // Clear table data
    /**
     * @param tableName
     * @return
     */
    public boolean clearTable(String tableName);

    /**
     * @param tableName
     * @param code
     * @return
     */
    public boolean clearTable(String tableName, String code);

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

    /**
     * @return
     */
    public List<StockPo> getStock();

    /**
     * @return
     */
    public List<ShareholderPo> getShareholder();
}
