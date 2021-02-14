package ir.bki.endpoints;

import ir.bki.dto.LinkDto;
import ir.bki.entities.Links;
import ir.bki.filters.LogReqRes;
import ir.bki.filters.basicauth.BasicAuthNeeded;
import ir.bki.interceptors.Loggable;
import ir.bki.services.LinksServices;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("link")
@Loggable
@LogReqRes
public class ShortenerEndPoint {
    @Inject
    private LinksServices linksServices;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String maintenance() {
        return "Use POST method to create a short link...";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @BasicAuthNeeded
    public Response creteLink(
            @FormParam("longUrl") String longUrl,
            @FormParam("expired-date") String expiredDate,
            @FormParam("desire_link") String desirelink,
            @Context UriInfo uriInfo) {

        if (longUrl != null && expiredDate != null) {
            longUrl = longUrl.trim();
            expiredDate = expiredDate.trim();
            if (longUrl.length() > 0 && expiredDate.length() > 0) {
                LinkDto dto = new LinkDto();
                dto.setLongLink(longUrl);
                dto.setExpiresDate(expiredDate);

                Links links;

                if (desirelink != null && desirelink.length() > 0) {
                    desirelink = desirelink.trim();
                    dto.setDesiredlink(desirelink);
                    links = linksServices.shortUrlAlreadyExist(desirelink);
                    if (links != null) {
                        URI shortAddress = URI.create(uriInfo.getBaseUri().getScheme()
                                + "://" +
                                uriInfo.getBaseUri().getAuthority() +
                                "/" + links.getShortLink());
                        links.setShortLink(String.valueOf(shortAddress));
                        return Response.created(shortAddress).entity(links).build();
                    }
                }

                links = linksServices.longUrlAlreadyExist(longUrl);

                if (links == null)
                    links = linksServices.createLink(dto);

                URI shortAddress = URI.create(uriInfo.getBaseUri().getScheme()
                        + "://" +
                        uriInfo.getBaseUri().getAuthority() +
                        "/" + links.getShortLink());
                links.setShortLink(String.valueOf(shortAddress));
                prepareLinksDto(dto, links);

                return Response.created(shortAddress).entity(dto.toJSON()).header("short_link", shortAddress).build();
            }
        }
        return Response.status(Response.Status.PRECONDITION_FAILED).build();
    }

    private void prepareLinksDto(LinkDto dto, Links links) {
        dto.setShortLink(links.getShortLink());
        dto.setLongLink(links.getRedirectLink());
        dto.setHttpStatusCode(links.getHttpStatusCode());
        dto.setCreatedDate(links.getCreatedDate());
        dto.setActivatedDate(links.getActivatedDate());
        dto.setExpiresDate(String.valueOf(links.getExpiresDate()));
        dto.setDescription(links.getDescription());
    }


}