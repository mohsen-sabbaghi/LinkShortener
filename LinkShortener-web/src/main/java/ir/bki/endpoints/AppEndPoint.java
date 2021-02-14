package ir.bki.endpoints;

import ir.bki.app.AppConstants;
import ir.bki.filters.LogReqRes;
import ir.bki.interceptors.Loggable;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("maintenance")
@Loggable
@LogReqRes
public class AppEndPoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getVersion() {
        return String.valueOf(new DateTime().toLocalDateTime());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("version")
    public String ping() {
        return AppConstants.APP_VERSION;
    }
}
