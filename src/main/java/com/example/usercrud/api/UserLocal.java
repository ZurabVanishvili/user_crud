package com.example.usercrud.api;

import com.example.usercrud.entity.User;

import java.util.List;

public interface UserLocal {

    List<User> getAllUsers();

    User getUserById(int id);

    void insertUser(User users);

    void updateUser(int id, User users);

    void deleteUser(int id);


}
