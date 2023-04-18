package com.example.usercrud.impl;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.Comment;
import com.example.usercrud.model.UserPosts;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Stateful
@Local
public class CommentSession implements CommentLocal {

    @Inject
    private EntityManager entityManager;



    @Override
    public Comment getCommentById(int id) {
        return entityManager.find(Comment.class,id);
    }

    @Override
    public List<Comment> getAllComments() {
        TypedQuery<Comment> commentTypedQuery=
                entityManager.createQuery(
                        "select c from Comment c ",Comment.class
                );
        return commentTypedQuery.getResultList();
    }

    @Override
    public void addComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public Comment updateComment(int commentId,  Comment comment) {
        Comment localComment = getCommentById(commentId);

        if (localComment!=null){
            localComment.updateComment(comment);
            return localComment;
        }
        throw new NotFoundException("Comment doesn't exist");
    }

    @Override
    public void removeComment(int id) {
        Comment comment = getCommentById(id);
        entityManager.remove(comment);
    }
}
