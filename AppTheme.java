import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AppTheme {
    
    // Filipino-inspired color palette
    public static final Color PRIMARY = new Color(206, 70, 45);      // Philippine Flag Red
    public static final Color SECONDARY = new Color(0, 56, 168);     // Philippine Flag Blue
    public static final Color ACCENT = new Color(252, 209, 22);      // Philippine Flag Yellow
    public static final Color BACKGROUND = new Color(250, 250, 245); // Warm white
    public static final Color CARD_BG = Color.WHITE;
    public static final Color TEXT_DARK = new Color(51, 51, 51);
    public static final Color TEXT_LIGHT = new Color(120, 120, 120);
    public static final Color SUCCESS = new Color(46, 204, 113);
    public static final Color WARNING = new Color(241, 196, 15);
    public static final Color DANGER = new Color(231, 76, 60);
    
    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    
    // Style methods
    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });
    }
    
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(SECONDARY);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY, 1),
            new EmptyBorder(9, 19, 9, 19)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(240, 245, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
    }
    
    // THIS WAS MISSING - Add it now
    public static void styleDangerButton(JButton button) {
        button.setBackground(DANGER);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER);
            }
        });
    }
    
    public static void styleTextField(JTextField field) {
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }
    
    public static void styleComboBox(JComboBox<?> combo) {
        combo.setFont(BODY_FONT);
        combo.setBackground(Color.WHITE);
    }
    
    public static void styleLabel(JLabel label, Font font, Color color) {
        label.setFont(font);
        label.setForeground(color);
    }
    
    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return card;
    }
}