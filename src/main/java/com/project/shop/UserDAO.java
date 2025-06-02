package com.project.shop;

import java.util.List;

public interface UserDAO {
    int addUser(User user); // Returns generated ID
    User getUserById(int userId);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int userId);
}