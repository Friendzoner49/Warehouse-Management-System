package project1.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project1.model.Customer;
import project1.util.DBConnection;
import java.sql.*;

public class CustomerDAO {
    
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM project_customers ORDER BY id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Customer(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static void addCustomer(String fullName, String phone, String email, String address) {
        String sql = "INSERT INTO project_customers(full_name, phone, email, address) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    
    public static void updateCustomer(int id, String fullName, String phone, String email, String address) {
        String sql = "UPDATE project_customers SET full_name=?, phone=?, email=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteCustomer(int id) {
        String sql = "DELETE FROM project_customers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Customer getCustomerByPhoneOrEmail(String phoneOrEmail) {
        String sql = "SELECT * FROM project_customers WHERE phone = ? OR email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, phoneOrEmail);
            ps.setString(2, phoneOrEmail);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("id"),              
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}