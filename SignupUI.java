import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SignupUI {

    private JComboBox<RegionItem> regionCombo;

    public SignupUI() {
        JFrame frame = new JFrame("Sign Up");
        frame.setSize(350, 380); // Increased height for region field
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTextField emailField = new JTextField(15);
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JTextField birthField = new JTextField(10);
        
        // Region dropdown
        JPanel regionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        regionPanel.add(new JLabel("Region:"));
        regionCombo = new JComboBox<>();
        regionCombo.setPreferredSize(new Dimension(200, 25));
        loadRegions();
        regionPanel.add(regionCombo);

        JButton registerBtn = new JButton("Register");

        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Username:"));
        frame.add(userField);
        frame.add(new JLabel("Password:"));
        frame.add(passField);
        frame.add(new JLabel("Birthdate (YYYY-MM-DD):"));
        frame.add(birthField);
        frame.add(regionPanel);
        frame.add(registerBtn);

        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String username = userField.getText().trim();
            String pass = new String(passField.getPassword());
            String birth = birthField.getText().trim();
            
            RegionItem selectedRegion = (RegionItem) regionCombo.getSelectedItem();
            int regionId = selectedRegion != null ? selectedRegion.regionId : 17; // Default to Unknown (17)

            // Basic validation
            if (email.isEmpty() || username.isEmpty() || pass.isEmpty() || birth.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid email.");
                return;
            }

            if (UserDAO.register(email, username, pass, birth, regionId)) {
                JOptionPane.showMessageDialog(frame, "Registered successfully! You can now login.");
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Error: Username or email may already exist.");
            }
        });

        frame.setVisible(true);
    }
    
    private void loadRegions() {
        List<Region> regions = RegionDAO.getAllRegions();
        for (Region region : regions) {
            regionCombo.addItem(new RegionItem(region.getId(), region.getName()));
        }
    }
    
    // Helper class for combo box
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