package ir.bki.endpoints;

import ir.bki.entities.Links;
import ir.bki.filters.LogReqRes;
import ir.bki.interceptors.Loggable;
import ir.bki.producers.HashThisProducer;
import ir.bki.services.LinksServices;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/{url}")
@Loggable
@LogReqRes
public class FetchEndPoint {

    @Inject
    public HashThisProducer hashids;

    @Inject
    private LinksServices linksServices;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response fetchUrl(@PathParam("url") String url) {

        Links linksToRedirect = linksServices.shortUrlAlreadyExist(url);

        if (linksToRedirect == null) {
            try {
                linksToRedirect = linksServices.retrieveOne(hashids.getHashThisInstance().decode(url.trim())[0]);
            } catch (Exception e) {
                return Response.status(404).entity("URL Not Found").build();
            }
        }
        if (linksServices.isExpired(linksToRedirect)) {
            return Response.status(410).entity("Requested URL expired or no longer available").build();
        }
        return Response.seeOther(URI.create(linksToRedirect.getRedirectLink())).build();
    }

}
