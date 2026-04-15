import java.sql.*;

public class UserDAO {

    static String url = "jdbc:postgresql://localhost:5432/kusina-ng-pinas";
    static String user = "admin";
    static String password = "8227";

    // Registration/Signup
    // Update this method in UserDAO.java
    public static boolean register(String email, String username, String pass, String birthdate, int regionId) {
        String sql = "INSERT INTO users(email, username, password, birthdate, region_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);
            ps.setString(3, pass);
            
            try {
                ps.setDate(4, Date.valueOf(birthdate));
            } catch (IllegalArgumentException ex) {
                System.err.println("Invalid date format: " + birthdate);
                return false;
            }
            
            ps.setInt(5, regionId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.err.println("Duplicate username or email: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    // Login
    public static boolean login(String input, String pass) {
        String sql = "SELECT * FROM users WHERE (email = ? OR username = ?) AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, input);
            ps.setString(2, input);
            ps.setString(3, pass);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if match found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static int getUserId(String usernameOrEmail) {
        String sql = "SELECT id FROM users WHERE username = ? OR email = ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1; // User not found
    }
}