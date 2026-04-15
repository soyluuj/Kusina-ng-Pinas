import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class DeleteRecipeUI extends JDialog {

    private JComboBox<RecipeItem> recipeCombo;
    private JButton deleteBtn;
    
    private int userId;
    private HomePageUI parent;
    private List<Recipe> userRecipes;

    public DeleteRecipeUI(HomePageUI parent, int userId) {
        super(parent, "Delete Recipe", true);
        this.parent = parent;
        this.userId = userId;
        this.userRecipes = RecipeDAO.getRecipesByUser(userId);

        // Dialog Setup
        setSize(450, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("Select Recipe to Delete");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Recipe selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        selectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        recipeCombo = new JComboBox<>();
        recipeCombo.setPreferredSize(new Dimension(350, 30));
        
        // Populate combo box with user's recipes
        if (userRecipes.isEmpty()) {
            recipeCombo.addItem(new RecipeItem(null, "You have no recipes to delete"));
            recipeCombo.setEnabled(false);
        } else {
            for (Recipe recipe : userRecipes) {
                recipeCombo.addItem(new RecipeItem(recipe,
                        recipe.getName() + " (" + recipe.getDifficulty() + ")"));
            }
        }
        
        selectionPanel.add(recipeCombo);
        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Info label
        JLabel infoLabel = new JLabel("ℹ️ You can only delete recipes you created");
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Warning label
        JLabel warningLabel = new JLabel("⚠️ This action cannot be undone!");
        warningLabel.setFont(new Font("Arial", Font.BOLD, 12));
        warningLabel.setForeground(new Color(231, 76, 60));
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(warningLabel);

        add(mainPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        deleteBtn = new JButton("Delete Recipe");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setEnabled(!userRecipes.isEmpty());
        deleteBtn.addActionListener(e -> confirmAndDelete());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(deleteBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void confirmAndDelete() {
        RecipeItem selectedItem = (RecipeItem) recipeCombo.getSelectedItem();
        if (selectedItem == null || selectedItem.recipe == null) {
            JOptionPane.showMessageDialog(this, 
                    "Please select a recipe to delete.", 
                    "No Recipe Selected", 
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Recipe recipe = selectedItem.recipe;
        String recipeName = recipe.getName();

        // Confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + recipeName + "\"?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = RecipeDAO.deleteRecipe(recipe.getRecipeId(), userId);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "\"" + recipeName + "\" has been deleted.",
                        "Recipe Deleted",
                        JOptionPane.INFORMATION_MESSAGE);
                parent.refreshRecipeList();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete recipe. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
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