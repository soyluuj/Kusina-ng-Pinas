import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PostRecipeUI extends JDialog {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField prepTimeField;
    private JTextField cookTimeField;
    private JComboBox<String> difficultyCombo;
    private JComboBox<RegionItem> regionCombo;
    
    private int userId;
    private HomePageUI parent;

    public PostRecipeUI(HomePageUI parent, int userId) {
        super(parent, "Post New Recipe", true);
        this.parent = parent;
        this.userId = userId;

        setSize(480, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND);

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(AppTheme.PRIMARY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Post New Recipe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppTheme.BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Recipe Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("Recipe Name:");
        nameLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        nameField = new JTextField(25);
        AppTheme.styleTextField(nameField);
        formPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.5;
        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setFont(AppTheme.BODY_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

        // Prep Time
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel prepLabel = new JLabel("Prep Time (minutes):");
        prepLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(prepLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        prepTimeField = new JTextField(10);
        AppTheme.styleTextField(prepTimeField);
        formPanel.add(prepTimeField, gbc);

        // Cook Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JLabel cookLabel = new JLabel("Cook Time (minutes):");
        cookLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(cookLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cookTimeField = new JTextField(10);
        AppTheme.styleTextField(cookTimeField);
        formPanel.add(cookTimeField, gbc);

        // Difficulty
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        JLabel diffLabel = new JLabel("Difficulty:");
        diffLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(diffLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        AppTheme.styleComboBox(difficultyCombo);
        formPanel.add(difficultyCombo, gbc);

        // Region
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel regionLabel = new JLabel("Recipe Origin:");
        regionLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(regionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        regionCombo = new JComboBox<>();
        AppTheme.styleComboBox(regionCombo);
        loadRegions();
        formPanel.add(regionCombo, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(AppTheme.BACKGROUND);

        JButton cancelBtn = new JButton("Cancel");
        AppTheme.styleSecondaryButton(cancelBtn);
        cancelBtn.addActionListener(e -> dispose());

        JButton postBtn = new JButton("Post Recipe");
        postBtn.setBackground(AppTheme.SUCCESS);
        postBtn.setForeground(Color.WHITE);
        postBtn.setFont(AppTheme.BUTTON_FONT);
        postBtn.setFocusPainted(false);
        postBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        postBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        postBtn.addActionListener(e -> postRecipe());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(postBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadRegions() {
        List<Region> regions = RegionDAO.getAllRegions();
        for (Region region : regions) {
            regionCombo.addItem(new RegionItem(region.getId(), region.getName()));
        }
    }

    private void postRecipe() {
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String prepTimeStr = prepTimeField.getText().trim();
        String cookTimeStr = cookTimeField.getText().trim();
        String difficulty = (String) difficultyCombo.getSelectedItem();
        
        RegionItem selectedRegion = (RegionItem) regionCombo.getSelectedItem();
        int regionId = selectedRegion != null ? selectedRegion.regionId : 17;

        if (name.isEmpty()) {
            UIUtils.showStyledMessage(this, "Please enter a recipe name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int prepTime, cookTime;
        try {
            prepTime = prepTimeStr.isEmpty() ? 0 : Integer.parseInt(prepTimeStr);
            cookTime = cookTimeStr.isEmpty() ? 0 : Integer.parseInt(cookTimeStr);
            
            if (prepTime < 0 || cookTime < 0) {
                UIUtils.showStyledMessage(this, "Time values cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            UIUtils.showStyledMessage(this, "Please enter valid numbers for prep/cook time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = RecipeDAO.addRecipe(name, description, prepTime, cookTime, difficulty, userId, regionId);

        if (success) {
            UIUtils.showStyledMessage(this, "Recipe posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshRecipeList();
            dispose();
        } else {
            UIUtils.showStyledMessage(this, "Failed to post recipe. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RegionItem {
        int regionId;
        String regionName;
        
        RegionItem(int regionId, String regionName) {
            this.regionId = regionId;
            this.regionName = regionName;
        }
        
        @Override
        public String toString() {
            return regionName;
        }
    }
}