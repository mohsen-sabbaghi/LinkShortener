package ir.bki.linkshortener.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("maintenance")
public class AppEndPoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String maintenance(){
        return "maintenance...";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("ping")
    public String ping(){
        return "Ping...";
    }
}
