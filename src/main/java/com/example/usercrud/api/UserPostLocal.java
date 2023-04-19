package com.example.usercrud.api;

import com.example.usercrud.model.UserPosts;

import java.util.List;

public interface UserPostLocal {

    UserPosts getPostById(int id);

    List<UserPosts> getAllPosts();

    void insertPost(UserPosts userPosts);

    void updatePost(int postId, UserPosts newPost);

    void deleteUserPost(int id);

}
