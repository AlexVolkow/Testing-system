package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by disqustingman on 19.06.16.
 */
public class AppProperties {
    private static AppProperties ourInstance = new AppProperties();

    public static AppProperties instance() {
        return ourInstance;
    }

    private Map<String,String> dbProp;
    private Map<String,String> serverProp;

    private AppProperties() {
        dbProp = new HashMap<>();
        serverProp = new HashMap<>();
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("cfg/config.properties")){
            properties.load(input);

            dbProp.put("host",properties.getProperty("dbhost"));
            dbProp.put("port",properties.getProperty("dbport"));
            dbProp.put("user",properties.getProperty("dbuser"));
            dbProp.put("password",properties.getProperty("dbpassword"));
            dbProp.put("database",properties.getProperty("database"));

            serverProp.put("port",properties.getProperty("sport"));
            serverProp.put("adminpassword",properties.getProperty("adminpass"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Map<String,String> getDbProp(){
        return dbProp;
    }
    public Map<String,String> getServerProp(){
        return serverProp;
    }
}
