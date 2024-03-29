package ir.bki.filters.basicauth;


import ir.bki.app.AppConstants;
import ir.bki.dao.RoleDao;
import ir.bki.dao.UserDao;
import ir.bki.entities.Role;
import ir.bki.entities.User;
import ir.bki.filters.auhtorize.SecurityContextCustom;
import ir.bki.filters.auhtorize.UserPrincipalDto;
import ir.bki.util.PasswordUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

import static ir.bki.logging.LoggerTypes.BASICAUTH;
import static javax.ws.rs.Priorities.AUTHENTICATION;

/**
 * @Author Mohsen Sabbaghi
 */

@Provider
@Priority(AUTHENTICATION)
@BasicAuthNeeded
public class BasicAuthNeededFilter implements ContainerRequestFilter {
    //TO USE THIS FILTER YOU NEED TO SET BELOW CONFIGURATION
    //WebLogic setting for active BASIC SECURITY
    //the Oracle/Middleware/user_projects/domains/domain_name/config directory.
    //Locate the <security-configuration>
    //<enforce-valid-basic-auth-credentials>false</enforce-valid-basic-auth-credentials>
    private static final Logger LOGGER = Logger.getLogger(BASICAUTH);
    private String USER_NAME;

    //-------------------------------------------------------------------------------
    private static boolean isBasicTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.length() > 0 && authorizationHeader.toLowerCase()
                .startsWith("basic");
    }

    //-------------------------------------------------------------------------------
    @Override
    public void filter(ContainerRequestContext request) {
        String authHeader = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (!isBasicTokenBasedAuthentication(authHeader)) {
            throw new NotAuthorizedException("basic");//Response Message
        }
        try {
            verifyUserPass(authHeader, request);

            //setUserNametoSecurityContext for next filter
            setUserNametoSecurityContext(request, USER_NAME);

        } catch (Exception ex) {
            LOGGER.error("#Exception in basic Filter : " + " ,message: " + ex.getMessage());
            abortWithUnauthorized(request, ex.getMessage());
        }
    }

    private void setUserNametoSecurityContext(ContainerRequestContext request, String user) {
        UserPrincipalDto userPrincipalDto = new UserPrincipalDto(user);// Set User To Security Context :-)
        Role role = new Role();
        try {
            role = getRoleDao().findById(user.trim());
        } catch (Exception e) {
            LOGGER.error("#Cant Find Role: " + e.getMessage());
        }

        userPrincipalDto.setRoles(Collections.singletonList(role.getName()));
        request.setSecurityContext(new SecurityContextCustom(userPrincipalDto, request.getSecurityContext().getAuthenticationScheme()));
    }

    //-------------------------------------------------------------------------------
    private UserDao getUserDao() {
        InitialContext ctx;
        UserDao userDao = null;
        try {
            ctx = new InitialContext();
            userDao = (UserDao) ctx.lookup("java:app/LinkShortener-ejb/UserDao");
//            userDao = (UserDao) ctx.lookup("java:global.LinkShortener-ear.LinkShortener-ejb.UserDao");
//            userDao = (UserDao) ctx.lookup("java:global/LinkShortener-ear_ear/LinkShortener-ejb/UserDao");
        } catch (NamingException ex) {
            LOGGER.error("Cannot do the JNDI Lookup to instantiate the userDao service : " + ex);
        }
        return userDao;
    }

    //-------------------------------------------------------------------------------
    private RoleDao getRoleDao() {
        InitialContext ctx;
        RoleDao roleDao = null;
        try {
            ctx = new InitialContext();
            roleDao = (RoleDao) ctx.lookup("java:app/LinkShortener-ejb/RoleDao");
        } catch (NamingException ex) {
            LOGGER.error("Cannot do the JNDI Lookup to instantiate the RoleDao service : " + ex);
        }
        return roleDao;
    }

    //-------------------------------------------------------------------------------------------------
    private void verifyUserPass(String authHeader, ContainerRequestContext request) throws Exception {
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
        User user = null;
        user = AppConstants.Map_User.get(username);
        if (user == null)
            throw new Exception("user '" + username + "' does not found.");
        if (!user.getEnabled())
            throw new Exception("user '" + username + "' is locked.");
        USER_NAME = username;
        considerUserAndPassword(username, password, user, request);
    }

    //-------------------------------------------------------------------------------------------------
    private void considerUserAndPassword(String username, String password, User user, ContainerRequestContext request) throws Exception {


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
                LOGGER.info("Successfully called API with address [" + request.getUriInfo().getPath() + "] and [UserName: " + username + "]");
            }
        }
    }

    //-------------------------------------------------------------------------------
    private void abortWithUnauthorized(ContainerRequestContext requestContext, String msg) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, "basic")
                        .entity(msg)
                        .build());
    }
}
