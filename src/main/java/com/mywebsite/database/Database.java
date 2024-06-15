package main.java.com.mywebsite.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.*;

import com.github.javafaker.Faker;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.java.com.mywebsite.Data.FileSrcData;
import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.database.Interfaces.DatabaseInterface;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor
@Getter
@Setter
public abstract class Database extends Dao_DBConnect implements DatabaseInterface
{
    static Logger logger = LogManager.getLogger(Database.class.getName());

    protected boolean permitCreateDB = true;
    /**
     * enum for database use
     * @author cgl
     *
     */

    public static enum DatabaseType
    {
        sqlite("sqlite"),
        file("file");
        String value = null;
        DatabaseType(String value)
        {
        	this.value = value;
        }
        public static DatabaseType getValue()
        {
        	return sqlite;
        }
    };

    protected Connection connection;

    protected String serverIp;

    protected String path;

    boolean headerInUppercaseCharacter = true;

    static HashMap<String, String> mapFromFile;

    /**
     * get instance
     * @return
     */
    public static Database getInstance()
    {
        return getInstance(DatabaseType.getValue());
    }

    /**
     * get instance
     * @param source
     * @return
     */
    public static Database getInstance(DatabaseType source)
    {
        Database data = null;
        switch(source)
        {
            case file:
                data = new DatabaseFile();
                break;
            case sqlite:
                data = new DatabaseSQLite();
                break;
            default:
            	logger.info("Not supported yet: source '"+source.value+"'. Using '"+DatabaseType.sqlite+"'.");
                data = new DatabaseSQLite();
                break;
        }
        return data;
    }

    public abstract void connect();

    public abstract boolean createDatabaseIfNotExists();

    public abstract ArrayList<FileSrcData> getData();

    public abstract ArrayList<FileSrcData> getAllData();

    public abstract void insertData(List<FileSrcData> data);

    public abstract void insertData(List<FileSrcData> data, boolean test);

    public static int getRandom()
    {
    	return new Random().nextInt(10000000) + 1000000;
    }

    public abstract int getCountOfData();

	/**
	 * get properties
	 * @param filename
	 * @return Map<String, String>
	 */
    private Map<String, String> getProperties(String filename){
        if(StringUtils.isNotBlank(filename) && new File(filename).exists()){
            try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
                String line = "";
                HashMap<String, String> mapFromFile = new HashMap<String, String>();
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("=");
                    String name = parts[0].trim();
                    String value = parts[1].trim();
                    if(StringUtils.isNotBlank(name)
                            && StringUtils.isNotBlank(value)) {
                        mapFromFile.put(name, value);
                    }
                }
                this.mapFromFile = mapFromFile;
                return mapFromFile;
            } catch (Exception e) {
                logger.error(e);
            }
        } else {
            logger.info("File '"+filename+"' does not exist");
        }
        return null;
    }
    /**
     * load property
     * @param keyname
     * @return
     */
    public String getProperty(String keyname){
        return StringUtils.isNotBlank(keyname)
                ? mapFromFile.get(keyname)
                : null;
    }
}
