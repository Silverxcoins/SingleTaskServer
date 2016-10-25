package controllers;

import dao.CategoryDAO;
import dao.impl.CategoryDAOimpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@Singleton
@Path("/category")
public class CategoryController {
    private final ObjectMapper mapper;
    private final CategoryDAO categoryDAO;

    public CategoryController() {
        mapper = new ObjectMapper();
        categoryDAO = new CategoryDAOimpl();
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("add")
//    public Response add(String jsonString) throws IOException {
//        final HttpResponse response = categoryDAO.addCategory(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("delete")
//    public Response delete(String jsonString) throws IOException {
//        final HttpResponse response = categoryDAO.deleteCategory(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("update")
//    public Response update(String jsonString) throws IOException {
//        final HttpResponse response = categoryDAO.updateCategory(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("sync")
    public Response update(String jsonString) throws IOException {
        final HttpResponse response = categoryDAO.syncCategories(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public Response list(@QueryParam("user") int userId) throws IOException {
        final HttpResponse response = categoryDAO.getCategories(userId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }
}