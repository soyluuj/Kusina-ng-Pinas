import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO {
    
    private static final String URL = "jdbc:postgresql://localhost:5432/kusina-ng-pinas";
    private static final String USER = "admin";
    private static final String PASSWORD = "8227";
    
    public static List<Region> getAllRegions() {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT region_id, region_name, region_code FROM regions ORDER BY region_name";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                regions.add(new Region(
                    rs.getInt("region_id"),
                    rs.getString("region_name"),
                    rs.getString("region_code")
                ));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return regions;
    }
    
    public static String getRegionNameById(int regionId) {
        String sql = "SELECT region_name FROM regions WHERE region_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, regionId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("region_name");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "Unknown";
    }
}