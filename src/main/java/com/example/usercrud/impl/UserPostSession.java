package com.example.usercrud.impl;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.UserPosts;
import com.example.usercrud.model.Users;
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
public class UserPostSession implements UserPostLocal {

    @Inject
    private EntityManager entityManager;




    @Override
    public UserPosts getPostById(int id) {
        return entityManager.find(UserPosts.class, id);
    }

    @Override
    public List<UserPosts> getAllPosts() {
        TypedQuery<UserPosts> typedQuery =
                entityManager.createQuery(
                        "select p from UserPosts p", UserPosts.class
                );
        return typedQuery.getResultList();

    }

    @Override
    public UserPosts insertPost(UserPosts userPosts) {
        Users owner = entityManager.find(Users.class, userPosts.getOwner().getId());

        userPosts.setOwner(owner);

        entityManager.persist(userPosts);
        return userPosts;
    }


    @Override
    public UserPosts updatePost(int postId, UserPosts newPost) {
        UserPosts userPost = getPostById(postId);

        if (userPost != null ){
            userPost.updateUserPosts(newPost);
            return userPost;
        }
        throw new NotFoundException("Post not found");
    }




    @Override
    public void deleteUserPost(int id) {
        UserPosts userPosts = getPostById(id);
        entityManager.remove(userPosts);
    }
}
