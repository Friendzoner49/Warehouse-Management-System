package project1.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project1.model.Product;
import project1.util.DBConnection;
import java.sql.*;

public class ProductDAO {
    
    public static ObservableList<Product> getAllProducts() {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM project_products ORDER BY id";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("unit"),
                    rs.getDouble("price"),
                    rs.getInt("stock_qty")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static Product getProductById(int id) {
        String sql = "SELECT * FROM project_products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getDouble("price"),
                        rs.getInt("stock_qty")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void addProduct(String name, String unit, double price, int stockQty) {
        String sql = "INSERT INTO project_products(name, unit, price, stock_qty) VALUES(?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setDouble(3, price);
            ps.setInt(4, stockQty);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
    public static void updateProduct(int id, String name, String unit, double price, int stockQty) {
        String sql = "UPDATE project_products SET name=?, unit=?, price=?, stock_qty=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setDouble(3, price);
            ps.setInt(4, stockQty);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteProduct(int id) {
        String sql = "DELETE FROM project_products WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void updateStock(int productId, int quantity) {
        String sql = "UPDATE project_products SET stock_qty = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}