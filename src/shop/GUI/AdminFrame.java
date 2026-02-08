package shop.GUI;

import shop.model.Admin;
import shop.service.FileStorageService;
import shop.service.ProductService;
import shop.service.UserService;
import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {
    private ProductService productService;
    private UserService userService;

    public AdminFrame(Admin admin, ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
        
        setTitle("Admin Panel - " + admin.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Administrator Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutBtn = new JButton("Logout");
        
        logoutBtn.addActionListener(e -> logout());
        
        buttonPanel.add(logoutBtn);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Manage Products", new ProductPanel(productService, null, admin));
        
        add(tabs, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Logged in as Administrator");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                FileStorageService fileStorageService = new FileStorageService();
                UserService newUserService = new UserService(fileStorageService);
                ProductService newProductService = new ProductService(fileStorageService);
                shop.service.CartService newCartService = new shop.service.CartService(newProductService, newUserService);
                new LoginFrame(newUserService, newProductService, newCartService);
            });
        }
    }
}

