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

    /**
     * This is contain information for table that we represent in all tables
     * @param resultSet {@link ResultSet}
     * @throws SQLException If result set is empty
     */
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

    /**
     * This method return a number of columns
     * @return number of columns
     */
    public int getColumnCount() {
        return columnHeaders.size();
    }

    /**
     *This method return a number of rows
     * @return number of rows
     */
    public int getRowCount() {
        return tableData.size();
    }

    /**
     * This method return a values for cell
     * @param row Where is the row
     * @param column Where is the column
     * @return return a value for cell
     */
    public Object getValueAt(int row, int column) {
        List rowData = (List)(tableData.get(row));
        return rowData.get(column);
    }

    /**
     * This method return is cell is editable
     * @param row Where is the row
     * @param column Where is the column
     * @return if cell is editable
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * This method  return header name
     * @param column which is a column
     * @return return a current name of header row
     */
    public String getColumnName(int column) {
        return (String)(columnHeaders.get(column));
    }

}

