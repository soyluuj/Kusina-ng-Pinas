import javax.swing.*;
import java.awt.*;

public class PostRecipeUI extends JDialog {

    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField prepTimeField;
    private JTextField cookTimeField;
    private JComboBox<String> difficultyCombo;
    
    private int userId;
    private HomePageUI parent;

    public PostRecipeUI(HomePageUI parent, int userId) {
        super(parent, "Post New Recipe", true);
        this.parent = parent;
        this.userId = userId;

        // Dialog Setup
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
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

        add(formPanel, BorderLayout.CENTER);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton postBtn = new JButton("Post Recipe");
        postBtn.setBackground(new Color(46, 204, 113));
        postBtn.setForeground(Color.WHITE);
        postBtn.setFont(new Font("Arial", Font.BOLD, 12));
        postBtn.setFocusPainted(false);
        postBtn.addActionListener(e -> postRecipe());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(postBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void postRecipe() {
        // Get values from form
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

        // Save to database
        boolean success = RecipeDAO.addRecipe(name, description, prepTime, cookTime, difficulty, userId);

        if (success) {
            JOptionPane.showMessageDialog(this, "Recipe posted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.refreshRecipeList();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to post recipe. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}