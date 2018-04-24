package database.service;

import entity.Publisher;
import entity.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    void delete(User user);

    User getByName(String login);

    User getByID(int id);

    User editUser(User user);

    List<User> getAll();

}
