package shop.service;

import shop.model.Product;
import java.util.List;
import java.util.UUID;

public class ProductService {
    private List<Product> products;
    private FileStorageService fileStorageService;

    public ProductService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.products = fileStorageService.loadProducts();
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public void addProduct(String name, double price, int stock, String category, String imagePath) {
        Product product = new Product(UUID.randomUUID().toString(), name, price, stock, category, imagePath);
        products.add(product);
        fileStorageService.saveProducts(products);
    }

    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                break;
            }
        }
        fileStorageService.saveProducts(products);
    }

    public void deleteProduct(Product product) {
        products.removeIf(p -> p.getId().equals(product.getId()));
        fileStorageService.saveProducts(products);
    }
}
