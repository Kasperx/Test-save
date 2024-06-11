package main.java.com.mywebsite;

import main.java.com.mywebsite.Data.FileSrcData;
import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.database.Database;
import main.java.com.mywebsite.service.Tools;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.List;

public class Main extends Dao_DBConnect
{
    public static void main(String args[]){
        logger = LogManager.getLogger(Main.class.getName());
        logger.info("Program start ...");
        Database database = Database.getInstance();
        List<FileSrcData> fileSrcData = Tools.getObjectFromFile(System.getProperty("user.dir") + File.separator + "postcodes.csv");
        database.createDatabaseIfNotExists();
        database.insertData(fileSrcData, true);
        logger.info("Stored "+database.getCountOfData()+" data from file.");
    }
    private static void showHelp ()
    {
            logger.info("");
            logger.info("### This program is a test program for filtering location data and deciding some insurance parameter with those data ###");
            logger.info(" It will read a src file, store the information to a database and manage those data for parameter decisions.");
            logger.info("Syntax: [-help | -h | -? | ?]");
            logger.info("\t Options");
            logger.info("\t\t -h/-help/-?/?                  show this help and exit");
            logger.info("\nBye");
            System.exit(0);
    }
}
