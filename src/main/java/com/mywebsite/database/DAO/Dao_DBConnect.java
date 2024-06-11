package main.java.com.mywebsite.database.DAO;

import java.io.File;
import java.sql.Connection;

import org.apache.logging.log4j.Logger;

public class Dao_DBConnect
{
    protected Connection connection;
    protected String serverIp;
    protected String path;
    protected static Logger logger;
    protected final static String tableName = "Bewegungsdaten";
    protected static final int COUNT_DIGITS_AFTER_COMMA = 5;
    /*
    static {
        File file;
        if((file = new File(System.getProperty("user.dir") + "src/main/java/com/mywebsite/resources/log4j2.properties")).exists()) {
            PropertyConfigurator.configure(file.getAbsolutePath());
        }
    }
     */
}
