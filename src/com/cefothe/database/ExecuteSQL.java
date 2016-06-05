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

    public  ExecuteSQL(Connection connection){
        this.connection= connection;
        buildFrameLayout();
    }

    private void buildFrameLayout() {
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        createTextPane();
        createButtons();
        add(new JScrollPane(table));
    }

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

    protected void executeQuery(TCustomSqlStatement stmt, String sqlQuery){
        switch(stmt.sqlstatementtype){
            case sstselect:
                refreshTable(sqlQuery);
                break;
            default:
                executeAnotherQuery(sqlQuery);
        }
    }

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
