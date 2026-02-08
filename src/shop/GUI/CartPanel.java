package shop.GUI;

import shop.model.CartItem;
import shop.model.Customer;
import shop.service.CartService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CartPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private CartService cartService;
    private Customer customer;
    private JLabel totalLabel;
    private CustomerFrame parentFrame;

    public CartPanel(CartService cartService, Customer customer, CustomerFrame parentFrame) {
        this.cartService = cartService;
        this.customer = customer;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());

        String[] columns = {"Product", "Price", "Qty", "Total"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        totalLabel = new JLabel("Total: $0.0");
        JButton checkoutBtn = new JButton("Checkout / Pay");
        
        checkoutBtn.addActionListener(e -> {
            if (cartService.checkout(customer)) {
                JOptionPane.showMessageDialog(this, "Purchase Successful!");
                refresh();
                parentFrame.refreshBalance();
            }
        });

        bottomPanel.add(totalLabel);
        bottomPanel.add(checkoutBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refresh() {
        model.setRowCount(0);
        for (CartItem item : cartService.getCart().getItems()) {
            model.addRow(new Object[]{
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getTotalPrice()
            });
        }
        totalLabel.setText("Total: $" + cartService.getCart().calculateTotal());
    }
}


