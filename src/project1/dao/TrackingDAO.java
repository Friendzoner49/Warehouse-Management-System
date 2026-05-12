package project1.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project1.model.OrderTracking;
import project1.util.DBConnection;
import java.sql.*;

public class TrackingDAO {
    
    // Lấy tất cả tracking của 1 đơn hàng
    public static ObservableList<OrderTracking> getTrackingByOrderId(int orderId) {
        ObservableList<OrderTracking> list = FXCollections.observableArrayList();
        String sql = "SELECT ot.*, p.name as product_name, p.price as unit_price " +
                     "FROM project_order_tracking ot " +
                     "LEFT JOIN project_products p ON ot.product_id = p.id " +
                     "WHERE ot.order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new OrderTracking(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thêm tracking mới
    public static void addTracking(int orderId, int productId, int quantity, String status) {
        String sql = "INSERT INTO project_order_tracking(order_id, product_id, quantity, status) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setString(4, status);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Cập nhật status
    public static void updateTrackingStatus(int trackingId, String status) {
        String sql = "UPDATE project_order_tracking SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, trackingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Xóa tracking
    public static void deleteTracking(int trackingId) {
        String sql = "DELETE FROM project_order_tracking WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trackingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Tính tổng tiền của đơn hàng
    public static double getOrderTotal(int orderId) {
        String sql = "SELECT SUM(p.price * ot.quantity) as total " +
                     "FROM project_order_tracking ot " +
                     "JOIN project_products p ON ot.product_id = p.id " +
                     "WHERE ot.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
 // Thêm method này để xóa tracking khi xóa đơn
    public static void deleteTrackingByOrderId(int orderId) {
        String sql = "DELETE FROM project_order_tracking WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 // Lấy trạng thái giao hàng hiện tại của đơn (lấy trạng thái của item đầu tiên)
    public static String getOrderDeliveryStatus(int orderId) {
        String sql = "SELECT status FROM project_order_tracking WHERE order_id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("status");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "PENDING"; // Mặc định nếu chưa có tracking
    }

    // Cập nhật trạng thái giao cho TẤT CẢ sản phẩm trong đơn
    public static void updateOrderDeliveryStatus(int orderId, String status) {
        String sql = "UPDATE project_order_tracking SET status = ? WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}