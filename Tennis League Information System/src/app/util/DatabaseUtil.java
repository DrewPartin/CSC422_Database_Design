package app.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseUtil {
    private static String url;
    private static String user;
    private static String password;
    
    static {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load db.properties. Create it from db.properties.example.", e);
        }
    }
    
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}
