package controllers;

import dao.VariantDAO;
import dao.impl.VariantDAOimpl;
import org.codehaus.jackson.map.ObjectMapper;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@SuppressWarnings("OverlyBroadThrowsClause")
@Singleton
@Path("/variant")
public class VariantController {
    private final ObjectMapper mapper;
    private final VariantDAO variantDAO;

    public VariantController() {
        mapper = new ObjectMapper();
        variantDAO = new VariantDAOimpl();
    }

//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("add")
//    public Response add(String jsonString) throws IOException {
//        final HttpResponse response = variantDAO.addVariant(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("delete")
//    public Response delete(String jsonString) throws IOException {
//        final HttpResponse response = variantDAO.deleteVariant(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Path("update")
//    public Response update(String jsonString) throws IOException {
//        final HttpResponse response = variantDAO.updateVariant(jsonString);
//        final String json = mapper.writeValueAsString(response);
//        return Response.ok().entity(json).build();
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("sync")
    public Response sync(String jsonString) throws IOException {
        final HttpResponse response = variantDAO.syncVariants(jsonString);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public Response list(@QueryParam("user") int userId) throws IOException {
        final HttpResponse response = variantDAO.getVariants(userId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tasklist")
    public Response taskList(@QueryParam("task") int taskId) throws IOException {
        final HttpResponse response = variantDAO.getVariantsByTask(taskId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("categorylist")
    public Response categoryList(@QueryParam("category") int categoryId) throws IOException {
        final HttpResponse response = variantDAO.getVariantsByCategory(categoryId);
        final String json = mapper.writeValueAsString(response);
        return Response.ok().entity(json).build();
    }
}
