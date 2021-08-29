package ir.bki.filters.auhtorize;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class SecurityContextCustom implements SecurityContext {
    private final UserPrincipalDto user;
    private final String scheme;

    public SecurityContextCustom(UserPrincipalDto user, String scheme) {
        this.user = user;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String s) {
        if (user.getRoles() != null)
            for (String role : user.getRoles()) {
                if (role.equalsIgnoreCase(s))
                    return true;
            }
        return false;
    }

    @Override
    public boolean isSecure() {
        return "https".equals(this.scheme);
    }

    @Override
    public String getAuthenticationScheme() {
        return scheme;
    }
}
