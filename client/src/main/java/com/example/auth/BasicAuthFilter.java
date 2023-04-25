package com.example.auth;


import com.example.usercrud.model.UserResponse;
import com.example.usercrud.proxy.UserProxySession;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
public class BasicAuthFilter implements ContainerRequestFilter {

    @Inject
    private UserProxySession proxySession;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        if (isAddUserRequest(requestContext)) {
            return;
        }

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            String base64 = authorizationHeader.substring("Basic ".length()).trim();
            byte[] credentials = Base64.getDecoder().decode(base64);
            String loginAndPass = new String(credentials, StandardCharsets.UTF_8);
            String[] splinted = loginAndPass.split(":", 2);

            String login = splinted[0];
            String password = splinted[1];

            UserResponse user = proxySession.getUserByLogin(login);

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                requestContext.setProperty("user", user);
                requestContext.setProperty("posts", user.getPosts());
                requestContext.setProperty("comments",user.getComments());
                return;
            }
        }
        throw new NotAuthorizedException("Invalid username or password");
    }

    private boolean isAddUserRequest(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        return path.endsWith("/addUser");
    }
}
