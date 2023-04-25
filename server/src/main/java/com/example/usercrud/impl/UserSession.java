package com.example.usercrud.impl;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Local
@Stateless
//@SuppressWarnings("unused")

public class UserSession implements UserLocal {

    @Inject
    private EntityManager em;

    @Override
    public List<User> getAllUsers(int start, int pageSize,String firstName) {
        TypedQuery<User> users = em.createQuery(
                        "select u from User u where u.firstName = :firstname "
                                , User.class
                ).setFirstResult(start)
                .setMaxResults(pageSize);

        users.setParameter("firstname",firstName);

        return users.getResultList();
    }


    @Override
    public User getUserById(int id) {
        User user = em.find(User.class, id);
        if (user==null){
            throw new NotFoundException("User not found");
        }
        return user;
    }


    @Override
    public User getUserByLogin(String login) {
        try {
            TypedQuery<User> query = em.createQuery(
                    "select u from User u where u.login = :login", User.class
            );

            query.setParameter("login", login);
            return query.getSingleResult();

        } catch (NoResultException e) {
            throw new NotFoundException("Invalid Username");
        }
    }

    @Override
    public List<Comment> getUserComments(int id) {
        try {
            TypedQuery<Comment> query = em.createQuery(
                    "select c from Comment c where c.author.id = :author_id",Comment.class
            );
            query.setParameter("author_id",id);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void insertUser(User users) {
        em.persist(users);
    }

    @Override
    public void updateUser(int id, User users) {
        User presentUsers = getUserById(id);

        if (presentUsers != null) {
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
