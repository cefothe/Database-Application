package com.cefothe.database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by cefothe on 23.05.16.
 */
public class ConnectionDialog extends JDialog {
    private JTextField useridField;
    private JTextField passwordField;
    private JTextField urlField;

    private boolean canceled;
    private Connection connect;

    /**
     * This method show first dialog to connect to databse
     * @param frame This is main frame {@link JFrame}
     */
    public ConnectionDialog(JFrame frame){
        super(frame, "Connect to database", true);
        buildDialogLayout();
        setSize(300, 200);
    }

    /**
     * This method return database connection
     * @return {@link Connection}
     */
    public Connection getConnection() {
        setVisible(true);
        return connect;
    }

    /**
     * Build dialog to insert username, password and url
     */
    private void buildDialogLayout() {
        JLabel label;
        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 10, 5, 10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        label = new JLabel("Userid:", JLabel.LEFT);
        pane.add(label, constraints);

        constraints.gridy++;
        label = new JLabel("Password:", JLabel.LEFT);
        pane.add(label, constraints);

        constraints.gridy++;
        label = new JLabel("URL:", JLabel.LEFT);
        pane.add(label, constraints);
        
        constraints.gridx = 1;
        constraints.gridy = 0;

        useridField = new JTextField(10);
        pane.add(useridField, constraints);

        constraints.gridy++;
        passwordField = new JTextField(10);
        pane.add(passwordField, constraints);

        constraints.gridy++;
        urlField = new JTextField(15);
        pane.add(urlField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        pane.add(getButtonPanel(), constraints);
    }

    /**
     * This method generate buttons (OK button to start connection, Cancel button to stop connection)
     * @return {@link JPanel}
     */
    private JPanel getButtonPanel() {
        JPanel panel = new JPanel();
        JButton btn = new JButton("Ok");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                onDialogOk();
            }
        });
        panel.add(btn);
        btn = new JButton("Cancel");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                onDialogCancel();
            }
        });
        panel.add(btn);
        return panel;
    }

    private void onDialogOk() {
        if (attemptConnection()) {
            setVisible(false);
        }
    }

    private void onDialogCancel() {
        System.exit(0);
    }

    /**
     * Try to connect to database with {@link DriverManager}
     * @return true if connection is success
     */
    private boolean attemptConnection() {
        try {
            connect = DriverManager.getConnection(
                    urlField.getText(),
                    useridField.getText(),
                    passwordField.getText());
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error connecting to " +
                            "database: " + e.getMessage());
        }
        return false;
    }

}
