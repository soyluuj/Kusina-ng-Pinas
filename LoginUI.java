import javax.swing.*;
import java.awt.*;

public class LoginUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Kusina ng Pinas - Login");
            frame.setSize(420, 480);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            
            // Main panel with padding
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(AppTheme.BACKGROUND);
            mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            
            // Logo/Title
            JLabel titleLabel = new JLabel("Kusina ng Pinas");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(AppTheme.PRIMARY);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel subtitleLabel = new JLabel("Discover Filipino Recipes");
            subtitleLabel.setFont(AppTheme.BODY_FONT);
            subtitleLabel.setForeground(AppTheme.TEXT_LIGHT);
            subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            mainPanel.add(titleLabel);
            mainPanel.add(Box.createVerticalStrut(5));
            mainPanel.add(subtitleLabel);
            mainPanel.add(Box.createVerticalStrut(30));
            
            // Login card
            JPanel loginCard = AppTheme.createCard();
            loginCard.setLayout(new GridBagLayout());
            loginCard.setMaximumSize(new Dimension(340, 200));
            loginCard.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 10, 5, 10);
            
            // Username field
            gbc.gridx = 0;
            gbc.gridy = 0;
            JLabel userLabel = new JLabel(" Username or Email");
            userLabel.setFont(AppTheme.SMALL_FONT);
            userLabel.setForeground(AppTheme.TEXT_LIGHT);
            loginCard.add(userLabel, gbc);
            
            gbc.gridy = 1;
            JTextField userField = new JTextField(15);
            AppTheme.styleTextField(userField);
            loginCard.add(userField, gbc);
            
            // Password field
            gbc.gridy = 2;
            JLabel passLabel = new JLabel("Password");
            passLabel.setFont(AppTheme.SMALL_FONT);
            passLabel.setForeground(AppTheme.TEXT_LIGHT);
            loginCard.add(passLabel, gbc);
            
            gbc.gridy = 3;
            JPasswordField passField = new JPasswordField(15);
            AppTheme.styleTextField(passField);
            loginCard.add(passField, gbc);
            
            mainPanel.add(loginCard);
            mainPanel.add(Box.createVerticalStrut(20));
            
            // Buttons panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            buttonPanel.setBackground(AppTheme.BACKGROUND);
            buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JButton loginBtn = new JButton("Login");
            AppTheme.stylePrimaryButton(loginBtn);
            
            JButton signupBtn = new JButton("Sign Up");
            AppTheme.styleSecondaryButton(signupBtn);
            
            buttonPanel.add(loginBtn);
            buttonPanel.add(signupBtn);
            
            mainPanel.add(buttonPanel);
            mainPanel.add(Box.createVerticalStrut(15));
            
            // Signup link
            JLabel signupLink = new JLabel("Don't have an account? Sign up here");
            signupLink.setFont(AppTheme.SMALL_FONT);
            signupLink.setForeground(AppTheme.SECONDARY);
            signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
            signupLink.setAlignmentX(Component.CENTER_ALIGNMENT);
            signupLink.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    new SignupUI();
                }
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    signupLink.setText("<html><u>Don't have an account? Sign up here</u></html>");
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    signupLink.setText("Don't have an account? Sign up here");
                }
            });
            
            mainPanel.add(signupLink);
            
            // Login action
            loginBtn.addActionListener(e -> {
                String input = userField.getText().trim();
                String pass = new String(passField.getPassword());
                
                if (input.isEmpty() || pass.isEmpty()) {
                    UIUtils.showStyledMessage(frame, "Please fill all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                loginBtn.setText("Logging in...");
                loginBtn.setEnabled(false);
                
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        return UserDAO.login(input, pass);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            if (get()) {
                                int userId = UserDAO.getUserId(input);
                                frame.dispose();
                                new HomePageUI(input, userId);
                            } else {
                                UIUtils.showStyledMessage(frame, "Invalid username/email or password.", "Error", JOptionPane.ERROR_MESSAGE);
                                loginBtn.setText("Login");
                                loginBtn.setEnabled(true);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            loginBtn.setText("Login");
                            loginBtn.setEnabled(true);
                        }
                    }
                };
                worker.execute();
            });
            
            signupBtn.addActionListener(e -> new SignupUI());
            
            frame.add(mainPanel);
            frame.setVisible(true);
        });
    }
}