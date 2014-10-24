package com.an.sfs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.an.sfs.po.ShareholderPo;
import com.an.sfs.po.StockPo;

@Component
@Scope("singleton")
public class SussDaoImpl implements SussDao, TableNameItf {
    private static final Logger LOGGER = LoggerFactory.getLogger(SussDaoImpl.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public List<ShareholderPo> getShareholder() {
        String[] columnNames = new ShareholderPo().getAddColumnNames();
        String sql = getSqlQuery(ShareholderPo.TABLE_NAME, columnNames, null, null, null);
        LOGGER.debug("Execute sql: {}", sql);
        return loadShareholder(sql);
    }

    private List<ShareholderPo> loadShareholder(String sql) {
        List<ShareholderPo> poList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);) {
            while (rs.next()) {
                ShareholderPo po = new ShareholderPo();
                po.setCode(rs.getString(COL_CODE));
                po.setTime(rs.getString(COL_TIME));
                po.setHolderNum(rs.getFloat(COL_HOLDERNUM));
                po.setStockNum(rs.getFloat(COL_STOCKNUM));
                po.setPrice(rs.getFloat(COL_PRICE));
                po.setMoney(rs.getFloat(COL_MONEY));
                po.setTop10(rs.getFloat(COL_TOP10));
                poList.add(po);
            }
        } catch (SQLException e) {
            poList.clear();
            LOGGER.error("Error, sql {}", sql, e);
        }
        LOGGER.debug("poList size {}", poList.size());
        return poList;
    }

    @Override
    public List<StockPo> getStock() {
        String[] columnNames = new StockPo().getAddColumnNames();
        String sql = getSqlQuery(StockPo.TABLE_NAME, columnNames, null, null, null);
        LOGGER.debug("Execute sql: {}", sql);
        return loadStock(sql);
    }

    private List<StockPo> loadStock(String sql) {
        List<StockPo> poList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);) {
            while (rs.next()) {
                StockPo po = new StockPo();
                po.setCode(rs.getString(COL_CODE));
                poList.add(po);
            }
        } catch (SQLException e) {
            poList.clear();
            LOGGER.error("Error, sql {}", sql, e);
        }
        LOGGER.debug("poList size {}", poList.size());
        return poList;
    }

    @Override
    public boolean clearTable(String tableName) {
        LOGGER.warn("Clear talbe {}", tableName);
        try (Connection c = dataSource.getConnection(); Statement st = c.createStatement();) {
            String sql = String.format("DELETE FROM %s;", tableName);
            LOGGER.debug("Execute sql: {}", sql);
            try {
                st.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                LOGGER.error("Error, sql: {}", sql, e);
            }
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
        }
        LOGGER.error("Failed to clear table {}", tableName);
        return false;
    }

    @Override
    public boolean clearTable(String tableName, String code) {
        LOGGER.warn("Clear talbe {}", tableName);
        try (Connection c = dataSource.getConnection(); Statement st = c.createStatement();) {
            String sql = String.format("DELETE FROM %s WHERE code='%s';", tableName, code);
            LOGGER.debug("Execute sql: {}", sql);
            try {
                st.executeUpdate(sql);
                return true;
            } catch (SQLException e) {
                LOGGER.error("Error, sql: {}", sql, e);
            }
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
        }
        LOGGER.error("Failed to clear table {}", tableName);
        return false;
    }

    @Override
    public boolean addRowList(List<TableAddRowItf> rowList) {
        if (rowList.isEmpty()) {
            LOGGER.warn("Empty rowList");
            return true;
        }
        Connection c = null;
        try {
            c = dataSource.getConnection();
            Statement st = c.createStatement();
            beginTrans(c);
            for (TableAddRowItf row : rowList) {
                String sql = getSqlAdd(row.getTableName(), row.getAddColumnNames(), row.getAddValueTypes(),
                        row.getAddColumnValues());
                LOGGER.debug("Execute sql: {}", sql);
                executeUpdate(st, sql);
            }
        } catch (SQLException e) {
            rollback(c);
            LOGGER.error("Error: {}", e);
            return false;
        } finally {
            commit(c);
            endTrans(c);
            closeConn(c);
        }
        return true;
    }

    public boolean updateRowList(List<TableUpdateRowItf> rowList) {
        if (rowList.isEmpty()) {
            LOGGER.warn("Empty rowList");
            return true;
        }
        Connection c = null;
        try {
            c = dataSource.getConnection();
            Statement st = c.createStatement();
            beginTrans(c);
            for (TableUpdateRowItf row : rowList) {
                String sql = getSqlUpdate(row.getTableName(), row.getAddColumnNames(), row.getAddColumnValues(),
                        row.getAddValueTypes(), row.getUpdateByColumnNames(), row.getUpdateByColumnValues(),
                        row.getUpdateByValueTypes());
                LOGGER.debug("Execute sql: {}", sql);
                executeUpdate(st, sql);
            }
        } catch (SQLException e) {
            rollback(c);
            LOGGER.error("Error: {}", e);
            return false;
        } finally {
            commit(c);
            endTrans(c);
            closeConn(c);
        }
        return true;
    }

    private String getSqlQuery(String tableName, String[] columns, String[] keys, Object[] values, String[] valueTypes) {
        return getSqlQuery(new String[] { tableName }, columns, keys, values, valueTypes);
    }

    private String getSqlQuery(String[] tableNames, String[] columns, String[] keys, Object[] values,
            String[] valueTypes) {
        if (keys != null && values != null && valueTypes != null
                && (keys.length != values.length || keys.length != valueTypes.length)) {
            throw new IllegalArgumentException("Keys and values don't have the same length.");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        for (int i = 0; i < columns.length; i++) {
            if (i == columns.length - 1) {
                sql.append(columns[i]);
            } else {
                sql.append(columns[i]).append(", ");
            }
        }

        sql.append(" FROM ");

        int cnt = 0;
        for (String tableName : tableNames) {
            if (cnt == tableNames.length - 1) {
                sql.append(tableName);
            } else {
                sql.append(tableName).append(", ");
            }
            cnt++;
        }

        if (keys == null || keys.length == 0) {
            sql.append(";");
        } else {
            sql.append(" WHERE ");
            for (int i = 0; i < keys.length; i++) {
                Object val = values[i];
                String operator = " = ";

                if ("str".equals(valueTypes[i])) {
                    val = "'" + val + "'";
                } else if ("array".equals(valueTypes[i])) {
                    val = "(" + val + ")";
                    operator = " in ";
                }
                if (i == keys.length - 1) {
                    sql.append(keys[i]).append(operator).append(val).append(";");
                } else {
                    sql.append(keys[i]).append(operator).append(val).append(" AND ");
                }
            }
        }
        return sql.toString();
    }

    private String getSqlAdd(String tableName, String[] columns, String[] types, Object[] values) {
        if (columns.length != types.length || columns.length != values.length) {
            throw new IllegalArgumentException("Input array's length are not equal.");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" (");
        for (int i = 0; i < columns.length; i++) {
            if (i == columns.length - 1) {
                sql.append(columns[i]).append(") ");
            } else {
                sql.append(columns[i]).append(", ");
            }
        }
        sql.append(" VALUES (");
        for (int i = 0; i < columns.length; i++) {
            Object val = values[i];
            if ("str".equals(types[i])) {
                val = "'" + val + "'";
            }
            if (i == columns.length - 1) {
                sql.append(val).append(");");
            } else {
                sql.append(val).append(", ");
            }
        }
        return sql.toString();
    }

    /**
     * @param tableName
     * @param updateColumnNames
     * @param updateColumnValues
     * @param updateValueTypes
     * @param byColumnNames
     * @param byColumnValues
     * @param byValueTypes
     * @return
     */
    private String getSqlUpdate(String tableName, String[] updateColumnNames, Object[] updateColumnValues,
            String[] updateValueTypes, String[] byColumnNames, Object[] byColumnValues, String[] byValueTypes) {
        if (updateColumnNames == null || updateColumnNames.length == 0
                || updateColumnNames.length != updateColumnValues.length
                || updateColumnNames.length != updateValueTypes.length) {
            throw new IllegalArgumentException("Input array's length are not equal.");
        }
        if (byColumnNames == null || byColumnNames.length == 0 || byColumnNames.length != byColumnValues.length
                || byColumnNames.length != byValueTypes.length) {
            throw new IllegalArgumentException("Input array's length are not equal.");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < updateColumnNames.length; i++) {
            Object val = updateColumnValues[i];
            if ("str".equals(updateValueTypes[i])) {
                val = "'" + val + "'";
            }
            sql.append(updateColumnNames[i]).append(" = ").append(val);
            if (i != updateColumnNames.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE ");
        for (int i = 0; i < byColumnNames.length; i++) {
            String operator = " = ";
            Object val = byColumnValues[i];

            if ("str".equals(byValueTypes[i])) {
                val = "'" + val + "'";
            } else if ("array".equals(byValueTypes[i])) {
                operator = " in ";
                val = "(" + val + ")";
            }
            if (i == byColumnNames.length - 1) {
                sql.append(byColumnNames[i]).append(operator).append(val).append(";");
            } else {
                sql.append(byColumnNames[i]).append(operator).append(val).append(" AND ");
            }
        }

        return sql.toString();
    }

    private void executeUpdate(Statement st, String sql) {
        try {
            LOGGER.debug("Execute sql: {}", sql);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            LOGGER.error("Error, sql: {}", sql, e);
        }
    }

    private void beginTrans(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }

    private void endTrans(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }

    private void commit(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }

    public void closeConn(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }

    private void rollback(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        } catch (SQLException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }
}
