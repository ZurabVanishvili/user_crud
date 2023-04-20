package com.example.usercrud.service;
import com.example.usercrud.entity.User;
import com.example.usercrud.model.UsersResponse;
import com.example.usercrud.proxy.UserProxySession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;



@RequestScoped
@Path("users")

public class UserService {


    @Inject
    private UserProxySession userProxySession;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UsersResponse getUserResponse(@PathParam("id") int id) {
        return userProxySession.getUserById(id);
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsersResponse> getAllUsers() {

        return userProxySession.getAllUsers();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UsersResponse addUser(User user) {
        return userProxySession.addUser(user);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public UsersResponse updateUser(@PathParam("id") int id, User user) {

        return userProxySession.updateUser(id, user);

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UsersResponse deleteUser(@PathParam("id") int id) {
        return userProxySession.deleteUser(id);
    }

}
