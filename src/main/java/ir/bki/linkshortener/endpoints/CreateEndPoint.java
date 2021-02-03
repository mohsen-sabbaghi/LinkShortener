package ir.bki.linkshortener.endpoints;

import ir.bki.linkshortener.dto.LinkDto;
import ir.bki.linkshortener.entities.Links;
import ir.bki.linkshortener.services.LinksServices;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

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

        LinkDto dto = new LinkDto();
        dto.setLongLink(link);
        dto.setExpiresDate(expiredDate);

        Links response = linksServices.createLink(dto);

        String shortLink = String.valueOf(response.getShortLink());
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(shortLink);
//        uriBuilder.path(shortLink);
        return Response.created(uriBuilder.build()).entity(uriBuilder.build()).build();
    }
}