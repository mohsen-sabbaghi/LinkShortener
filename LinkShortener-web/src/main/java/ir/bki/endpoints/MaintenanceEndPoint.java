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
public class MaintenanceEndPoint {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getVersion() {
        return "<h3>" + new DateTime().toLocalDateTime() + "</h3>";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("version")
    public String ping() {
        return "<h3> App Version: " + AppConstants.APP_VERSION + "</h3>";
    }
}
