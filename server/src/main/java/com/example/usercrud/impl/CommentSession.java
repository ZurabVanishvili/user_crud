package com.example.usercrud.impl;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import jakarta.ejb.EJB;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Stateless
@Local
@SuppressWarnings("unused")
public class CommentSession implements CommentLocal {

    @Inject
    private EntityManager entityManager;

    @EJB
    private UserLocal userLocal;

    @Override
    public Comment getCommentById(int id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    public List<Comment> getAllComments(int start, int pageSize) {
        TypedQuery<Comment> commentTypedQuery =
                entityManager.createQuery(
                        "select c from Comment c ", Comment.class
                ).setFirstResult(start).setMaxResults(pageSize);
        return commentTypedQuery.getResultList();
    }

    @Override
    public List<Comment> getAllCommentsOfUser(int id) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Comment> criteriaQuery = cb.createQuery(Comment.class);
        Root<Comment> root = criteriaQuery.from(Comment.class);

        Join<Comment, User> userJoin = root.join("author");
        criteriaQuery.select(root)
                .where(cb.equal(userJoin.get("id"), id));

        TypedQuery<Comment> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();

    }


    @Override
    public void addComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public void updateComment(int commentId, Comment comment) {
        Comment localComment = getCommentById(commentId);

        if (localComment != null) {
            localComment.updateComment(comment);
            return;
        }
        throw new NotFoundException("Comment doesn't exist");
    }

    @Override
    public void deleteComment(int id, int user_id) {
        Comment comment = getCommentById(id);
        entityManager.remove(comment);
    }


}
