package project1.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project1.model.Invoice;
import project1.util.DBConnection;
import java.sql.*;
import java.time.LocalDate;

public class InvoiceDAO {
    
    public static ObservableList<Invoice> getAllInvoices() {
        ObservableList<Invoice> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM project_invoices ORDER BY issue_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Invoice(
                    rs.getInt("id"),
                    rs.getInt("order_id"),
                    rs.getDate("issue_date").toLocalDate(),
                    rs.getInt("issued_by"),
                    rs.getString("payment_status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static Invoice getInvoiceByOrderId(int orderId) {
        String sql = "SELECT * FROM project_invoices WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Invoice(
                        rs.getInt("id"),
                        rs.getInt("order_id"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getInt("issued_by"),
                        rs.getString("payment_status")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void createInvoice(Invoice invoice) {
        String sql = "INSERT INTO project_invoices(order_id, issue_date, issued_by, payment_status) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, invoice.getOrderId());
            ps.setDate(2, Date.valueOf(invoice.getIssueDate()));
            ps.setInt(3, invoice.getIssuedBy());
            ps.setString(4, invoice.getPaymentStatus());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void updatePaymentStatus(int invoiceId, String status) {
        String sql = "UPDATE project_invoices SET payment_status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, invoiceId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteInvoiceByOrderId(int orderId) {
        String sql = "DELETE FROM project_invoices WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 // Lấy trạng thái thanh toán của đơn
    public static String getOrderPaymentStatus(int orderId) {
        String sql = "SELECT payment_status FROM project_invoices WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("payment_status");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return "NO_INVOICE"; // Mặc định nếu chưa xuất hóa đơn
    }

    // Tạo mới hoặc cập nhật trạng thái thanh toán
    public static void updateOrCreateInvoiceStatus(int orderId, int userId, String status) {
        if ("NO_INVOICE".equals(status)) return; // Không thao tác nếu chọn "Chưa có hóa đơn"
        
        // Kiểm tra đã có hóa đơn chưa
        String checkSql = "SELECT id FROM project_invoices WHERE order_id = ?";
        boolean exists = false;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) { exists = rs.next(); }
        } catch (Exception e) { e.printStackTrace(); }

        if (exists) {
            String updateSql = "UPDATE project_invoices SET payment_status = ? WHERE order_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, status);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            } catch (Exception e) { e.printStackTrace(); }
        } else {
            String insertSql = "INSERT INTO project_invoices(order_id, issue_date, issued_by, payment_status) VALUES(?, CURRENT_DATE, ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, orderId);
                ps.setInt(2, userId);
                ps.setString(3, status);
                ps.executeUpdate();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}