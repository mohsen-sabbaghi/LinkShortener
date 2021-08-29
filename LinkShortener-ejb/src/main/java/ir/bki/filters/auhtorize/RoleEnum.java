package ir.bki.filters.auhtorize;

/**
 * @author Mahdi Sharifi
 * @version 1.0.1
 * https://www.linkedin.com/in/mahdisharifi/
 * @since 19/11/2019
 */
public enum RoleEnum { //TODO convert to entity
    admin, user, guest,
    ;
    public final static String Admin = "admin";
    public final static String User = "user";
    public final static String Guest = "guest";
}
