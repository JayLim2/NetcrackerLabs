package database.service;

import entity.User;

public interface UserService {
    User findUserByEmail(String email);

    User findUserById(int id);

    void saveUser(User user);
}

