package ir.bki.linkshortener.endpoints;

import ir.bki.linkshortener.dto.LinkDto;
import ir.bki.linkshortener.entities.Links;
import ir.bki.linkshortener.services.LinksServices;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("link")
public class CreateEndPoint {
    @Inject
    private LinksServices linksServices;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String maintenance() {
        return "CreateEndPoint...";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response creteLink(
            @FormParam("link") String link,
            @FormParam("expired-date") String expiredDate,
            @Context UriInfo uriInfo) {
        System.out.println("link = " + link + ", expiredDate = " + expiredDate + ", uriInfo = " + uriInfo.getPath());
        link = link.trim();
        expiredDate = expiredDate.trim();
        if (link.length() > 0 && expiredDate.length() > 0) {
            LinkDto dto = new LinkDto();
            dto.setLongLink(link);
            dto.setExpiresDate(expiredDate);

            Links links = alreadyExist(link);
            if (links == null)
                links = linksServices.createLink(dto);

            URI shortAddress = URI.create(uriInfo.getBaseUri().getScheme()
                    + "://" +
                    uriInfo.getBaseUri().getAuthority() +
                    "/" + links.getShortLink());
            links.setShortLink(String.valueOf(shortAddress));
            return Response.created(shortAddress).entity(links).build();
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).build();
    }

    private Links alreadyExist(String link) {
        Links res = null;
        try {
            res = linksServices.getLinksDao().getEm()
                    .createNamedQuery(Links.FIND_BY_LONG_URL, Links.class)
                    .setParameter("redirectLink", link).getSingleResult();
        } catch (Exception ignored) {
        }
        return res;
    }
}