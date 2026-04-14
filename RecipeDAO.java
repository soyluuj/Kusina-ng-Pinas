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
                     "TO_CHAR(r.date_created, 'YYYY-MM-DD') as date_created, " +
                     "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username " +
                     "FROM recipes r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "ORDER BY r.date_created DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("recipe_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("date_created"),
                        rs.getInt("prep_time"),
                        rs.getInt("cook_time"),
                        rs.getString("difficulty"),
                        rs.getInt("user_id"),
                        rs.getString("username")
                );
                recipes.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }
    
    // Add this method to RecipeDAO.java
    public static boolean addRecipe(String name, String description, int prepTime, 
                                    int cookTime, String difficulty, int userId) {
        String sql = "INSERT INTO recipes (name, description, prep_time, cook_time, difficulty, user_id, date_created) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, prepTime);
            ps.setInt(4, cookTime);
            ps.setString(5, difficulty);
            ps.setInt(6, userId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get recipes created by a specific user (for update/delete dropdown)
    public static List<Recipe> getRecipesByUser(int userId) {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT r.recipe_id, r.name, r.description, " +
                    "TO_CHAR(r.date_created, 'YYYY-MM-DD') as date_created, " +
                    "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.user_id = ? " +
                    "ORDER BY r.date_created DESC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = new Recipe(
                            rs.getInt("recipe_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("date_created"),
                            rs.getInt("prep_time"),
                            rs.getInt("cook_time"),
                            rs.getString("difficulty"),
                            rs.getInt("user_id"),
                            rs.getString("username")
                    );
                    recipes.add(recipe);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recipes;
    }

    // Get a single recipe by ID (for loading into update form)
    public static Recipe getRecipeById(int recipeId) {
        String sql = "SELECT r.recipe_id, r.name, r.description, " +
                    "TO_CHAR(r.date_created, 'YYYY-MM-DD') as date_created, " +
                    "r.prep_time, r.cook_time, r.difficulty, r.user_id, u.username " +
                    "FROM recipes r " +
                    "JOIN users u ON r.user_id = u.id " +
                    "WHERE r.recipe_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Recipe(
                            rs.getInt("recipe_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("date_created"),
                            rs.getInt("prep_time"),
                            rs.getInt("cook_time"),
                            rs.getString("difficulty"),
                            rs.getInt("user_id"),
                            rs.getString("username")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update an existing recipe
    public static boolean updateRecipe(int recipeId, String name, String description, 
                                    int prepTime, int cookTime, String difficulty) {
        String sql = "UPDATE recipes SET name = ?, description = ?, prep_time = ?, " +
                    "cook_time = ?, difficulty = ?, date_created = CURRENT_TIMESTAMP " +
                    "WHERE recipe_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, prepTime);
            ps.setInt(4, cookTime);
            ps.setString(5, difficulty);
            ps.setInt(6, recipeId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}