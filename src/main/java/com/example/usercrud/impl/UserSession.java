package com.example.usercrud.impl;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.model.Users;
import jakarta.ejb.Local;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Local
@Stateful
@SuppressWarnings("unused")

public class UserSession implements UserLocal {

    @Inject
    private EntityManager em;


    @Override
    public List<Users> getAllUsers() {
        TypedQuery<Users> users = em.createQuery(
                "SELECT u FROM Users u" +
                        "         LEFT JOIN UserPosts up ON u.id = up.owner.id" +
                        "         LEFT JOIN Comment c ON c.posts.id = up.owner.id", Users.class
        );

        return users.getResultList();
    }

    @Override
    public Users getUserById(int id) {
        return em.find(Users.class,id);
    }

    @Override
    public void insertUser(Users users) {
        em.persist(users);
    }

    @Override
    public void updateUser(int id, Users users) {
        Users presentUsers = getUserById(id);

        if (presentUsers !=null){
            presentUsers.updateUser(users);
            return;

        }
        throw new NotFoundException("User not found");

    }

    @Override
    public void deleteUser(int id) {
        Users deleteUsers = getUserById(id);
        em.remove(deleteUsers);
    }
}
