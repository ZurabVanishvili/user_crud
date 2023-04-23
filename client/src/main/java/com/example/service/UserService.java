package com.example.service;
import com.example.usercrud.entity.User;
import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.UserProxySession;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;



@RequestScoped
@Path("users")

public class UserService {


    @Inject
    private UserProxySession userProxySession;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserResponse getUserResponse(@PathParam("id") int id) {
        return userProxySession.getUserById(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserResponse> getAllUsers(
          @DefaultValue("10")  @QueryParam("pageSize") int pageSize,
          @DefaultValue("1")  @QueryParam("pageNumber") int pageNumber) {
        
        int start= (pageNumber -1)* pageSize;
        return userProxySession.getAllUsers(start,pageSize);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getByFirstName")
    public List<UserResponse> getUserByFirstName(
            @QueryParam("firstname") String firstName){
        return userProxySession.getUserByFirstName(firstName);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/addUser")
    public UserResponse addUser(User user) {
        return userProxySession.addUser(user);
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public UserResponse updateUser(@PathParam("id") int id, User user) {

        return userProxySession.updateUser(id, user);

    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserResponse deleteUser(@PathParam("id") int id) {
        return userProxySession.deleteUser(id);
    }

}
