package com.example.usercrud.impl;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Local
@Stateless
@SuppressWarnings("unused")

public class UserSession implements UserLocal {

    @Inject
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> users = em.createQuery(
                "SELECT u FROM User u" +
                        "         LEFT JOIN UserPosts up ON u.id = up.owner.id" +
                        "         LEFT JOIN Comment c ON c.posts.id = up.owner.id", User.class
        );

        return users.getResultList();
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class,id);
    }

    @Override
    public void insertUser(User users) {
        em.persist(users);
    }

    @Override
    public void updateUser(int id, User users) {
        User presentUsers = getUserById(id);

        if (presentUsers != null){
            presentUsers.updateUser(users);
            return;
        }
        throw new NotFoundException("User not found");

    }

    @Override
    public void deleteUser(int id) {
        User deleteUsers = getUserById(id);
        em.remove(deleteUsers);
    }
}
