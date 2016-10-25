package controllers;

import dao.TaskDAO;
import dao.TaskVariantDAO;
import dao.impl.TaskDAOimpl;
import dao.impl.TaskVariantDAOimpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@Singleton
@Path("/task-variant")
public class TaskVariantController {
    private final ObjectMapper mapper;
    private final TaskVariantDAO taskVariantDAO;

    public TaskVariantController() {
        mapper = new ObjectMapper();
        taskVariantDAO = new TaskVariantDAOimpl();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("sync")
    public Response sync(String jsonString) throws IOException {
        final HttpResponse response = taskVariantDAO.syncTaskVariant(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public Response list(@QueryParam("user") int userId) throws IOException {
        final HttpResponse response = taskVariantDAO.getTaskVariant(userId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }
}
