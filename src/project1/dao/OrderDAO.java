package project1.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project1.model.Order;
import project1.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;

public class OrderDAO {
    
    public static ObservableList<Order> getAllOrders() {
        ObservableList<Order> list = FXCollections.observableArrayList();
        String sql = "SELECT o.*, c.full_name as customer_name FROM project_orders o " +
                     "LEFT JOIN project_customers c ON o.customer_id = c.id " +
                     "ORDER BY o.order_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Order(
                    rs.getInt("id"),
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getDate("order_date").toLocalDate(),
                    rs.getInt("created_by"),
                    rs.getDouble("total_amount")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static Order getOrderById(int id) {
        String sql = "SELECT o.*, c.full_name as customer_name FROM project_orders o " +
                     "LEFT JOIN project_customers c ON o.customer_id = c.id " +
                     "WHERE o.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("order_date").toLocalDate(),
                        rs.getInt("created_by"),
                        rs.getDouble("total_amount")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void addOrder(Order order) {
        String sql = "INSERT INTO project_orders(customer_id, order_date, created_by, total_amount) " +
                     "VALUES(?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, order.getCustomerId());
            ps.setDate(2, Date.valueOf(order.getOrderDate()));
            ps.setInt(3, order.getCreatedBy());
            ps.setDouble(4, order.getTotalAmount());
            ps.executeUpdate();
            
            // Lấy ID vừa sinh từ PostgreSQL
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getInt(1)); // Cập nhật ID cho object
                    System.out.println("Generated Order ID: " + order.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }
    
    public static void updateOrder(Order order) {
        String sql = "UPDATE project_orders SET customer_id=?, order_date=?, created_by=?, total_amount=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, order.getCustomerId());
            ps.setDate(2, Date.valueOf(order.getOrderDate()));
            ps.setInt(3, order.getCreatedBy());
            ps.setDouble(4, order.getTotalAmount());
            ps.setInt(5, order.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteOrder(int id) {
        String sql = "DELETE FROM project_orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}