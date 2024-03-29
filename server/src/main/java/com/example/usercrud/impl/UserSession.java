package com.example.usercrud.impl;

import com.example.usercrud.api.UserLocal;
import com.example.usercrud.entity.Comment;
import com.example.usercrud.entity.User;
import com.example.usercrud.entity.User_;
import jakarta.ejb.Local;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import jakarta.persistence.criteria.*;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@Local
@Stateless
@SuppressWarnings("unused")

public class UserSession implements UserLocal {

    @Inject
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers(int start, int pageSize, String firstName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);

        Root<User> userRoot = query.from(User.class);

        Predicate checkFirstName;

        if (firstName != null) {
            checkFirstName = cb.equal(userRoot.get(User_.FIRST_NAME), firstName);
        } else checkFirstName = cb.conjunction();

        query.select(userRoot).where(checkFirstName);


        TypedQuery<User> users = entityManager.createQuery(query)
                .setFirstResult(start).
                setMaxResults(pageSize);

        return users.getResultList();
    }


    @Override
    public User getUserById(int id) {
        User user = entityManager.find(User.class, id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }


    @Override
    public User getUserByLogin(String login) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
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
        TypedQuery<Comment> query = entityManager.createQuery(
                "select c from Comment c where c.author.id = :author_id", Comment.class
        );
        query.setParameter("author_id", id);
        return query.getResultList();

    }


    @Override
    public void insertUser(User users) {
        entityManager.persist(users);
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
        entityManager.remove(deleteUsers);
    }
}
