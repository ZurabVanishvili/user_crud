package com.example.usercrud.proxy;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.model.UserResponse;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CommentProxySession {
    @EJB
    private CommentLocal commentLocal;

    @EJB
    private UserPostLocal userPostLocal;

    @EJB
    private UserLocal userLocal;

    public List<CommentResponse> getAllComments(int start, int pageSize) {
        List<Comment> comments = commentLocal.getAllComments(start, pageSize);
        return getCommentResponses(comments);
    }


    public List<CommentResponse> getCommentResponses(List<Comment> comments) {
        List<CommentResponse> response = new ArrayList<>();

        for (Comment comment : comments) {

            CommentResponse commentResponse = new CommentResponse(
                    comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
            );
            response.add(commentResponse);
        }
        return response;
    }


    public CommentResponse addComment(int id,int user_id, Comment comment) {
        UserPosts localPost = userPostLocal.getPostById(id);
        User user = userLocal.getUserById(user_id);
        if (localPost == null) {
            throw new NotFoundException("Post not found");
        }

        commentLocal.addComment(comment);
        localPost.addComment(comment);
        user.addComment(comment);

        return new CommentResponse(
                comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
        );


    }

    public CommentResponse updateComment(int id, Comment comment, UserResponse user) {
        Comment localComment = commentLocal.getCommentById(id);
        if (localComment==null){
            throw new NotFoundException("Comment doesn't exists");
        }

        for (CommentResponse commentResponse : user.getComments()) {
            if (commentResponse.getId() == id) {
                commentLocal.updateComment(id, comment);
                Comment response = commentLocal.getCommentById(id);
                return new CommentResponse(
                        id, comment.getCommentContent(), response.getPosts().getId()
                );
            }
        }
        try {
            throw new IllegalAccessException("That comment doesn't belong to you");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public CommentResponse deleteComment(int id) {
        Comment comment = commentLocal.getCommentById(id);
        if (comment==null){
            throw new NotFoundException("Comment doesn't exist");
        }
        CommentResponse commentResponse = new CommentResponse(
                comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
        );

        commentLocal.removeComment(id);
        return commentResponse;

    }
}
