package ir.bki.filters.auhtorize;


import org.apache.log4j.Logger;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.Priorities.AUTHORIZATION;

/**
 * @author Mahdi Sharifi, cassiomolin
 * @version 1.0.0
 * https://www.linkedin.com/in/mahdisharifi/
 * @since 23/11/2019
 */
@Priority(AUTHORIZATION + 20) // authorization filter - should go after any authentication filters
public class RolesAllowedRequestFilter implements ContainerRequestFilter {

    private final boolean denyAll;
    private final List<RoleEnum> rolesAllowed;
    private final Logger LOGGER = Logger.getLogger("authorization");
    @Context
    private ResourceInfo resourceInfo;

    @Context
    private UriInfo uriInfo;

    public RolesAllowedRequestFilter() {
        LOGGER.info("##RolesAllowedRequestFilter-> " + resourceInfo + " ,uriInfo: " + uriInfo);
        this.denyAll = false;
//        this.rolesAllowed = null;
        ArrayList<RoleEnum> list = new ArrayList<>();
        list.add(RoleEnum.admin);
        this.rolesAllowed = list;
    }


    private static boolean isAuthenticated(final ContainerRequestContext requestContext) {
        return requestContext.getSecurityContext().getUserPrincipal() != null;
    }

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        Principal principal = requestContext.getSecurityContext().getUserPrincipal();
        UserPrincipalDto user = (UserPrincipalDto) principal;

        LOGGER.info("RolesAllowedRequestFilter: " + user + "" + " #rolesAllowed: " + rolesAllowed + " ,denyAll: " + denyAll);
        if (!denyAll) {
            if (rolesAllowed != null && rolesAllowed.size() > 0 && !isAuthenticated(requestContext)) {
//                throw new AccessDeniedException(LocalizationMessages.USER_NOT_AUTHORIZED());//ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
                throw new AccessDeniedException("USER_NOT_AUTHORIZED()");//ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
            }
            assert rolesAllowed != null;
            for (RoleEnum roleEnum : rolesAllowed) {
                if (requestContext.getSecurityContext().isUserInRole(roleEnum.name())) {
                    return;
                }
            }
        }
//        throw new AccessDeniedException(LocalizationMessages.USER_NOT_AUTHORIZED());//ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
        throw new AccessDeniedException("USER_NOT_AUTHORIZED()");//ForbiddenException(LocalizationMessages.USER_NOT_AUTHORIZED());
    }

}
