import javax.swing.*;
import java.awt.*;

public class SignupUI {

    public SignupUI() {
        JFrame frame = new JFrame("Sign Up");
        frame.setSize(300, 300);
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Only close this window
        frame.setLocationRelativeTo(null);

        JTextField emailField = new JTextField(15);
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JTextField birthField = new JTextField(10); // YYYY-MM-DD

        JButton registerBtn = new JButton("Register");

        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Username:"));
        frame.add(userField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);
        frame.add(new JLabel("Birthdate (YYYY-MM-DD):"));
        frame.add(birthField);
        frame.add(registerBtn);

        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = userField.getText().trim();
            String pass = new String(passField.getPassword());
            String birth = birthField.getText().trim();

            if (email.isEmpty() || username.isEmpty() || pass.isEmpty() || birth.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            // Email format check
            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email.");
                return;
            }

            if (UserDAO.register(email, username, pass, birth)) {
                JOptionPane.showMessageDialog(frame, "Registered successfully! You can now login.");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Username or email may already exist.");
            }
        });

        frame.setVisible(true);
    }
}