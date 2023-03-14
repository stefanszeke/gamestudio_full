package sk.tuke.gamestudio_backend.databaseJDBC.properties;

import java.io.FileInputStream;
import java.util.Properties;


// used for jdbc database connection
public class AppProperties {

    static Properties properties = new Properties();
    static {
              try {
                properties.load(new FileInputStream("src/main/resources/database.properties"));
              } catch (Exception e) {
                e.printStackTrace();
              }
         }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
