package ir.bki.endpoints;

import com.google.gson.Gson;
import ir.bki.dao.LinksDao;
import ir.bki.entities.Links;
import ir.bki.filters.LogReqRes;
import ir.bki.filters.basicauth.BasicAuthNeeded;
import ir.bki.interceptors.Loggable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/manage")
@Loggable
@LogReqRes
public class AdminEndPoint {

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;
    @Inject
    private LinksDao linksDao;

    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    @BasicAuthNeeded
    public String getAllLinks() {
        return new Gson().toJson(linksDao.findAll());
    }

    @Path("remove/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    @BasicAuthNeeded
    public Response removeLink(@PathParam("id") Long id) {
        try {
            linksDao.deleteById(id);
            return Response.ok().entity("Successfully Removed").build();
        } catch (Exception e) {
            return Response.noContent().entity("No Content to Remove").build();
        }
    }

    @Path("disable/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
    @BasicAuthNeeded
    public Response disableLink(@PathParam("id") Long id) {
        try {
            Links newLink = linksDao.findById(id);
            newLink.setEnabled(!newLink.isEnabled());
            linksDao.edit(newLink);
            return Response.ok().entity("Status Successfully Changed to: " + newLink.isEnabled()).build();
        } catch (Exception e) {
            return Response.noContent().entity("No Content to Disable").build();
        }
    }

}