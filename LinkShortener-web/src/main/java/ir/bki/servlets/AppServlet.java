package ir.bki.servlets;

import ir.bki.endpoints.AdminEndPoint;
import ir.bki.endpoints.MaintenanceEndPoint;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("ls")
public class AppServlet extends Application {

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

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(MaintenanceEndPoint.class);
        resources.add(AdminEndPoint.class);
    }
}
