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
                    
                    // Get the user ID
                    int userId = UserDAO.getUserId(input);
                    
                    frame.dispose(); // Close login window
                    
                    // ✅ THIS OPENS YOUR ACTUAL HOME PAGE WITH RECIPE TABLE
                    SwingUtilities.invokeLater(() -> new HomePageUI(input, userId));
                    
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
}