package ir.bki.app;

import ir.bki.entities.Role;
import ir.bki.entities.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AppConstants {
    public static final String BUILD_TIME = "2021/08/28 - 1400/06/06";
    public static final String APP_VERSION = "2021.2.0";
    public static final String DATASOURCE_SCHEMA = "LINKSHORTENER.";


    public static Map<String, User> Map_User = new ConcurrentHashMap<>();
    public static Map<String, Role> Map_Role = new ConcurrentHashMap<>();

}
