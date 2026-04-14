import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomePageUI extends JFrame {

    private String loggedInUsername;
    private int loggedInUserId;
    private JTable recipeTable;
    private DefaultTableModel tableModel;

    public HomePageUI(String username, int userId) {
        this.loggedInUsername = username;
        this.loggedInUserId = userId;

        // Frame Setup
        setTitle("Kusina ng Pinas - Home");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL (Welcome + Logout) =====
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome back, " + loggedInUsername + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        topPanel.add(logoutBtn, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL (Holds both buttons and table) =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Button Panel (at the top of center area)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton postRecipeBtn = new JButton("📝 Post Recipe");
        JButton updateRecipeBtn = new JButton("✏️ Update Recipe");
        JButton deleteRecipeBtn = new JButton("🗑️ Delete Recipe");

        styleButton(postRecipeBtn, new Color(46, 204, 113));
        styleButton(updateRecipeBtn, new Color(52, 152, 219));
        styleButton(deleteRecipeBtn, new Color(231, 76, 60));

        postRecipeBtn.addActionListener(e -> {
            new PostRecipeUI(this, loggedInUserId);
        });

        updateRecipeBtn.addActionListener(e -> {
            new UpdateRecipeUI(this, loggedInUserId);
        });
        
        deleteRecipeBtn.addActionListener(e -> placeholderAction("Delete Recipe"));

        buttonPanel.add(postRecipeBtn);
        buttonPanel.add(updateRecipeBtn);
        buttonPanel.add(deleteRecipeBtn);

        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        // Recipe Table Panel (in the center of center area)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("All Recipes"));

        String[] columns = {"Recipe Name", "Author", "Difficulty", "Prep Time", "Cook Time", "Date Added"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recipeTable = new JTable(tableModel);
        recipeTable.setRowHeight(25);
        recipeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        recipeTable.getTableHeader().setBackground(new Color(240, 240, 240));

        recipeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        recipeTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        recipeTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        recipeTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        recipeTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        recipeTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(recipeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setForeground(Color.GRAY);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // Load recipes
        refreshRecipeList();

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    private void placeholderAction(String feature) {
        JOptionPane.showMessageDialog(this,
                "✨ " + feature + " feature coming soon!",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshRecipeList() {
        tableModel.setRowCount(0);

        java.util.List<Recipe> recipes = RecipeDAO.getAllRecipes();
        
        if (recipes.isEmpty()) {
            // Add a placeholder row if no recipes exist
            tableModel.addRow(new Object[]{"No recipes yet", "-", "-", "-", "-", "-"});
        } else {
            for (Recipe recipe : recipes) {
                Object[] row = {
                        recipe.getName(),
                        recipe.getAuthorName(),
                        recipe.getDifficulty(),
                        recipe.getPrepTime() + " min",
                        recipe.getCookTime() + " min",
                        recipe.getDateCreated()
                };
                tableModel.addRow(row);
            }
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> LoginUI.main(new String[]{}));
        }
    }
}