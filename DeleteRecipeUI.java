import javax.swing.*;
import java.awt.*;
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

        setSize(450, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(AppTheme.BACKGROUND);

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(AppTheme.DANGER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        JLabel titleLabel = new JLabel("Delete Recipe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel selectLabel = new JLabel("Select Recipe to Delete:");
        selectLabel.setFont(AppTheme.HEADER_FONT);
        selectLabel.setForeground(AppTheme.TEXT_DARK);
        selectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(selectLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        recipeCombo = new JComboBox<>();
        recipeCombo.setPreferredSize(new Dimension(350, 35));
        recipeCombo.setMaximumSize(new Dimension(350, 35));
        recipeCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        AppTheme.styleComboBox(recipeCombo);
        
        if (userRecipes.isEmpty()) {
            recipeCombo.addItem(new RecipeItem(null, "You have no recipes to delete"));
            recipeCombo.setEnabled(false);
        } else {
            for (Recipe recipe : userRecipes) {
                recipeCombo.addItem(new RecipeItem(recipe,
                    recipe.getName() + " (" + recipe.getDifficulty() + ")"));
            }
        }
        
        mainPanel.add(recipeCombo);
        mainPanel.add(Box.createVerticalStrut(15));

        JLabel infoLabel = new JLabel("ℹYou can only delete recipes you created");
        infoLabel.setFont(AppTheme.SMALL_FONT);
        infoLabel.setForeground(AppTheme.TEXT_LIGHT);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(infoLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel warningLabel = new JLabel("This action cannot be undone!");
        warningLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        warningLabel.setForeground(AppTheme.DANGER);
        warningLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(warningLabel);

        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 15));
        buttonPanel.setBackground(AppTheme.BACKGROUND);

        JButton cancelBtn = new JButton("Cancel");
        AppTheme.styleSecondaryButton(cancelBtn);
        cancelBtn.addActionListener(e -> dispose());

        deleteBtn = new JButton("Delete Recipe");
        AppTheme.styleDangerButton(deleteBtn);
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
            UIUtils.showStyledMessage(this, "Please select a recipe to delete.", "No Recipe Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Recipe recipe = selectedItem.recipe;
        String recipeName = recipe.getName();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + recipeName + "\"?\nThis action cannot be undone.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = RecipeDAO.deleteRecipe(recipe.getRecipeId(), userId);

            if (success) {
                UIUtils.showStyledMessage(this, "\"" + recipeName + "\" has been deleted.", "Recipe Deleted", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshRecipeList();
                dispose();
            } else {
                UIUtils.showStyledMessage(this, "Failed to delete recipe. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
}