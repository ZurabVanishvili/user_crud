package com.example.usercrud.proxy;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.User;
import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.UserPostsResponse;
import com.example.usercrud.model.UsersResponse;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserProxySession {

    @EJB
    private UserLocal userLocal;

    @Inject
    private UserPostProxySession proxySession;

    public UsersResponse getUserById(int id) {
        User user = userLocal.getUserById(id);

        UsersResponse usersResponse = null;

        if (user != null) {
            usersResponse = new UsersResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),getUserPosts(user.getPosts()));
        }

        return usersResponse;
    }

    public List<UsersResponse> getAllUsers(int start, int pageSize){
        List<User> users = userLocal.getAllUsers(start, pageSize);
        List<UsersResponse> response = new ArrayList<>();

        for (User usersLocal: users){
            UsersResponse usersResponse = getUserById(usersLocal.getId());
            response.add(usersResponse);
        }
        return response;
    }

    public UsersResponse addUser(User user){
        userLocal.insertUser(user);
       return new UsersResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),getUserPosts(user.getPosts()));
    }

    public UsersResponse updateUser(int id, User user){
        userLocal.updateUser(id, user);
        return new UsersResponse(id, user.getFirstName(), user.getLastName(), user.getMail(),getUserPosts(user.getPosts()));
    }

    public UsersResponse deleteUser(int id){
        User user= userLocal.getUserById(id);
        UsersResponse response=
                new UsersResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),getUserPosts(user.getPosts()));
        userLocal.deleteUser(id);
        return response;
    }

    private List<UserPostsResponse> getUserPosts(List<UserPosts> user) {
        return proxySession.getAllPostResponse(user);
    }

}
