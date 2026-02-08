
package shop.GUI;

import shop.model.Product;
import shop.model.User;
import shop.service.CartService;
import shop.service.ProductService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private ProductService productService;
    private CartService cartService;
    private User currentUser;

    public ProductPanel(ProductService productService, CartService cartService, User user) {
        this.productService = productService;
        this.cartService = cartService;
        this.currentUser = user;
        
        setLayout(new BorderLayout(10, 10));
        
        String[] columns = {"ID", "Name", "Price", "Stock", "Category"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        if (currentUser.getRole().equals("ADMIN")) {
            JButton addBtn = new JButton("Add");
            JButton editBtn = new JButton("Edit");
            JButton deleteBtn = new JButton("Delete");
            JButton refreshBtn = new JButton("Refresh");
            
            addBtn.addActionListener(e -> addProduct());
            editBtn.addActionListener(e -> editProduct());
            deleteBtn.addActionListener(e -> deleteProduct());
            refreshBtn.addActionListener(e -> loadData());
            
            buttonPanel.add(addBtn);
            buttonPanel.add(editBtn);
            buttonPanel.add(deleteBtn);
            buttonPanel.add(refreshBtn);
        } else {
            JButton addToCartBtn = new JButton("Add to Cart");
            JButton refreshBtn = new JButton("Refresh");
            
            addToCartBtn.addActionListener(e -> addToCart());
            refreshBtn.addActionListener(e -> loadData());
            
            buttonPanel.add(addToCartBtn);
            buttonPanel.add(refreshBtn);
        }
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadData();
    }
    
    private void loadData() {
        model.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            model.addRow(new Object[]{
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock(),
                p.getCategory()
            });
        }
    }
    
    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField categoryField = new JTextField();
        
        Object[] fields = {
            "Name:", nameField,
            "Price:", priceField,
            "Stock:", stockField,
            "Category:", categoryField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, 
            "Add Product", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String category = categoryField.getText();
                
                productService.addProduct(name, price, stock, category, null);
                loadData();
                JOptionPane.showMessageDialog(this, "Product added");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }
    
    private void editProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        Product product = productService.getAllProducts().stream()
            .filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        
        if (product == null) return;
        
        JTextField nameField = new JTextField(product.getName());
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()));
        JTextField stockField = new JTextField(String.valueOf(product.getStock()));
        JTextField categoryField = new JTextField(product.getCategory());
        
        Object[] fields = {
            "Name:", nameField,
            "Price:", priceField,
            "Stock:", stockField,
            "Category:", categoryField
        };
        
        int result = JOptionPane.showConfirmDialog(this, fields, 
            "Edit Product", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String category = categoryField.getText();
                
                Product updated = new Product(
                    product.getId(), name, price, stock, category, product.getImagePath()
                );
                
                productService.updateProduct(updated);
                loadData();
                JOptionPane.showMessageDialog(this, "Product updated");
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }
    
    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        Product product = productService.getAllProducts().stream()
            .filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        
        if (product == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete " + product.getName() + "?", 
            "Confirm", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            productService.deleteProduct(product);
            loadData();
            JOptionPane.showMessageDialog(this, "Product deleted");
        }
    }
    
    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product");
            return;
        }
        
        String id = (String) model.getValueAt(row, 0);
        Product product = productService.getAllProducts().stream()
            .filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        
        if (product == null) return;
        
        String qtyStr = JOptionPane.showInputDialog(this, 
            "Enter quantity for " + product.getName() + ":");
        
        if (qtyStr != null) {
            try {
                int qty = Integer.parseInt(qtyStr);
                cartService.addToCart(product, qty);
                JOptionPane.showMessageDialog(this, "Added to cart");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid quantity");
            }
        }
    }
}
