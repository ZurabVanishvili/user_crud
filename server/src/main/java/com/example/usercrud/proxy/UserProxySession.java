package com.example.usercrud.proxy;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.model.UserPostsResponse;
import com.example.usercrud.model.UserResponse;
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

    @Inject
    private CommentProxySession commentProxySession;


    public UserResponse getUserById(int id) {
        User user = userLocal.getUserById(id);

        UserResponse userResponse = null;

        if (user != null) {
            userResponse = new UserResponse(user.getId(), user.getFirstName(),
                    user.getLastName(), user.getMail(),user.getLogin(),user.getPassword(),
                    getUserPosts(user.getPosts()),
                    commentProxySession.getCommentResponses(userLocal.getUserComments(user.getId())));
        }

        return userResponse;
    }

    public List<UserResponse> getAllUsers(int start, int pageSize, String firstName){
        List<User> users = userLocal.getAllUsers(start, pageSize, firstName);
        List<UserResponse> response = new ArrayList<>();

        for (User usersLocal: users){
            UserResponse userResponse = getUserById(usersLocal.getId());
            response.add(userResponse);
        }
        return response;
    }



    public UserResponse getUserByLogin(String login){
        User user = userLocal.getUserByLogin(login);
        return new UserResponse(
                user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),
                user.getLogin(),user.getPassword(),getUserPosts(user.getPosts()),getUserComments(user.getComments()));
    }

    public UserResponse addUser(User user){
        userLocal.insertUser(user);
       return new UserResponse(
               user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),user.getLogin(),user.getPassword(),getUserPosts(user.getPosts()));
    }

    public UserResponse updateUser(int id, User user){
        userLocal.updateUser(id, user);
        return new UserResponse(id, user.getFirstName(), user.getLastName(), user.getMail(),user.getLogin(),user.getPassword(),getUserPosts(user.getPosts()));
    }

    public UserResponse deleteUser(int id){
        User user= userLocal.getUserById(id);
        UserResponse response=
                new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),user.getLogin(), user.getPassword(), getUserPosts(user.getPosts()));
        userLocal.deleteUser(id);
        return response;
    }

    private List<UserPostsResponse> getUserPosts(List<UserPosts> user) {
        return proxySession.getAllPostResponse(user);
    }
    private List<CommentResponse> getUserComments(List<Comment> comments) {
        return commentProxySession.getCommentResponses(comments);
    }

}
