package com.example.usercrud.impl;

import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.entity.UserPosts;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
    public List<UserPosts> getAllPosts(int start, int pageSize) {
        TypedQuery<UserPosts> typedQuery =
                entityManager.createQuery(
                        "select p from UserPosts p", UserPosts.class
                ).setFirstResult(start)
                        .setMaxResults(pageSize);
        return typedQuery.getResultList();

    }

    @Override
    public void insertPost(UserPosts userPosts) {
        entityManager.persist(userPosts);
    }


    @Override
    public void updatePost(int postId, UserPosts newPost) {
        UserPosts userPost = getPostById(postId);

        if (userPost != null ){
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
