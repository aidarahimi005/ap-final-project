
package shop.service;

import shop.model.CartItem;
import shop.model.Customer;
import shop.model.Product;
import shop.model.ShoppingCart;
import javax.swing.JOptionPane;

public class CartService {
    private ShoppingCart cart;
    private ProductService productService;
    private UserService userService;

    public CartService(ProductService productService, UserService userService) {
        this.cart = new ShoppingCart();
        this.productService = productService;
        this.userService = userService;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void addToCart(Product product, int quantity) {
        if (product.getStock() >= quantity) {
            cart.addItem(product, quantity);
        }
    }

    public void removeFromCart(Product product) {
        cart.removeItem(product);
    }

    public boolean checkout(Customer customer) {
        double total = cart.calculateTotal();
        
        if (customer.getBalance() < total) {
            JOptionPane.showMessageDialog(null, "Insufficient Balance!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            if (p.getStock() < item.getQuantity()) {
                JOptionPane.showMessageDialog(null, "Not enough stock for: " + p.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            p.setStock(p.getStock() - item.getQuantity());
            productService.updateProduct(p);
        }

        customer.setBalance(customer.getBalance() - total);
        userService.updateUser(customer);
        
        cart.clear();
        return true;
    }
}

