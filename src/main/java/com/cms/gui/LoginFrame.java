package com.cms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;
    
    public LoginFrame() {
        setTitle("College Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        initializeComponents();
        layoutComponents();
        addEventListeners();
        
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        roleComboBox = new JComboBox<>(new String[]{"admin", "faculty", "student"});
    }
    
    private void layoutComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("College Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);
        
        // Username
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(roleComboBox, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        centerPanel.add(loginButton, gbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }
    
    private void addEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                
                if (authenticateUser(username, password, role)) {
                    dispose();
                    new MainFrame(role).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this,
                            "Invalid credentials. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private boolean authenticateUser(String username, String password, String role) {
        // Simple authentication - in real application, check against database
        // For demo purposes, using hardcoded credentials
        if ("admin".equals(username) && "admin123".equals(password) && "admin".equals(role)) {
            return true;
        }
        // Add more authentication logic here
        return false;
    }
}