package shop.Main;

import shop.GUI.LoginFrame;
import shop.service.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        File imagesDir = new File("images");
        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }

        FileStorageService fileStorageService = new FileStorageService();
        UserService userService = new UserService(fileStorageService);
        ProductService productService = new ProductService(fileStorageService);
        CartService cartService = new CartService(productService, userService);

        new LoginFrame(userService, productService, cartService);
    }
}
