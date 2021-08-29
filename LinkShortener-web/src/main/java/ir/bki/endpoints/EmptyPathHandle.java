package ir.bki.endpoints;

import ir.bki.interceptors.Loggable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import java.io.IOException;

@Path("")
@Loggable
public class EmptyPathHandle {
    @Context
    private HttpServletRequest request;

    @Context
    private HttpServletResponse response;

    @GET
    public String handleEmpty() throws IOException {
        if (request.getRequestURI().equals("") || request.getRequestURI().equals("/") || request.getRequestURI().equals("/administrator.jsp")) {
            response.sendRedirect("/shorten");
        }
        return "Redirected to create new url";
    }
}
