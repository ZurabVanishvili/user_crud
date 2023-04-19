package com.example.usercrud.service;
import com.example.usercrud.api.UserLocal;
import com.example.usercrud.model.Users;
import com.example.usercrud.response.UserPostsResponse;
import com.example.usercrud.response.UsersResponse;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static com.example.usercrud.service.PostService.getUserPostsResponses;


@RequestScoped
@Path("users")

public class UserService {

    @EJB
    private UserLocal userLocal;


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserResponse(@PathParam("id") int id) {
        UsersResponse usersResponse = getUserResponseById(id);
        return Response.status(Response.Status.OK).entity(usersResponse).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<Users> users = userLocal.getAllUsers();
        List<UsersResponse> response = new ArrayList<>();

        for (Users usersLocal: users){
            UsersResponse usersResponse = getUserResponseById(usersLocal.getId());

//            getUserPosts(usersLocal);

            response.add(usersResponse);
        }
        return Response.status(Response.Status.OK).entity(response).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addUser(Users users) {
        userLocal.insertUser(users);
        return Response.status(Response.Status.CREATED).build();
    }
    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") int id, Users user) {

        userLocal.updateUser(id, user);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") int id) {

        userLocal.deleteUser(id);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    private UsersResponse getUserResponseById(int id) {
        Users user = userLocal.getUserById(id);

        UsersResponse usersResponse = null;

        if (user != null) {
            usersResponse = new UsersResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getMail(),getUserPosts(user));
        }

        return usersResponse;
    }

    private List<UserPostsResponse> getUserPosts(Users user) {

        return getUserPostsResponses(user);
    }

}
