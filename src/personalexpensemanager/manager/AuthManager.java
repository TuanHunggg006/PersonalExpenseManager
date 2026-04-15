package personalexpensemanager.manager;

import personalexpensemanager.model.User;
import java.util.ArrayList;

public class AuthManager {
    private ArrayList<User> userList;

    public AuthManager() {
        userList = new ArrayList<>();

        userList.add(new User("U01", "user", "123", "Administrator"));
        userList.add(new User("U02", "hung", "123", "Hung Nguyen"));
    }

    public User login(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean register(User newUser) {
        if (newUser == null) return false;

        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(newUser.getUsername())) {
                return false; // trùng username
            }
        }

        userList.add(newUser);
        return true;
    }

    public ArrayList<User> getAllUsers() {
        return userList;
    }
}