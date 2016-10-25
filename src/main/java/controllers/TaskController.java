package controllers;

import dao.TaskDAO;
import dao.impl.TaskDAOimpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@Singleton
@Path("/task")
public class TaskController {
    private final ObjectMapper mapper;
    private final TaskDAO taskDAO;

    public TaskController() {
        mapper = new ObjectMapper();
        taskDAO = new TaskDAOimpl();
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("add")
//    public Response add(String jsonString) throws IOException {
//        final HttpResponse response = taskDAO.addTask(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("delete")
//    public Response delete(String jsonString) throws IOException {
//        final HttpResponse response = taskDAO.deleteTask(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("update")
//    public Response update(String jsonString) throws IOException {
//        final HttpResponse response = taskDAO.updateTask(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("sync")
    public Response sync(String jsonString) throws IOException {
        final HttpResponse response = taskDAO.syncTasks(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public Response list(@QueryParam("user") int userId) throws IOException {
        final HttpResponse response = taskDAO.getTasks(userId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }
}
