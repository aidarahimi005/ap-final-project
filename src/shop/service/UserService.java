
package shop.service;

import shop.model.Customer;
import shop.model.User;
import java.util.List;

public class UserService {
    private List<User> users;
    private FileStorageService fileStorageService;

    public UserService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
        this.users = fileStorageService.loadUsers();
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean register(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new Customer(username, password, 0.0));
        fileStorageService.saveUsers(users);
        return true;
    }

    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                break;
            }
        }
        fileStorageService.saveUsers(users);
    }
}
