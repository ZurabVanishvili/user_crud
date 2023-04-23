package com.example.usercrud.api;

import com.example.usercrud.entity.User;

import java.util.List;

public interface UserLocal {

    List<User> getAllUsers(int start, int pageSize);

    User getUserById(int id);

    List<User> getUserByFirstName(String firstName);

    User getUserByLogin(String login);

    void insertUser(User users);

    void updateUser(int id, User users);

    void deleteUser(int id);


}
