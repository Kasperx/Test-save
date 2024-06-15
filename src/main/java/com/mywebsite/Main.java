package main.java.com.mywebsite;

import main.java.com.mywebsite.Data.FileSrcData;
import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.database.Database;
import main.java.com.mywebsite.database.DatabaseSQLite;
import main.java.com.mywebsite.service.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class Main extends Dao_DBConnect{

    static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args){
        logger = LogManager.getLogger(Main.class.getName());
        logger.info("Program start ...");
        Database database = Database.getInstance();
        if(database.isDBEmpty()) {
            List<FileSrcData> fileSrcData = Tools.getDataFromFile(System.getProperty("user.dir") + File.separator + "postcodes.csv");
            database.createDatabaseIfNotExists();
            database.insertData(fileSrcData, true);
        }

        for(String arg: args){
            if(arg.equalsIgnoreCase("-help")
                    || arg.equalsIgnoreCase("-h")
                    || arg.equalsIgnoreCase("-?")
                    || arg.equalsIgnoreCase("?")){
                    showHelp();
            }
            if(arg.equalsIgnoreCase("-print")
                    || arg.equalsIgnoreCase("print-info")){
                database.printInfo();
            }
            if(arg.equalsIgnoreCase("-print-data")){
                database.printData();
            }
            if(arg.equalsIgnoreCase("-print-all")){
                database.printAllData();
            }
        }
        logger.info("Bye");
        System.exit(0);
    }
    private static void showHelp (){
        logger.info("");
        logger.info("### This program is a test program for filtering location data and deciding some insurance parameter with those data ###");
        logger.info(" It will read a src file, store the information to a database and manage those data for parameter decisions.");
        logger.info("Syntax: [-help | -h | -? | ?] [-print | print-data | print-all]");
        logger.info("\t Options");
        logger.info("\t\t -h/-help/-?/?         show this help and exit");
        logger.info("\t\t -print | print-info   print info of stored data");
        logger.info("\t\t -print-data           print data from database (with limit "+LIMIT_PRINT_DATA+")");
        logger.info("\t\t -print-all            print data from database");
        logger.info("\nBye");
        System.exit(0);
    }
}
