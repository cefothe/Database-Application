package com.cefothe.database;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Created by cefothe on 24.05.16.
 */
public class Main extends JFrame implements ActionListener {

    private Connection connection;

    private JPanel previusActivePanel;

    public static void main(String[] args) {
        Main main = new Main();
    }

    public Main(){
       ConnectionDialog dialog = new ConnectionDialog(this);
        connection = dialog.getConnection();
        MenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        setSize(600, 450);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Database browser")){
            DatabaseBrowser databaseBrowser = new DatabaseBrowser(connection);
            changeActiveModel(databaseBrowser);
        }if (e.getActionCommand().equals("Execute SQL")){
            ExecuteSQL executeSQL = new ExecuteSQL();
            changeActiveModel(executeSQL);
        }
    }

    private void changeActiveModel(JPanel current){
        if(previusActivePanel!=null){
            remove(previusActivePanel);
        }
        add(current,BorderLayout.CENTER);
        previusActivePanel = current;
        revalidate();
    }
}
