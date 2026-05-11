import javax.swing.*;
import java.awt.*;

public class UIUtils {
    
    public static JDialog createLoadingDialog(JFrame parent, String message) {
        JDialog loadingDialog = new JDialog(parent, "Please wait...", true);
        loadingDialog.setSize(250, 100);
        loadingDialog.setLocationRelativeTo(parent);
        loadingDialog.setUndecorated(true);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(AppTheme.PRIMARY, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(AppTheme.BODY_FONT);
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setForeground(AppTheme.PRIMARY);
        
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        
        loadingDialog.add(panel);
        return loadingDialog;
    }
    
    public static void showStyledMessage(Component parent, String message, String title, int messageType) {
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
}