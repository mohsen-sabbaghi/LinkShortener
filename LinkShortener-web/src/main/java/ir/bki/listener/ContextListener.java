package ir.bki.listener;

import com.sun.javafx.PlatformUtil;
import com.sun.net.httpserver.HttpContext;
import ir.bki.app.AppConstants;
import ir.bki.dao.RoleDao;
import ir.bki.dao.UserDao;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.ws.rs.core.Context;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Inject
    private RoleDao roleDao;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initializeLog4j(sce);
        try {
            LOGGER.info("log4j initialized From: " + sce.getServletContext().getResource("/WEB-INF/log4j.xml"));
        } catch (MalformedURLException e) {
            LOGGER.warn("Didn't find the log4j.xml Because: " + e.getMessage());
        }

        LOGGER.info("Build Time: " + BUILD_TIME);
        LOGGER.info("App Version: " + APP_VERSION);

        loadUserFromDb();
        loadRole();

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

    public void loadRole() {
        try {
            long ctm = System.currentTimeMillis();
            AppConstants.Map_Role = roleDao.findAllMap();
            long diff = System.currentTimeMillis() - ctm;
            if (AppConstants.Map_Role.size() > 0)
                LOGGER.info("[" + String.format("%-6d", diff) + " ms]" + " size: " + String.format("%-3d", AppConstants.Map_Role.size()) + " ,Loading Roles Map...");
            else
                LOGGER.warn("[" + String.format("%-6d", diff) + " ms]" + " size: " + String.format("%-3d", AppConstants.Map_Role.size()) + " ,Loading Roles Map...");

        } catch (Exception e) {
            LOGGER.error("Exception in loading Role Map! Message is:" + e.getMessage());
        }
//        return AppConstants.Map_Role.size();
    }

    private void initializeLog4j(ServletContextEvent servletContextEvent) {
        String webAppPath = servletContextEvent.getServletContext().getRealPath("/");

        String log4jFilePath = webAppPath + "WEB-INF/log4j.xml";
        if (PlatformUtil.isWindows())
            log4jFilePath = webAppPath + "WEB-INF\\log4j.xml";

        DOMConfigurator.configure(log4jFilePath);
        Path path = Paths.get(log4jFilePath);
        if (Files.exists(path)) {
            System.out.println("#INITIALIZED log4j #Found Log4j.xml in path success: " + log4jFilePath);
        } else
            System.err.println("#INITIALIZED log4j  #Not Found Log4j.xml in path: " + log4jFilePath);
        LOGGER.info("INITIALIZED log4j configuration from file:" + log4jFilePath);
    }

}
