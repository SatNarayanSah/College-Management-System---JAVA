package com.cms.gui;

import com.cms.dao.StudentDAO;
import com.cms.model.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame {
    private String userRole;
    private JTabbedPane tabbedPane;
    private StudentDAO studentDAO;

    public MainFrame(String role) {
        this.userRole = role;
        this.studentDAO = new StudentDAO();
        
        setTitle("College Management System - " + role.toUpperCase() + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Initialize panels based on role
        if (userRole.equals("admin")) {
            tabbedPane.addTab("Students", createStudentPanel());
            tabbedPane.addTab("Courses", createCoursesPanel());
            tabbedPane.addTab("Faculty", createFacultyPanel());
            tabbedPane.addTab("Marks", createMarksPanel());
        } else if (userRole.equals("faculty")) {
            tabbedPane.addTab("Courses", createCoursesPanel());
            tabbedPane.addTab("Marks", createMarksPanel());
            tabbedPane.addTab("Students", createStudentPanel());
        } else if (userRole.equals("student")) {
            tabbedPane.addTab("My Profile", createStudentProfilePanel());
            tabbedPane.addTab("My Courses", createCoursesPanel());
            tabbedPane.addTab("My Marks", createMarksPanel());
        }
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Add toolbar for logout
        JToolBar toolBar = new JToolBar();
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        toolBar.add(logoutButton);
        add(toolBar, BorderLayout.NORTH);
        
        // Add tabbed pane
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> studentList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(studentList);
        
        // Load students
        refreshStudentList(listModel);
        
        // Buttons for CRUD operations
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");
        JButton refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        if (userRole.equals("admin")) {
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
        }
        buttonPanel.add(refreshButton);
        
        // Add student form
        addButton.addActionListener(e -> showAddStudentDialog());
        
        // Update student
        updateButton.addActionListener(e -> {
            String selected = studentList.getSelectedValue();
            if (selected != null) {
                String studentId = selected.split(" - ")[0];
                showUpdateStudentDialog(studentId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to update.");
            }
        });
        
        // Delete student
        deleteButton.addActionListener(e -> {
            String selected = studentList.getSelectedValue();
            if (selected != null) {
                String studentId = selected.split(" - ")[0];
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete student ID " + studentId + "?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (studentDAO.deleteStudent(studentId)) {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                        refreshStudentList(listModel);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting student.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.");
            }
        });
        
        // Refresh list
        refreshButton.addActionListener(e -> refreshStudentList(listModel));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshStudentList(DefaultListModel<String> listModel) {
        listModel.clear();
        List<Student> students = studentDAO.getAllStudents();
        for (Student student : students) {
            listModel.addElement(student.getStudentId() + " - " + student.getFullName() + " - " + student.getEmail());
        }
    }

    private void showAddStudentDialog() {
        JTextField studentIdField = new JTextField(20);
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField dobField = new JTextField(20);
        JTextField enrollmentField = new JTextField(20);
        JTextField semesterField = new JTextField(20);
        JTextField branchField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Student ID:"));
        panel.add(studentIdField);
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        panel.add(dobField);
        panel.add(new JLabel("Enrollment Date (YYYY-MM-DD):"));
        panel.add(enrollmentField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Branch:"));
        panel.add(branchField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Student student = new Student();
                student.setStudentId(studentIdField.getText());
                student.setFirstName(firstNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setEmail(emailField.getText());
                student.setPhone(phoneField.getText());
                student.setAddress(addressField.getText());
                if (!dobField.getText().isEmpty()) {
                    student.setDateOfBirth(LocalDate.parse(dobField.getText()));
                }
                if (!enrollmentField.getText().isEmpty()) {
                    student.setEnrollmentDate(LocalDate.parse(enrollmentField.getText()));
                }
                student.setSemester(Integer.parseInt(semesterField.getText()));
                student.setBranch(branchField.getText());

                if (studentDAO.addStudent(student)) {
                    JOptionPane.showMessageDialog(this, "Student added successfully!");
                    refreshStudentList((DefaultListModel<String>) ((JList<?>) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView()).getModel());
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding student.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateStudentDialog(String studentId) {
        Student student = studentDAO.getStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField firstNameField = new JTextField(student.getFirstName(), 20);
        JTextField lastNameField = new JTextField(student.getLastName(), 20);
        JTextField emailField = new JTextField(student.getEmail(), 20);
        JTextField phoneField = new JTextField(student.getPhone(), 20);
        JTextField addressField = new JTextField(student.getAddress(), 20);
        JTextField dobField = new JTextField(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "", 20);
        JTextField semesterField = new JTextField(String.valueOf(student.getSemester()), 20);
        JTextField branchField = new JTextField(student.getBranch(), 20);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        panel.add(dobField);
        panel.add(new JLabel("Semester:"));
        panel.add(semesterField);
        panel.add(new JLabel("Branch:"));
        panel.add(branchField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                student.setFirstName(firstNameField.getText());
                student.setLastName(lastNameField.getText());
                student.setEmail(emailField.getText());
                student.setPhone(phoneField.getText());
                student.setAddress(addressField.getText());
                if (!dobField.getText().isEmpty()) {
                    student.setDateOfBirth(LocalDate.parse(dobField.getText()));
                } else {
                    student.setDateOfBirth(null);
                }
                student.setSemester(Integer.parseInt(semesterField.getText()));
                student.setBranch(branchField.getText());

                if (studentDAO.updateStudent(student)) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully!");
                    refreshStudentList((DefaultListModel<String>) ((JList<?>) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView()).getModel());
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating student.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createStudentProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        // For student role: Display logged-in student's details
        panel.add(new JLabel("Profile details coming soon..."));
        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Courses management coming soon..."), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFacultyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Faculty management coming soon..."), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Marks management coming soon..."), BorderLayout.CENTER);
        return panel;
    }
}