package database.service;

import entity.User;

public interface UserService {
    User findUserByEmail(String email);

    void saveUser(User user);
}

