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
    
    private int userId;
    private HomePageUI parent;
    private List<Recipe> userRecipes;

    public UpdateRecipeUI(HomePageUI parent, int userId) {
        super(parent, "Update Recipe", true);
        this.parent = parent;
        this.userId = userId;
        this.userRecipes = RecipeDAO.getRecipesByUser(userId);

        // Dialog Setup
        setSize(500, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL (Recipe Selection) =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        
        topPanel.add(new JLabel("Select Recipe:"));
        
        recipeCombo = new JComboBox<>();
        recipeCombo.setPreferredSize(new Dimension(300, 25));
        
        // Populate combo box with user's recipes
        if (userRecipes.isEmpty()) {
            recipeCombo.addItem(new RecipeItem(null, "No recipes found - Create one first!"));
            recipeCombo.setEnabled(false);
        } else {
            for (Recipe recipe : userRecipes) {
                recipeCombo.addItem(new RecipeItem(recipe, 
                    recipe.getName() + " (Created: " + recipe.getDateCreated() + ")"));
            }
        }
        
        recipeCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadSelectedRecipe();
            }
        });
        
        topPanel.add(recipeCombo);
        add(topPanel, BorderLayout.NORTH);

        // ===== SEPARATOR =====
        JSeparator separator = new JSeparator();
        separator.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(separator, BorderLayout.CENTER);

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // ----- Recipe Name -----
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        formPanel.add(new JLabel("Recipe Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        nameField = new JTextField(25);
        formPanel.add(nameField, gbc);

        // ----- Description -----
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        formPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.5;
        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

        // ----- Prep Time -----
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(new JLabel("Prep Time (minutes):"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        prepTimeField = new JTextField(10);
        formPanel.add(prepTimeField, gbc);

        // ----- Cook Time -----
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Cook Time (minutes):"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        cookTimeField = new JTextField(10);
        formPanel.add(cookTimeField, gbc);

        // ----- Difficulty -----
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Difficulty:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyCombo = new JComboBox<>(difficulties);
        formPanel.add(difficultyCombo, gbc);

        // ===== BUTTON PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton updateBtn = new JButton("Update Recipe");
        updateBtn.setBackground(new Color(52, 152, 219));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 12));
        updateBtn.setFocusPainted(false);
        updateBtn.addActionListener(e -> updateRecipe());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(updateBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.SOUTH);

        // Load first recipe if available
        if (!userRecipes.isEmpty()) {
            loadSelectedRecipe();
        } else {
            clearForm();
        }

        setVisible(true);
    }

    private void loadSelectedRecipe() {
        RecipeItem selectedItem = (RecipeItem) recipeCombo.getSelectedItem();
        if (selectedItem != null && selectedItem.recipe != null) {
            Recipe recipe = selectedItem.recipe;
            
            nameField.setText(recipe.getName());
            descriptionArea.setText(recipe.getDescription());
            prepTimeField.setText(String.valueOf(recipe.getPrepTime()));
            cookTimeField.setText(String.valueOf(recipe.getCookTime()));
            
            // Set difficulty in combo box
            String difficulty = recipe.getDifficulty();
            for (int i = 0; i < difficultyCombo.getItemCount(); i++) {
                if (difficultyCombo.getItemAt(i).equals(difficulty)) {
                    difficultyCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        descriptionArea.setText("");
        prepTimeField.setText("");
        cookTimeField.setText("");
        difficultyCombo.setSelectedIndex(0);
    }

    private void updateRecipe() {
        RecipeItem selectedItem = (RecipeItem) recipeCombo.getSelectedItem();
        if (selectedItem == null || selectedItem.recipe == null) {
            JOptionPane.showMessageDialog(this, "Please select a recipe to update.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String prepTimeStr = prepTimeField.getText().trim();
        String cookTimeStr = cookTimeField.getText().trim();
        String difficulty = (String) difficultyCombo.getSelectedItem();

        // Validation
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a recipe name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int prepTime, cookTime;
        try {
            prepTime = prepTimeStr.isEmpty() ? 0 : Integer.parseInt(prepTimeStr);
            cookTime = cookTimeStr.isEmpty() ? 0 : Integer.parseInt(cookTimeStr);
            
            if (prepTime < 0 || cookTime < 0) {
                JOptionPane.showMessageDialog(this, "Time values cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for prep/cook time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update in database
        boolean success = RecipeDAO.updateRecipe(selectedItem.recipe.getRecipeId(), 
                                                  name, description, prepTime, cookTime, difficulty);

        if (success) {
            JOptionPane.showMessageDialog(this, "Recipe updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshRecipeList();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update recipe. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Inner class for combo box items
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
}