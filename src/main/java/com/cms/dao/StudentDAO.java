package com.cms.dao;

import com.cms.model.Student;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_id, first_name, last_name, email, phone, " +
                    "address, date_of_birth, enrollment_date, semester, branch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setString(6, student.getAddress());
            stmt.setDate(7, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            stmt.setDate(8, student.getEnrollmentDate() != null ? Date.valueOf(student.getEnrollmentDate()) : Date.valueOf(LocalDate.now()));
            stmt.setInt(9, student.getSemester());
            stmt.setString(10, student.getBranch());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding student: " + e.getMessage());
            return false;
        }
    }
    
    public Student getStudentById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting student: " + e.getMessage());
        }
        return null;
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all students: " + e.getMessage());
        }
        return students;
    }
    
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET first_name=?, last_name=?, email=?, phone=?, " +
                    "address=?, date_of_birth=?, semester=?, branch=? WHERE student_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getAddress());
            stmt.setDate(6, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            stmt.setInt(7, student.getSemester());
            stmt.setString(8, student.getBranch());
            stmt.setString(9, student.getStudentId());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteStudent(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, studentId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getString("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setAddress(rs.getString("address"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            student.setDateOfBirth(dob.toLocalDate());
        }
        
        Date enrollmentDate = rs.getDate("enrollment_date");
        if (enrollmentDate != null) {
            student.setEnrollmentDate(enrollmentDate.toLocalDate());
        }
        
        student.setSemester(rs.getInt("semester"));
        student.setBranch(rs.getString("branch"));
        student.setUserId(rs.getInt("user_id"));
        
        return student;
    }
}