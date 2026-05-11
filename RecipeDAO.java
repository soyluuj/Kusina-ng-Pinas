import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    private static final String URL = "jdbc:postgresql://localhost:5432/kusina-ng-pinas";
    private static final String USER = "admin";
    private static final String PASSWORD = "8227";

    public static List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT r.recipe_id, r.name, r.description, " +
                     "TO_CHAR(r.date_created, 'YYYY-MM-DD') AS date_created, " +
                     "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username, " +
                     "reg.region_name " +
                     "FROM recipes r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "LEFT JOIN regions reg ON r.region_id = reg.region_id " +
                     "ORDER BY r.date_created DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String regionName = rs.getString("region_name");
                if (regionName == null) {
                    regionName = "Unknown";
                }
                
                Recipe recipe = new Recipe(
                        rs.getInt("recipe_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("date_created"),
                        rs.getInt("prep_time"),
                        rs.getInt("cook_time"),
                        rs.getString("difficulty"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        regionName
                );
                recipes.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public static List<Recipe> getRecipesByUser(int userId) {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT r.recipe_id, r.name, r.description, " +
                     "TO_CHAR(r.date_created, 'YYYY-MM-DD') AS date_created, " +
                     "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username, " +
                     "reg.region_name " +
                     "FROM recipes r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "LEFT JOIN regions reg ON r.region_id = reg.region_id " +
                     "WHERE r.user_id = ? " +
                     "ORDER BY r.date_created DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String regionName = rs.getString("region_name");
                    if (regionName == null) {
                        regionName = "Unknown";
                    }
                    
                    Recipe recipe = new Recipe(
                            rs.getInt("recipe_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("date_created"),
                            rs.getInt("prep_time"),
                            rs.getInt("cook_time"),
                            rs.getString("difficulty"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            regionName
                    );
                    recipes.add(recipe);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    public static Recipe getRecipeById(int recipeId) {
        String sql = "SELECT r.recipe_id, r.name, r.description, " +
                     "TO_CHAR(r.date_created, 'YYYY-MM-DD') AS date_created, " +
                     "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username, " +
                     "reg.region_name " +
                     "FROM recipes r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "LEFT JOIN regions reg ON r.region_id = reg.region_id " +
                     "WHERE r.recipe_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String regionName = rs.getString("region_name");
                    if (regionName == null) {
                        regionName = "Unknown";
                    }
                    
                    return new Recipe(
                            rs.getInt("recipe_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("date_created"),
                            rs.getInt("prep_time"),
                            rs.getInt("cook_time"),
                            rs.getString("difficulty"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            regionName
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean addRecipe(String name, String description, int prepTime, 
                                    int cookTime, String difficulty, int userId, int regionId) {
        String sql = "INSERT INTO recipes (name, description, prep_time, cook_time, difficulty, user_id, region_id, date_created) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, prepTime);
            ps.setInt(4, cookTime);
            ps.setString(5, difficulty);
            ps.setInt(6, userId);
            ps.setInt(7, regionId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRecipe(int recipeId, String name, String description, 
                                       int prepTime, int cookTime, String difficulty, 
                                       int userId, int regionId) {
        String checkSql = "SELECT user_id FROM recipes WHERE recipe_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            
            checkPs.setInt(1, recipeId);
            ResultSet rs = checkPs.executeQuery();
            
            if (rs.next()) {
                int ownerId = rs.getInt("user_id");
                if (ownerId != userId) {
                    System.err.println("Security: User " + userId + " attempted to update recipe " + recipeId + " owned by " + ownerId);
                    return false;
                }
            } else {
                return false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        String sql = "UPDATE recipes SET name = ?, description = ?, prep_time = ?, " +
                     "cook_time = ?, difficulty = ?, region_id = ?, date_created = CURRENT_TIMESTAMP " +
                     "WHERE recipe_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, prepTime);
            ps.setInt(4, cookTime);
            ps.setString(5, difficulty);
            ps.setInt(6, regionId);
            ps.setInt(7, recipeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRecipe(int recipeId, int userId) {
        String checkSql = "SELECT user_id, name FROM recipes WHERE recipe_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            
            checkPs.setInt(1, recipeId);
            ResultSet rs = checkPs.executeQuery();
            
            if (rs.next()) {
                int ownerId = rs.getInt("user_id");
                if (ownerId != userId) {
                    System.err.println("Security: User " + userId + " attempted to delete recipe " + recipeId + " owned by " + ownerId);
                    return false;
                }
            } else {
                return false;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        
        String sql = "DELETE FROM recipes WHERE recipe_id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, recipeId);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}