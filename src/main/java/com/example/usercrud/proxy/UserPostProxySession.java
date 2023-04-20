package com.example.usercrud.proxy;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.model.UserPostsResponse;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserPostProxySession {

    @EJB
    private UserPostLocal userPostLocal;

    @EJB
    private UserLocal userLocal;



    public List<UserPostsResponse> getAllPosts(){
        return getAllPostResponse(userPostLocal.getAllPosts());
    }

    public UserPostsResponse getPostById(int id){
        UserPosts post = userPostLocal.getPostById(id);
        if (post == null) {
            return null;
        }

        List<CommentResponse> commentResponses = new ArrayList<>();


        for (Comment comment : post.getComments()) {
            CommentResponse commentResponse = new CommentResponse(comment.getId(), comment.getCommentContent(), post.getId());
            commentResponses.add(commentResponse);
        }

        return new UserPostsResponse(
                post.getId(), post.getContent(), post.getTitle(),commentResponses
        );
    }

    public UserPostsResponse addPostToUser(int id, UserPosts post){
        User user = userLocal.getUserById(id);
        if (user == null) {
            return null;
        }
        userPostLocal.insertPost(post);
        user.addPost(post);
        return new UserPostsResponse(
                post.getId(), post.getContent(), post.getTitle()
        );
    }


    public UserPostsResponse updatePostOfUser(int id, UserPosts post){
        userPostLocal.updatePost(id, post);

        UserPostsResponse response = getPostById(id);
        return new UserPostsResponse(
                id, post.getContent(),
                post.getTitle(),response.getComments()
        );
    }

    public UserPostsResponse deletePost(int id){
        UserPosts post= userPostLocal.getPostById(id);
        UserPostsResponse response = getPostById(id);

        userPostLocal.deleteUserPost(id);

        return new UserPostsResponse(
                post.getId(), post.getContent(), post.getTitle(),response.getComments());
    }



    public static List<UserPostsResponse> getAllPostResponse(List<UserPosts> posts){

        UserPostsResponse response;
        CommentResponse commentResponse;
        List<CommentResponse> commentResponseList ;
        List<Comment> comments;
        List<UserPostsResponse> postResponses = new ArrayList<>();

        if (posts!=null) {
            for (UserPosts userPostsLocal : posts) {
                comments = userPostsLocal.getComments();
                commentResponseList = new ArrayList<>();

                for (Comment comment : comments) {
                    commentResponse = new CommentResponse(comment.getId(), comment.getCommentContent(), comment.getPosts().getId());
                    commentResponseList.add(commentResponse);
                }
                response = new UserPostsResponse(userPostsLocal.getId(),
                        userPostsLocal.getContent(), userPostsLocal.getTitle(), commentResponseList);


                postResponses.add(response);
            }
        }
        return postResponses;
    }
}

