package com.cefothe.database;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cefothe on 23.05.16.
 */class ResultSetTableModel extends AbstractTableModel {

    private List columnHeaders;
    private List tableData;

    public ResultSetTableModel(ResultSet resultSet)
            throws SQLException {
        List rowData;
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int count = rsmd.getColumnCount();
        columnHeaders = new ArrayList(count);
        tableData = new ArrayList();
        for (int i = 1; i <= count; i++) {
            columnHeaders.add(rsmd.getColumnName(i));
        }
        while (resultSet.next()) {
            rowData = new ArrayList(count);
            for (int i = 1; i <= count; i++) {
                rowData.add(resultSet.getObject(i));
            }
            tableData.add(rowData);
        }
    }

    public int getColumnCount() {
        return columnHeaders.size();
    }

    public int getRowCount() {
        return tableData.size();
    }

    public Object getValueAt(int row, int column) {
        List rowData = (List)(tableData.get(row));
        return rowData.get(column);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public String getColumnName(int column) {
        return (String)(columnHeaders.get(column));
    }

}

