package controllers;

import dao.UserDAO;
import dao.impl.UserDAOimpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@Singleton
@Path("/user")
public class UserController {
    private final ObjectMapper mapper;
    private final UserDAO userDAO;

    public UserController() {
        mapper = new ObjectMapper();
        userDAO = new UserDAOimpl();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("signup")
    public Response signUp(String jsonString) throws IOException {
        final HttpResponse response = userDAO.signUp(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("signin")
    public Response signIn(String jsonString) throws IOException {
        final HttpResponse response = userDAO.signIn(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }
}
