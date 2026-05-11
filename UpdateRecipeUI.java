import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class UpdateRecipeUI extends JDialog {

    private JComboBox<RecipeItem> recipeCombo;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField prepTimeField;
    private JTextField cookTimeField;
    private JComboBox<String> difficultyCombo;
    private JComboBox<RegionItem> regionCombo;
    
    private int userId;
    private HomePageUI parent;
    private List<Recipe> userRecipes;

    public UpdateRecipeUI(HomePageUI parent, int userId) {
        super(parent, "Update Recipe", true);
        this.parent = parent;
        this.userId = userId;
        this.userRecipes = RecipeDAO.getRecipesByUser(userId);

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND);

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(AppTheme.SECONDARY);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Update Recipe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Top Panel (Recipe Selection)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(AppTheme.BACKGROUND);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        selectionPanel.setBackground(AppTheme.BACKGROUND);
        selectionPanel.add(new JLabel("Select Your Recipe:"));
        
        recipeCombo = new JComboBox<>();
        recipeCombo.setPreferredSize(new Dimension(300, 30));
        AppTheme.styleComboBox(recipeCombo);
        
        if (userRecipes.isEmpty()) {
            UIUtils.showStyledMessage(this,
                "You haven't created any recipes yet.\nUse 'Post Recipe' to create your first recipe!",
                "No Recipes Found",
                JOptionPane.INFORMATION_MESSAGE);
            recipeCombo.addItem(new RecipeItem(null, "You have no recipes to update"));
            recipeCombo.setEnabled(false);
        } else {
            for (Recipe recipe : userRecipes) {
                recipeCombo.addItem(new RecipeItem(recipe,
                    recipe.getName() + " (" + recipe.getDifficulty() + ")"));
            }
        }
        
        recipeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadSelectedRecipe();
            }
        });
        
        selectionPanel.add(recipeCombo);
        topPanel.add(selectionPanel);

        JLabel infoLabel = new JLabel("You can only update recipes you created");
        infoLabel.setFont(AppTheme.SMALL_FONT);
        infoLabel.setForeground(AppTheme.TEXT_LIGHT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        topPanel.add(infoLabel);

        add(topPanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppTheme.BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Recipe Name
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        JLabel regionLabel = new JLabel("Recipe Origin:");
        regionLabel.setFont(AppTheme.BODY_FONT);
        formPanel.add(regionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        regionCombo = new JComboBox<>();
        AppTheme.styleComboBox(regionCombo);
        loadRegions();
        formPanel.add(regionCombo, gbc);

        // Button Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(AppTheme.BACKGROUND);

        JButton cancelBtn = new JButton("Cancel");
        AppTheme.styleSecondaryButton(cancelBtn);
        cancelBtn.addActionListener(e -> dispose());

        JButton updateBtn = new JButton("Update Recipe");
        updateBtn.setBackground(AppTheme.SECONDARY);
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(AppTheme.BUTTON_FONT);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setEnabled(!userRecipes.isEmpty());
        updateBtn.addActionListener(e -> updateRecipe());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(updateBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.SOUTH);

        if (userRecipes.isEmpty()) {
            nameField.setEnabled(false);
            descriptionArea.setEnabled(false);
            prepTimeField.setEnabled(false);
            cookTimeField.setEnabled(false);
            difficultyCombo.setEnabled(false);
            regionCombo.setEnabled(false);
        } else {
            loadSelectedRecipe();
        }

        setVisible(true);
    }

    private void loadRegions() {
        List<Region> regions = RegionDAO.getAllRegions();
        for (Region region : regions) {
            regionCombo.addItem(new RegionItem(region.getId(), region.getName()));
        }
    }

    private void loadSelectedRecipe() {
        RecipeItem selectedItem = (RecipeItem) recipeCombo.getSelectedItem();
        if (selectedItem != null && selectedItem.recipe != null) {
            Recipe recipe = selectedItem.recipe;
            
            nameField.setText(recipe.getName());
            descriptionArea.setText(recipe.getDescription());
            prepTimeField.setText(String.valueOf(recipe.getPrepTime()));
            cookTimeField.setText(String.valueOf(recipe.getCookTime()));
            
            for (int i = 0; i < difficultyCombo.getItemCount(); i++) {
                if (difficultyCombo.getItemAt(i).equals(recipe.getDifficulty())) {
                    difficultyCombo.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < regionCombo.getItemCount(); i++) {
                RegionItem item = regionCombo.getItemAt(i);
                if (item.regionName.equals(recipe.getRegion())) {
                    regionCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void updateRecipe() {
        RecipeItem selectedItem = (RecipeItem) recipeCombo.getSelectedItem();
        if (selectedItem == null || selectedItem.recipe == null) {
            UIUtils.showStyledMessage(this, "Please select a recipe to update.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

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

        boolean success = RecipeDAO.updateRecipe(selectedItem.recipe.getRecipeId(), 
            name, description, prepTime, cookTime, difficulty, userId, regionId);

        if (success) {
            UIUtils.showStyledMessage(this, "Recipe updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshRecipeList();
            dispose();
        } else {
            UIUtils.showStyledMessage(this, "Failed to update recipe. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RecipeItem {
        Recipe recipe;
        String displayText;

        RecipeItem(Recipe recipe, String displayText) {
            this.recipe = recipe;
            this.displayText = displayText;
        }

        @Override
        public String toString() {
            return displayText;
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