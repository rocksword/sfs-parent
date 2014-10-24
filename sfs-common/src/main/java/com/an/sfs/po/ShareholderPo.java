package com.an.sfs.po;

import com.an.sfs.TableAddRowItf;
import com.an.sfs.TableNameItf;

public class ShareholderPo implements TableNameItf, TableAddRowItf, Comparable<StockPo> {
    public static final String TABLE_NAME = "shareholder";
    private int id;
    private String code;
    private String time;// Time
    private float holderNum;// Person number, x10000
    private float stockNum;// Stock number each person, x10000
    private float price; // Stock price
    private float money;// Total hold money each person, x10000
    private float top10;// Top 10 total hold percent, %

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[] getAddColumnNames() {
        return new String[] { COL_CODE, COL_TIME, COL_HOLDERNUM, COL_STOCKNUM, COL_PRICE, COL_MONEY, COL_TOP10 };
    }

    @Override
    public Object[] getAddColumnValues() {
        return new Object[] { code, time, holderNum, stockNum, price, money, top10 };
    }

    @Override
    public String[] getAddValueTypes() {
        return new String[] { "str", "str", "", "", "", "", "" };
    }

    public ShareholderPo() {
    }

    @Override
    public int compareTo(StockPo o) {
        return this.code.compareTo(o.getCode());
    }

    @Override
    public String toString() {
        return "ShareholderPo [id=" + id + ", code=" + code + ", time=" + time + ", holderNum=" + holderNum
                + ", stockNum=" + stockNum + ", price=" + price + ", money=" + money + ", top10=" + top10 + "]";
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getHolderNum() {
        return holderNum;
    }

    public void setHolderNum(float holderNum) {
        this.holderNum = holderNum;
    }

    public float getStockNum() {
        return stockNum;
    }

    public void setStockNum(float stockNum) {
        this.stockNum = stockNum;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getTop10() {
        return top10;
    }

    public void setTop10(float top10) {
        this.top10 = top10;
    }
}
