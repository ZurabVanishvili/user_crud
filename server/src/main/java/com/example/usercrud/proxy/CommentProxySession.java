package com.example.usercrud.proxy;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.UserPosts;
import com.example.usercrud.model.CommentResponse;
import com.example.usercrud.model.UserPostsResponse;
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

    public List<CommentResponse> getAllComments(int start, int pageSize) {
        List<Comment> comments = commentLocal.getAllComments(start, pageSize);
        return getCommentResponses(comments);
    }

    public List<CommentResponse> getAllComments() {
        List<Comment> comments = commentLocal.getAllComments();
        return getCommentResponses(comments);
    }

    private List<CommentResponse> getCommentResponses(List<Comment> comments) {
        List<CommentResponse> response = new ArrayList<>();

        for (Comment comment : comments) {

            CommentResponse commentResponse = new CommentResponse(
                    comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
            );
            response.add(commentResponse);
        }
        return response;
    }


    public CommentResponse addComment(int id, Comment comment) {
        UserPosts localPost = userPostLocal.getPostById(id);
        if (localPost == null) {
            throw new NotFoundException("Post not found");
        }

        commentLocal.addComment(comment);
        localPost.addComment(comment);

        return new CommentResponse(
                comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
        );


    }

    public CommentResponse updateComment(int id, Comment comment) {

        commentLocal.updateComment(id, comment);

        Comment response = commentLocal.getCommentById(id);
        return new CommentResponse(
                id, comment.getCommentContent(), response.getPosts().getId()
        );

    }

    public CommentResponse deleteComment(int id) {
        Comment comment = commentLocal.getCommentById(id);
        CommentResponse commentResponse = new CommentResponse(
                comment.getId(), comment.getCommentContent(), comment.getPosts().getId()
        );

        commentLocal.removeComment(id);

        return commentResponse;


    }
}