package shop.GUI;

import shop.model.Customer;
import shop.service.CartService;
import shop.service.ProductService;
import shop.service.UserService;
import javax.swing.*;
import java.awt.*;

public class CustomerFrame extends JFrame {
    private Customer customer;
    private JLabel balanceLabel;
    private UserService userService;

    public CustomerFrame(Customer customer, ProductService productService, CartService cartService, UserService userService) {
        this.customer = customer;
        this.userService = userService;

        setTitle("Customer Panel - " + customer.getUsername());
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Balance: $" + String.format("%.2f", customer.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        infoPanel.add(balanceLabel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addBalanceBtn = new JButton("Add Balance");
        JButton logoutBtn = new JButton("Logout");
        
        addBalanceBtn.addActionListener(e -> addBalance());
        logoutBtn.addActionListener(e -> logout(productService, cartService));
        
        buttonPanel.add(addBalanceBtn);
        buttonPanel.add(logoutBtn);
        
        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        ProductPanel productPanel = new ProductPanel(productService, cartService, customer);
        CartPanel cartPanel = new CartPanel(cartService, customer, this);
        
        tabs.add("Products", productPanel);
        tabs.add("My Cart", cartPanel);
        
        tabs.addChangeListener(e -> {
            if (tabs.getSelectedIndex() == 1) {
                cartPanel.refresh();
            }
        });
        
        add(tabs, BorderLayout.CENTER);
        setVisible(true);
    }

    private void addBalance() {
        String amountStr = JOptionPane.showInputDialog(this,
            "Enter amount to add:", "Add Balance", JOptionPane.QUESTION_MESSAGE);
        
        if (amountStr != null && !amountStr.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive");
                    return;
                }
                
                customer.setBalance(customer.getBalance() + amount);
                userService.updateUser(customer);
                refreshBalance();
                JOptionPane.showMessageDialog(this, 
                    String.format("$%.2f added to your balance", amount));
                    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        }
    }

    private void logout(ProductService productService, CartService cartService) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame(userService, productService, cartService);
            });
        }
    }

    public void refreshBalance() {
        balanceLabel.setText("Balance: $" + String.format("%.2f", customer.getBalance()));
    }
}
