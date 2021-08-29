package ir.bki.servlets;


import ir.bki.endpoints.ShortenerEndPoint;
import ir.bki.filters.auhtorize.RolesAllowedRequestFilter;
import ir.bki.filters.basicauth.BasicAuthNeededFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("create")
public class CreateServlet extends Application {
    @Override
    public Set<Object> getSingletons() {
        return super.getSingletons();
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(BasicAuthNeededFilter.class);
        resources.add(RolesAllowedRequestFilter.class);
        resources.add(ShortenerEndPoint.class);
    }
}
