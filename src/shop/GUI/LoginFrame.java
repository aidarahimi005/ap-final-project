package shop.GUI;

import shop.model.Admin;
import shop.model.Customer;
import shop.model.User;
import shop.service.*;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private UserService userService;
    private ProductService productService;
    private CartService cartService;
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame(UserService userService, ProductService productService, CartService cartService) {
        this.userService = userService;
        this.productService = productService;
        this.cartService = cartService;

        setTitle("Shopping Mall - Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        JLabel titleLabel = new JLabel("Welcome to Shopping Mall", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        userField = new JTextField();
        formPanel.add(userField);
        
        formPanel.add(new JLabel("Password:"));
        passField = new JPasswordField();
        formPanel.add(passField);
        
        formPanel.add(new JLabel()); 
        formPanel.add(new JLabel()); 
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");
        
    
        loginBtn.addActionListener(e -> handleLogin());
        registerBtn.addActionListener(e -> handleRegister());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }
        
        User user = userService.login(username, password);
        
        if (user != null) {
            dispose();
            if (user instanceof Admin) {
                
                new AdminFrame((Admin) user, productService, userService);
            } else {
                new CustomerFrame((Customer) user, productService, cartService, userService);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password");
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters");
            return;
        }
        
        if (userService.register(username, password)) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! You can now login.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            
            passField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, 
                "Username already exists", 
                "Registration Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}

