package project1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(ConfigLoader.get("db.driver"));
        return DriverManager.getConnection(
            ConfigLoader.get("db.url"),
            ConfigLoader.get("db.username"),
            ConfigLoader.get("db.password")
        );
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}