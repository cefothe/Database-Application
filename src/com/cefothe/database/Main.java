package com.cefothe.database;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

/**
 * Created by cefothe on 24.05.16.
 */
public class Main extends JFrame implements ActionListener {

    private Connection connection;

    private JPanel previusActivePanel;

    /**
     * Entry point of application
     * @param args options parameters
     */
    public static void main(String[] args) {
        Main main = new Main();
    }

    /**
     * Build application
     */
    public Main(){
       ConnectionDialog dialog = new ConnectionDialog(this);
        connection = dialog.getConnection();
        MenuBar menuBar = new MenuBar(this);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        setSize(500, 600);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                setVisible(false);
                removeWindowListener(this);
                dispose();
                System.exit(0);
            }
        });

    }

    /**
     * Catch all action event
     * @param e current {@link ActiveEvent}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Database browser")){
            DatabaseBrowser databaseBrowser = new DatabaseBrowser(connection);
            changeActiveModel(databaseBrowser);
        }if (e.getActionCommand().equals("Execute SQL")){
            ExecuteSQL executeSQL = new ExecuteSQL(connection                                                                                                                                                                                                                                                                                                                                       );
            changeActiveModel(executeSQL);
        }
    }

    /**
     *  Change active model in main frame
     * @param current current active {@link JPanel}
     */
    private void changeActiveModel(JPanel current){
        if(previusActivePanel!=null){
            remove(previusActivePanel);
        }
        add(current,BorderLayout.CENTER);
        previusActivePanel = current;
        revalidate();
    }
}
