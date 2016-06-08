package com.cefothe.database;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.List;

/**
 * Created by cefothe on 23.05.16.
 */
public class DatabaseBrowser extends  JPanel  {

    private Connection connection;
    private JComboBox catalogBox;
    private JComboBox schemaBox;
    private JComboBox tableBox;
    private JTable table;

    /**
     * This constructor is main constructor for {@link DatabaseBrowser}
     * @param connection Database connections {@link Connection}
     */
    public  DatabaseBrowser(Connection connection){
        this.connection = connection;
        buildFrameLayout();
    }

    /**
     * Build frame layout for Database browser
     */
    private void buildFrameLayout() {
       add(getSelectionPanel(), BorderLayout.NORTH);
        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        refreshTable();
       add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /**
     *  This method generated top menu ot Database Browser
     * @return {@link JPanel}
     */
    private JPanel getSelectionPanel() {
        JLabel label;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 10, 5, 10);
        label = new JLabel("Catalog", JLabel.RIGHT);
        panel.add(label, constraints);
        label = new JLabel("Schema", JLabel.RIGHT);
        panel.add(label, constraints);
        label = new JLabel("Table", JLabel.RIGHT);
        panel.add(label, constraints);

        constraints.gridy = 1;
        catalogBox = new JComboBox();
        populateCatalogBox();
        panel.add(catalogBox, constraints);
        schemaBox = new JComboBox();
        populateSchemaBox();
        panel.add(schemaBox, constraints);
        tableBox = new JComboBox();
        populateTableBox();
        panel.add(tableBox, constraints);

        catalogBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                String newCatalog = (String)(
                        catalogBox.getSelectedItem());
                try {
                    connection.setCatalog(newCatalog);
                } catch (Exception e) {};
                populateSchemaBox();
                populateTableBox();
                refreshTable();
            }
        });

        schemaBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                populateTableBox();
                refreshTable();
            }
        });

        tableBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                refreshTable();
            }
        });
        return panel;


    }

    /**
     * This method change and generate values in catalog box
     */
    private void populateCatalogBox() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getCatalogs();
            java.util.List values = new ArrayList();
            while (resultSet.next()) {
                values.add (resultSet.getString(1));
            }
            resultSet.close();
            catalogBox.setModel(new DefaultComboBoxModel(values.toArray()));
            catalogBox.setSelectedItem(connection.getCatalog());
            catalogBox.setEnabled(values.size() > 0);
        } catch (Exception e) {
            catalogBox.setEnabled(false);
        }
    }
    /**
     * This method change and generate values in schema box
     */
    private void populateSchemaBox() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getSchemas();
            List values = new ArrayList();
            while (resultSet.next()) {
                values.add (resultSet.getString(1));
            }
            resultSet.close();
            schemaBox.setModel(new DefaultComboBoxModel(values.toArray()));
            schemaBox.setEnabled(values.size() > 0);
        } catch (Exception e) {
            schemaBox.setEnabled(false);
        }
    }
    /**
     * This method change and generate values in table box
     */
    private void populateTableBox() {
        try {
            String[] types = {"TABLE"};
            String catalog = connection.getCatalog();
            String schema = (String)(schemaBox.getSelectedItem());
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(catalog, schema, null,
                    types);
            List values = new ArrayList();
            while (resultSet.next()) {
                values.add(resultSet.getString(3));
            }
            resultSet.close();
            tableBox.setModel(new DefaultComboBoxModel(values.toArray()));
            tableBox.setEnabled(values.size() > 0);
        } catch (Exception e) {
            tableBox.setEnabled(false);
        }
    }


    /**
     * This method refresh table contains
     */
    private void refreshTable() {
        String catalog = (catalogBox.isEnabled() ?
                catalogBox.getSelectedItem().toString() :
                null);
        String schema = (schemaBox.isEnabled() ?
                schemaBox.getSelectedItem().toString() :
                null);
        String tableName = (String)tableBox.getSelectedItem();
        if (tableName == null) {
            table.setModel(new DefaultTableModel());
            return;
        }
        String selectTable = (schema == null ? "" : schema + ".") +
                tableName;
        if (selectTable.indexOf(' ') > 0) {
            selectTable = "\"" + selectTable + "\"";
        }
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " +
                    selectTable);
            table.setModel(new ResultSetTableModel(resultSet));
        } catch (Exception e) {};
    }
}
