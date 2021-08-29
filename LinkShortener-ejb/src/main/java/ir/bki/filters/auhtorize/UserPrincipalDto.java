package ir.bki.filters.auhtorize;

/**
 * Created by me-sharifi on 2/17/2018.
 */
//https://simplapi.wordpress.com/2015/09/19/jersey-jax-rs-securitycontext-in-action/

import ir.bki.util.serializer.GsonExcludeField;
import ir.bki.util.serializer.GsonModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.security.Principal;
import java.util.List;

/**
 * User bean.
 *
 * @author Deisss (MIT License)
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPrincipalDto extends GsonModel implements Principal {

    private String user;

    @GsonExcludeField
    private List<String> roles;//TODO Change List to Set

    public UserPrincipalDto(String user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return this.user;
    }
}
