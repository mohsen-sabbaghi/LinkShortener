package ir.bki.linkshortener.endpoints;

import ir.bki.linkshortener.entities.Links;
import ir.bki.linkshortener.services.LinksServices;
import ir.bki.linkshortener.util.BaseConversion;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("")
public class FetchEndPoint {

    @Inject
    private LinksServices linksServices;
    @Inject
    private BaseConversion baseConversion;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/{url}")
    public Response fetchLink(@PathParam("url") String url) {
        Links linksToRedirect = linksServices.retrieveOne(baseConversion.decode(url));
        if (linksToRedirect == null) {
            return Response.status(404).entity("URL Not Found").build();
        }
        return Response.seeOther(URI.create(linksToRedirect.getRedirectLink())).build();
    }

}
