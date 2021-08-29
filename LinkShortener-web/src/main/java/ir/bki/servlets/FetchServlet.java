package ir.bki.servlets;

import ir.bki.endpoints.EmptyPathHandle;
import ir.bki.endpoints.FetchEndPoint;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/")
public class FetchServlet extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(EmptyPathHandle.class);
        resources.add(FetchEndPoint.class);
    }
}
