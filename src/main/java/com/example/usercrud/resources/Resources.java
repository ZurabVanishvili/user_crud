package com.example.usercrud.resources;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;



@RequestScoped
@Stateful
public class Resources {
    @PersistenceContext(name = "userCrud", type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Produces
    public EntityManager getEm() {
        return em;
    }
}
