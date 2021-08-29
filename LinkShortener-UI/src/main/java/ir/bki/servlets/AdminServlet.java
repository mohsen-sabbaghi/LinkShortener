package ir.bki.servlets;

import ir.bki.app.AppConstants;
import ir.bki.dao.LinksDao;
import ir.bki.dao.UserDao;
import ir.bki.entities.Links;
import ir.bki.entities.User;
import ir.bki.util.PasswordUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/*"}, displayName = "admin")
public class AdminServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger("basicAuth");

    //-------------------------------------------------------------------------------
    private static boolean isBasicTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.length() > 0 && authorizationHeader.toLowerCase()
                .startsWith("basic");
    }

    public void init() throws ServletException {

    }

    @Override
    public void doGet(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) {

        String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (!isBasicTokenBasedAuthentication(authHeader)) {
            abortWithUnauthorized(httpServletResponse, "The User/Password for basic authentication is not valid.");
            return;
        }

        try {
            verifyUserPass(authHeader, httpRequest);

            List<Links> linksList = getLinksDao().findAll();
            linksList.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
            httpRequest.setAttribute("allLinks", linksList);
            httpRequest.setAttribute("linksCount", (long) linksList.size());
            httpRequest.setAttribute("appVersion", AppConstants.APP_VERSION);
            httpRequest.setAttribute("appBuildDate", AppConstants.BUILD_TIME);

            RequestDispatcher view = httpRequest.getRequestDispatcher("/WEB-INF/jsp/admin.jsp");
            view.forward(httpRequest, httpServletResponse);
        } catch (Exception ex) {
            LOGGER.error("#Exception in basic Filter : " + " ,message: " + ex.getMessage());
            abortWithUnauthorized(httpServletResponse, ex.getMessage());
        }

    }

    //-------------------------------------------------------------------------------
    private UserDao getUserDao() {
        InitialContext ctx;
        UserDao userDao = null;
        try {
            ctx = new InitialContext();
            userDao = (UserDao) ctx.lookup("java:app/LinkShortener-ejb/UserDao");
        } catch (NamingException ex) {
            LOGGER.error("Cannot do the JNDI Lookup to instantiate the userDao service : " + ex);
        }
        return userDao;
    }

    //-------------------------------------------------------------------------------
    private LinksDao getLinksDao() {
        InitialContext ctx;
        LinksDao linksDao = null;
        try {
            ctx = new InitialContext();
            linksDao = (LinksDao) ctx.lookup("java:app/LinkShortener-ejb/LinksDao");
        } catch (NamingException ex) {
            LOGGER.error("Cannot do the JNDI Lookup to instantiate the userDao service : " + ex);
        }
        return linksDao;
    }

    //-------------------------------------------------------------------------------------------------
    private void verifyUserPass(String authHeader, HttpServletRequest request) throws Exception {
        String username = "";
        String password = "";
        try {
            String headerAuthorize = authHeader.substring("Basic".length()).trim();
            String userpass = Base64Coder.decodeString(headerAuthorize);
            String[] userPassArray = userpass.split(":");
            username = userPassArray[0];
            password = userPassArray[1];
        } catch (Exception ex) {
            throw new Exception("invalid input request");
        }
        User user;
        if (AppConstants.Map_User.size() < 1) {
            AppConstants.Map_User = getUserDao().findAllToMap();
        }
        user = AppConstants.Map_User.get(username);

        if (user == null)
            throw new Exception("user '" + username + "' does not found.");
        if (!user.getEnabled())
            throw new Exception("user '" + username + "' is locked.");

        considerUserAndPassword(username, password, user, request);
    }

    //-------------------------------------------------------------------------------------------------
    private void considerUserAndPassword(String username, String password, User user, HttpServletRequest request) throws Exception {


        if (!user.getPassword().equals(PasswordUtils.digestPassword(password))) {
            if (user.getTryCount() == null) user.setTryCount(0);
            if (user.getTryCount() < 3) {
                user.setTryCount(1 + user.getTryCount());
                getUserDao().edit(user, user.getUsername());
                throw new Exception("Invalid username or password. Username: [" + username + "]");
            } else {
                user.setTryCount(1 + user.getTryCount());
                user.setStatus(-1);
                user.setEnabled(false);
                getUserDao().edit(user, user.getUsername());
                throw new Exception("Invalid user/password input exceeded. Username: [" + username + "]");
            }
        } else {//password is correct
            if (user.getTryCount() < 3) {
                user.setTryCount(0);
                user.setEnabled(true);
                user.setStatus(0);
                getUserDao().edit(user, user.getUsername());
                LOGGER.info("Successfully called API with address [" + request.getPathInfo() + "] and [UserName: " + username + "]");
            }
        }
    }

    //-------------------------------------------------------------------------------
    private void abortWithUnauthorized(HttpServletResponse httpServletResponse, String msg) {
        httpServletResponse.setHeader(HttpHeaders.WWW_AUTHENTICATE, "basic");
        try {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, msg);
        } catch (IOException ignored) {
        }

    }

}
