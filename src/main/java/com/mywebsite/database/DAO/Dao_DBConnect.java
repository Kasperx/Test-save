package main.java.com.mywebsite.database.DAO;

import java.io.File;
import java.sql.Connection;

import org.apache.logging.log4j.Logger;

public class Dao_DBConnect
{
    protected Connection connection;

    protected String serverIp;

    protected String path;

    protected final static String tableName = "Bewegungsdaten";

    protected static final int COUNT_DIGITS_AFTER_COMMA = 5;

    protected static int LIMIT_PRINT_DATA = 10;
}
