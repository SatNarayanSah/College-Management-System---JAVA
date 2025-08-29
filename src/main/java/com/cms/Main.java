package com.cms;

import com.cms.gui.LoginFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Set system look and feel
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Could not set look and feel: " + e.getMessage());
                }
                
                // Create and show login frame
                new LoginFrame().setVisible(true);
            }
        });
    }
}