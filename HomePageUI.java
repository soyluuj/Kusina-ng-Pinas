import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HomePageUI extends JFrame {

    private String loggedInUsername;
    private int loggedInUserId;
    private JTable recipeTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> regionFilterCombo;

    public HomePageUI(String username, int userId) {
        this.loggedInUsername = username;
        this.loggedInUserId = userId;

        // Frame Setup
        setTitle("Kusina ng Pinas - Home");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND);

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(AppTheme.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel appTitle = new JLabel("Kusina ng Pinas");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUsername + "!");
        welcomeLabel.setFont(AppTheme.BODY_FONT);
        welcomeLabel.setForeground(new Color(255, 255, 255, 200));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(appTitle);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(welcomeLabel);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(255, 255, 255, 30));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setOpaque(false);

        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(255, 255, 255, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(255, 255, 255, 30));
            }
        });

        logoutBtn.addActionListener(e -> logout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton postRecipeBtn = new JButton(" Post Recipe");
        JButton updateRecipeBtn = new JButton(" Update Recipe");
        JButton deleteRecipeBtn = new JButton(" Delete Recipe");

        // Style Post button (Green/Success)
        AppTheme.stylePrimaryButton(postRecipeBtn);
        postRecipeBtn.setBackground(AppTheme.SUCCESS);

        // Style Update button (Blue/Secondary)
        AppTheme.styleSecondaryButton(updateRecipeBtn);

        // Style Delete button (Red/Danger)
        AppTheme.styleDangerButton(deleteRecipeBtn);

        postRecipeBtn.addActionListener(e -> new PostRecipeUI(this, loggedInUserId));
        updateRecipeBtn.addActionListener(e -> new UpdateRecipeUI(this, loggedInUserId));
        deleteRecipeBtn.addActionListener(e -> new DeleteRecipeUI(this, loggedInUserId));

        buttonPanel.add(postRecipeBtn);
        buttonPanel.add(updateRecipeBtn);
        buttonPanel.add(deleteRecipeBtn);

        centerPanel.add(buttonPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(AppTheme.CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "All Recipes",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            AppTheme.HEADER_FONT,
            AppTheme.TEXT_DARK
        ));

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);
        filterPanel.add(new JLabel("Filter by Region:"));
        regionFilterCombo = new JComboBox<>();
        regionFilterCombo.setFont(AppTheme.BODY_FONT);
        regionFilterCombo.addItem("All Regions");
        loadRegionFilter();
        regionFilterCombo.addActionListener(e -> filterRecipesByRegion());
        filterPanel.add(regionFilterCombo);
        tablePanel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Recipe Name", "Author", "Region", "Difficulty", "Prep Time", "Cook Time", "Date Added"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recipeTable = new JTable(tableModel);
        recipeTable.setRowHeight(30);
        recipeTable.setFont(AppTheme.BODY_FONT);
        recipeTable.getTableHeader().setFont(AppTheme.HEADER_FONT);
        recipeTable.getTableHeader().setBackground(new Color(240, 240, 240));
        recipeTable.setSelectionBackground(new Color(255, 240, 240));

        recipeTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        recipeTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        recipeTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        recipeTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        recipeTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        recipeTable.getColumnModel().getColumn(5).setPreferredWidth(70);
        recipeTable.getColumnModel().getColumn(6).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(recipeTable);
        scrollPane.setPreferredSize(new Dimension(850, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(AppTheme.BACKGROUND);
        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setFont(AppTheme.SMALL_FONT);
        statusLabel.setForeground(AppTheme.TEXT_LIGHT);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        refreshRecipeList();
        setVisible(true);
    }

    private void loadRegionFilter() {
        List<Region> regions = RegionDAO.getAllRegions();
        for (Region region : regions) {
            regionFilterCombo.addItem(region.getName());
        }
    }

    private void filterRecipesByRegion() {
        String selectedRegion = (String) regionFilterCombo.getSelectedItem();
        tableModel.setRowCount(0);
        
        List<Recipe> allRecipes = RecipeDAO.getAllRecipes();
        
        for (Recipe recipe : allRecipes) {
            if (selectedRegion == null || selectedRegion.equals("All Regions") || 
                recipe.getRegion().equals(selectedRegion)) {
                Object[] row = {
                    recipe.getName(),
                    recipe.getAuthorName(),
                    recipe.getRegion(),
                    recipe.getDifficulty(),
                    recipe.getPrepTime() + " min",
                    recipe.getCookTime() + " min",
                    recipe.getDateCreated()
                };
                tableModel.addRow(row);
            }
        }
    }

    public void refreshRecipeList() {
        filterRecipesByRegion();
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