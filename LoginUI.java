import javax.swing.*;
import java.awt.*;

public class LoginUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login");
            frame.setSize(300, 200);
            frame.setLayout(new FlowLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Center the frame on screen
            frame.setLocationRelativeTo(null);

            JTextField userField = new JTextField(15);
            JPasswordField passField = new JPasswordField(15);

            JButton loginBtn = new JButton("Login");
            JButton signupBtn = new JButton("Sign Up");

            frame.add(new JLabel("Username or Email:"));
            frame.add(userField);
            frame.add(new JLabel("Password:"));
            frame.add(passField);
            frame.add(loginBtn);
            frame.add(signupBtn);

            // LOGIN ACTION
            loginBtn.addActionListener(e -> {
                String input = userField.getText().trim();
                String pass = new String(passField.getPassword());

                if (input.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                    return;
                }

                if (UserDAO.login(input, pass)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.dispose(); // Close login window
                    openMainApplication(); // Open your main app here
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username/email or password.");
                }
            });

            // OPEN SIGNUP
            signupBtn.addActionListener(e -> {
                new SignupUI();
            });

            frame.setVisible(true);
        });
    }
    
    // Placeholder
    private static void openMainApplication() {
        JFrame mainFrame = new JFrame("Main Application");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(new JLabel("Welcome to the Main Application!", SwingConstants.CENTER));
        mainFrame.setVisible(true);
    }
}