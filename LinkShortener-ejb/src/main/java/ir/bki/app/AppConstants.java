package ir.bki.app;

import ir.bki.entities.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AppConstants {
    public static final String BUILD_TIME = "2020/2/13 1399/11/25";
    public static final String APP_VERSION = "2020.2.0";

    public static Map<String, User> Map_User = new ConcurrentHashMap<>();
}
