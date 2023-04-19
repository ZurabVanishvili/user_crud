package com.example.usercrud.api;

import com.example.usercrud.model.Users;

import java.util.List;

public interface UserLocal {

    List<Users> getAllUsers();

    Users getUserById(int id);

    void insertUser(Users users);

    void updateUser(int id, Users users);

    void deleteUser(int id);


}
