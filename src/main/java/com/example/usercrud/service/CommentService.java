package com.example.usercrud.service;

import com.example.usercrud.api.CommentLocal;
import com.example.usercrud.api.UserPostLocal;
import com.example.usercrud.model.Comment;
import com.example.usercrud.model.UserPosts;
import com.example.usercrud.response.CommentResponse;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Path("comments")
public class CommentService {

    @EJB
    private CommentLocal commentLocal;

    @EJB
    private UserPostLocal userPostLocal;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllComments() {
        List<Comment> comments = commentLocal.getAllComments();
        List<CommentResponse> response =  new ArrayList<>();

        for (Comment comment: comments){

            CommentResponse commentResponse = new CommentResponse(comment.getId(),comment.getCommentContent(),comment.getPosts().getId());
            response.add(commentResponse);
        }
        return Response.status(Response.Status.OK).
                entity(response).build();
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addComment(@PathParam("id") int id, Comment comment) {

        UserPosts post = userPostLocal.getPostById(id);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        commentLocal.addComment(comment);
        post.addComment(comment);

        return Response.status(Response.Status.CREATED).build();
    }

    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateComment(@PathParam("id") int id, Comment comment) {
        commentLocal.updateComment(id, comment);
        return Response.status(Response.Status.CREATED).build();

    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteComment(@PathParam("id") int id) {

        commentLocal.removeComment(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
