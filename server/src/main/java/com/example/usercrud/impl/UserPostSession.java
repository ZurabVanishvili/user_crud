package com.example.usercrud.impl;

import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.entity.*;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Stateless
@Local
@SuppressWarnings("unused")
public class UserPostSession implements UserPostLocal {

    @Inject
    private EntityManager entityManager;

    @Override
    public UserPosts getPostById(int id) {
        return entityManager.find(UserPosts.class, id);
    }

    @Override
    public List<UserPosts> getAllPosts(int start, int pageSize, String firstName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserPosts> query = cb.createQuery(UserPosts.class);

        Root<UserPosts> userPostsRoot = query.from(UserPosts.class);
        Join<UserPosts, User> user = userPostsRoot.join(UserPosts_.OWNER);


        Predicate checkFirstname;

        if (firstName != null) {
            checkFirstname = cb.equal(user.get(User_.FIRST_NAME), firstName);
        } else checkFirstname = cb.conjunction();

        query.multiselect(userPostsRoot).where(checkFirstname);

        TypedQuery<UserPosts> userPostsTypedQuery = entityManager.createQuery(query)
                .setFirstResult(start)
                .setMaxResults(pageSize);
        return userPostsTypedQuery.getResultList();

    }


    @Override
    public List<UserPosts> getPostByCommentSizeAndUsername(int commentSize, String username,String title) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserPosts> query = cb.createQuery(UserPosts.class);

        Root<UserPosts> root = query.from(UserPosts.class);

        Join<UserPosts, User> userJoin = root.join(UserPosts_.OWNER);
        Join<UserPosts, Comment> commentJoin = root.join(UserPosts_.COMMENTS);

        if (username.isBlank()||title.isBlank()||commentSize==0){
            cb.conjunction();
        }
        Predicate usernamePredicate = cb.equal(userJoin.get(User_.LOGIN), username);
        Predicate contentPredicate =  cb.like(root.get(UserPosts_.TITLE),"%"+title+"%");

        Expression<Boolean> commentExpression = cb.ge(cb.count(commentJoin), commentSize);

        query.select(root)
                .where(usernamePredicate,contentPredicate)
                .groupBy(root)
                .having(commentExpression);

        TypedQuery<UserPosts> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }


    @Override
    public void insertPost(UserPosts userPosts) {
        entityManager.persist(userPosts);
    }


    @Override
    public void updatePost(int postId, UserPosts newPost) {
        UserPosts userPost = getPostById(postId);

        if (userPost != null) {
            userPost.updateUserPosts(newPost);
            return;
        }
        throw new NotFoundException("Post not found");
    }


    @Override
    public void deleteUserPost(int id) {
        UserPosts userPosts = getPostById(id);
        entityManager.remove(userPosts);
    }
}
