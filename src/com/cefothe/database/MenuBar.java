package com.cefothe.database;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
/**
 * Created by cefothe on 24.05.16.
 */
public class MenuBar extends JMenuBar {

    public MenuBar(Main main){
        super();
        buildInformationMenu(main);

    }

    private void buildInformationMenu(Main main) {
        JMenu menu = new JMenu("Main");
        add(menu);

        /**
         *Information about project
         */
        JMenuItem jmi = new JMenuItem("Information");
        menu.add(jmi);
        jmi.addActionListener(main);

        /**
         * Database browser
         */
        JMenuItem databaseBrowser = new JMenuItem("Database browser");
        menu.add(databaseBrowser);
        databaseBrowser.addActionListener(main);

        /**
         * Exit command
         */
        JMenuItem exit = new JMenuItem("Exit");
        menu.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


    }
}
