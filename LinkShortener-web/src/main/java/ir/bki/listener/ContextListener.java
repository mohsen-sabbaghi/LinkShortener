package ir.bki.listener;

import com.sun.net.httpserver.HttpContext;
import ir.bki.app.AppConstants;
import ir.bki.dao.UserDao;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.core.Context;
import java.net.MalformedURLException;

import static ir.bki.app.AppConstants.APP_VERSION;
import static ir.bki.app.AppConstants.BUILD_TIME;

@WebListener
public class ContextListener implements ServletContextListener {
    private final Logger LOGGER = Logger.getRootLogger();
    @Context
    private HttpContext httpContext;
    @Context
    private ServletContext servletContext;
    @Inject
    private UserDao userDao;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            LOGGER.info("log4j initialized From: " + sce.getServletContext().getResource("/WEB-INF/classes/log4j.xml"));
        } catch (MalformedURLException e) {
            LOGGER.warn("Didn't find the log4j.xml Because: " + e.getMessage());
        }
        LOGGER.info("Build Time: " + BUILD_TIME);
        LOGGER.info("App Version: " + APP_VERSION);

        loadUserFromDb();

    }

    public void loadUserFromDb() {
        try {
            long ctm = System.currentTimeMillis();
            AppConstants.Map_User = userDao.findAllToMap();
            long diff = System.currentTimeMillis() - ctm;
            LOGGER.info("[" + String.format("%-6d", diff) + " ms]" + " size: " + String.format("%-3d", AppConstants.Map_User.size()) + " ,Loading Users ...");
        } catch (Exception e) {
            LOGGER.error("##Error in loading Users From Db: " + e.getMessage());
        }
    }
}
