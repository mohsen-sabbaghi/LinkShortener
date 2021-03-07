package ir.bki.app;

import ir.bki.entities.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AppConstants {
    public static final String BUILD_TIME = "2021/03/06 1399/12/15";
    public static final String APP_VERSION = "2021.1.0";
    public static final String DATASOURCE_SCHEMA = "LINKSHORTENER.";


    public static Map<String, User> Map_User = new ConcurrentHashMap<>();
}
