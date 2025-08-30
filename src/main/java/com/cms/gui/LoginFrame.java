package com.cms.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JComboBox<String> roleComboBox;
    private JLabel logoLabel;

    public LoginFrame() {
        setTitle("College Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initializeComponents();
        layoutComponents();
        addEventListeners();

        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeComponents() {
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        roleComboBox = new JComboBox<>(new String[]{"admin", "faculty", "student"});

        // Load logo image (replace with your actual path)
        ImageIcon logoIcon = new ImageIcon("src/main/resources/images/logo.png");
        Image scaled = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaled), JLabel.CENTER);

        // Button style
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void layoutComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo at top
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(logoLabel, gbc);

        // Title
        JLabel titleLabel = new JLabel("College Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridy = 1;
        centerPanel.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        centerPanel.add(roleComboBox, gbc);

        // Login button
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(loginButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void addEventListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                if (authenticateUser(email, password, role)) {
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

    private boolean authenticateUser(String email, String password, String role) {
        // Simple authentication - in real application, check against database
        if ("admin@gmail.com".equals(email) && "admin123".equals(password) && "admin".equals(role)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
