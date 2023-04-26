package com.example.usercrud.api;

import com.example.usercrud.entity.Comment;

import java.util.List;

public interface CommentLocal {

    Comment getCommentById(int id);

    List<Comment> getAllComments(int start, int pageSize);

    List<Comment> getAllCommentsOfUser(int id);

    void addComment(Comment comment);

    void updateComment(int commentId, Comment comment);

    void deleteComment(int id);
}
