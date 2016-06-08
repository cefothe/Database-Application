package com.cefothe.database;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;

import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.*;

import javax.swing.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by cefothe on 24.05.16.
 */
public class ExecuteSQL extends JPanel {

    private JTextPane jTextPane;
    private JButton jButton;
    private JTable table;
    private Connection connection;

    /**
     * This is main constructor
     * @param connection {@link Connection}
     */
    public  ExecuteSQL(Connection connection){
        this.connection= connection;
        buildFrameLayout();
    }

    /**
     * Build frame layout for execute sql screen
     */
    private void buildFrameLayout() {
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        createTextPane();
        createButtons();
        add(new JScrollPane(table));
    }

    /**
     * Create submit button to execute sql
     */
    private void createButtons() {
        JButton submitButton = new JButton("Execute SQL");
        jButton = submitButton;
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateSql(jTextPane.getText());
            }
        });
        add(submitButton);

    }

    /**
     * Validate sql with {@link TGSqlParser}
     * @param sqlQuery sql query
     */
    private void validateSql(String sqlQuery){
        TGSqlParser sqlparser= new TGSqlParser(EDbVendor.dbvoracle);
        sqlparser.setSqltext(sqlQuery);
        int ret = sqlparser.parse();
        if (ret == 0){
            executeQuery(sqlparser.getSqlstatements().get(0),sqlQuery);
        }else{
            JOptionPane.showMessageDialog(this, sqlparser.getErrormessage());
        }
    }

    /**
     * This method execute sql query related to their type.
     * @param stmt {@link TCustomSqlStatement} That is contain type of query
     * @param sqlQuery last insert sql query
     */
    protected void executeQuery(TCustomSqlStatement stmt, String sqlQuery){
        switch(stmt.sqlstatementtype){
            case sstselect:
                refreshTable(sqlQuery);
                break;
            default:
                executeAnotherQuery(sqlQuery);
        }
    }

    /**
     * Execute all queries without SELECT
     * @param sqlQuery
     */
    private void executeAnotherQuery(String sqlQuery){
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sqlQuery);
            JOptionPane.showMessageDialog(this, "Query is sussec exexute");
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            JOptionPane.showMessageDialog(this, e.getMessage());
        };
    }

    /**
     * Refresh table contain when we have SELECT query
     * @param sqlQuery sql query
     */
    private void refreshTable(String sqlQuery) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            table.setModel(new ResultSetTableModel(resultSet));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            JOptionPane.showMessageDialog(this, e.getMessage());
        };
    }


    /**
     * Create text area put sql query
     */
    private void createTextPane() {
        JTextPane textPane =  new JTextPane();
        JScrollPane paneScrollPane = new JScrollPane(textPane);
        paneScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(500, 200));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));
        jTextPane = textPane;
        add(paneScrollPane);
    }

}
