import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SignupUI {

    private JComboBox<RegionItem> regionCombo;

    public SignupUI() {
        JFrame frame = new JFrame("Kusina ng Pinas - Sign Up");
        frame.setSize(420, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("📝 Create Account");
        titleLabel.setFont(AppTheme.TITLE_FONT);
        titleLabel.setForeground(AppTheme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Form card
        JPanel formCard = AppTheme.createCard();
        formCard.setLayout(new GridBagLayout());
        formCard.setMaximumSize(new Dimension(340, 350));
        formCard.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("📧 Email");
        emailLabel.setFont(AppTheme.SMALL_FONT);
        emailLabel.setForeground(AppTheme.TEXT_LIGHT);
        formCard.add(emailLabel, gbc);
        
        gbc.gridy = 1;
        JTextField emailField = new JTextField(15);
        AppTheme.styleTextField(emailField);
        formCard.add(emailField, gbc);
        
        // Username
        gbc.gridy = 2;
        JLabel userLabel = new JLabel("👤 Username");
        userLabel.setFont(AppTheme.SMALL_FONT);
        userLabel.setForeground(AppTheme.TEXT_LIGHT);
        formCard.add(userLabel, gbc);
        
        gbc.gridy = 3;
        JTextField userField = new JTextField(15);
        AppTheme.styleTextField(userField);
        formCard.add(userField, gbc);
        
        // Password
        gbc.gridy = 4;
        JLabel passLabel = new JLabel("🔒 Password");
        passLabel.setFont(AppTheme.SMALL_FONT);
        passLabel.setForeground(AppTheme.TEXT_LIGHT);
        formCard.add(passLabel, gbc);
        
        gbc.gridy = 5;
        JPasswordField passField = new JPasswordField(15);
        AppTheme.styleTextField(passField);
        formCard.add(passField, gbc);
        
        // Birthdate
        gbc.gridy = 6;
        JLabel birthLabel = new JLabel("🎂 Birthdate (YYYY-MM-DD)");
        birthLabel.setFont(AppTheme.SMALL_FONT);
        birthLabel.setForeground(AppTheme.TEXT_LIGHT);
        formCard.add(birthLabel, gbc);
        
        gbc.gridy = 7;
        JTextField birthField = new JTextField(10);
        AppTheme.styleTextField(birthField);
        formCard.add(birthField, gbc);
        
        // Region
        gbc.gridy = 8;
        JLabel regionLabel = new JLabel("📍 Region");
        regionLabel.setFont(AppTheme.SMALL_FONT);
        regionLabel.setForeground(AppTheme.TEXT_LIGHT);
        formCard.add(regionLabel, gbc);
        
        gbc.gridy = 9;
        regionCombo = new JComboBox<>();
        AppTheme.styleComboBox(regionCombo);
        loadRegions();
        formCard.add(regionCombo, gbc);
        
        mainPanel.add(formCard);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(AppTheme.BACKGROUND);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton registerBtn = new JButton("Register");
        AppTheme.stylePrimaryButton(registerBtn);
        
        JButton cancelBtn = new JButton("Cancel");
        AppTheme.styleSecondaryButton(cancelBtn);
        
        buttonPanel.add(registerBtn);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel);
        
        // Actions
        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = userField.getText().trim();
            String pass = new String(passField.getPassword());
            String birth = birthField.getText().trim();
            
            RegionItem selectedRegion = (RegionItem) regionCombo.getSelectedItem();
            int regionId = selectedRegion != null ? selectedRegion.regionId : 17;
            
            if (email.isEmpty() || username.isEmpty() || pass.isEmpty() || birth.isEmpty()) {
                UIUtils.showStyledMessage(frame, "Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (!email.contains("@") || !email.contains(".")) {
                UIUtils.showStyledMessage(frame, "Please enter a valid email.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (UserDAO.register(email, username, pass, birth, regionId)) {
                UIUtils.showStyledMessage(frame, "Registered successfully! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } else {
                UIUtils.showStyledMessage(frame, "Error: Username or email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> frame.dispose());
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void loadRegions() {
        List<Region> regions = RegionDAO.getAllRegions();
        for (Region region : regions) {
            regionCombo.addItem(new RegionItem(region.getId(), region.getName()));
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