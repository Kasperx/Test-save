package main.java.com.mywebsite.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.java.com.mywebsite.Data.FileSrcData;
import main.java.com.mywebsite.database.DAO.Dao_DBConnect;
import main.java.com.mywebsite.service.Tools;
import org.apache.logging.log4j.LogManager;

public class DatabaseSQLite extends Database
{  
    String path = System.getProperty("user.dir")+"/test.db";

    Connection connection = null;

    boolean test = true;

    public DatabaseSQLite(){
        logger = LogManager.getLogger(DatabaseSQLite.class.getName());
        File dbFile = new File(path);
        try{
            if(!dbFile.exists()){
                dbFile.createNewFile();
            }
            connect(true);
        } catch(IOException e) {
            logger.error(e);
        }
    }
    /**
     * connect to database
     */
    public void connect()
    {
    	connect(false);
    }
    /**
     * connect to database
     * @param showInfo
     */
    public void connect(boolean showInfo)
    {
        try{
        	if(connection == null || connection.isClosed()){
        		Class.forName("org.sqlite.JDBC");
        		connection = DriverManager.getConnection("jdbc:sqlite:"+path);
        		if(showInfo){
        			logger.info("Connected to database '"+path+"'.");
        		}
        	}
        } catch(Exception e) {
            logger.error(e);
        }
    }
    /**
     * get data without sensible information
     */
    public ArrayList<FileSrcData> getData(){
        /*
        String sql = ""
        		+ "SELECT"
        		+ " position,"
        		+ " name,"
                + " surname,"
        		+ " action,"
        		+ " action_name"
        		+ " FROM"
        		+ " person;";
        return getDataFromDBWithoutHeader(sql, false);
        */
        return new ArrayList<>();
    }

    /**
     * get data with all information
     */
    public ArrayList<FileSrcData> getAllData(){
        /*
        String sql = "SELECT"
                + " p.id,"
                + " p.name,"
                + " p.lastname,"
                + " login.p_password as password,"
                + " login.p_admin as admin"
//                + " FROM person p, login l"
                + " FROM person p"
                + " inner join login on p.id = login.p_id;";
        ArrayList<Person> data = getDataFromDBWithoutHeader(sql, true);
        return data;
        */
        return new ArrayList<>();
    }

    /**
     * if permitted: create Database If Not Exists
     */
    public boolean createDatabaseIfNotExists()
    {
        if(permitCreateDB) {
            //executeSet("remove from table test");
            //executeSet("drop database if exists test");
            //////////////////////////////
            executeSet("create table if not exists " + Dao_DBConnect.tableName + " ("
                    + "id integer primary key autoincrement,"
                    + "ISO_3166_1_ALPHA_2 text,"
                    + "ISO_3166_1_ALPHA_2_REGION_CODE text,"
                    + "REGION1 text,"
                    + "REGION2 text,"
                    + "REGION3 text,"
                    + "REGION4 text,"
                    + "POSTLEITZAHL integer,"
                    + "ORT text,"
                    + "AREA1 text,"
                    + "AREA2 text,"
                    + "LATITUDE real,"
                    + "LONGITUDE real,"
                    + "ZEITZONE text,"
                    + "CALC_LOCALE integer,"
                    + "SOMMERZEIT integer,"
                    + "ACTIVE text"
                    + ");");
            /*
             *  table login with boolean.. but seems problem with jdbc -> sqlite
             *  sometimes boolean = int, sometimes not :(
             *  so boolean = int and works. remember for other dbs
             */
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void insertData(List<FileSrcData> data) {
        logger.info("Storing object to db ...");
        for(FileSrcData fileSrcData: data) {
            insertData(fileSrcData);
        }
    }

    @Override
    public void insertData(List<FileSrcData> data, boolean test) {
        logger.info("Storing object to db ...");
        // to see progress ... (remove after test)
        if(test) {
            for (int i = 0; i < data.size(); i++) {
                if (i % 1000 == 0) {
                    logger.info("Storing object #"+i+" to db -> " + data.get(i).toString());
                }
                insertData(data.get(i));
            }
        }
    }

    @Override
    public int getCountOfData() {
        ResultSet resultSet = executeGet("select count(*) from "+Dao_DBConnect.tableName);
        try {
            return resultSet.getInt(1);
        } catch (Exception e){
            logger.error(e);
            return 0;
        }
    }

    @Override
    public boolean insertData(FileSrcData fileSrcData)
    {
        try {
            String sql = ""
                    +" insert into " + Dao_DBConnect.tableName + " ("
                    + "ISO_3166_1_ALPHA_2,"
                    + "ISO_3166_1_ALPHA_2_REGION_CODE,"
                    + "REGION1,"
                    + "REGION2,"
                    + "REGION3,"
                    + "REGION4,"
                    + "POSTLEITZAHL,"
                    + "ORT,"
                    + "AREA1,"
                    + "AREA2,"
                    + "LATITUDE,"
                    + "LONGITUDE,"
                    + "ZEITZONE,"
                    //+ "CALC_LOCALE,"
                    //+ "SOMMERZEIT,"
                    + "ACTIVE"
                    + ") values ("
                    + "";
            connect();
            int count = 1;
            PreparedStatement stmt = connection.prepareStatement(sql+
                    "?,?,?," +
                    "?,?,?," +
                    "?,?,?," +
                    "?,?,?," +
                    "?,?" +
                    ""+");");
            stmt.setString(count++, fileSrcData.getISO_3166_1_ALPHA_2());
            stmt.setString(count++, fileSrcData.getISO_3166_1_ALPHA_2_REGION_CODE());
            stmt.setString(count++, fileSrcData.getREGION1());
            stmt.setString(count++, fileSrcData.getREGION2());
            stmt.setString(count++, fileSrcData.getREGION3());
            stmt.setString(count++, fileSrcData.getREGION4());
            stmt.setInt(count++, fileSrcData.getPOSTLEITZAHL());
            stmt.setString(count++, fileSrcData.getORT());
            stmt.setString(count++, fileSrcData.getAREA1());
            stmt.setString(count++, fileSrcData.getAREA2());
            stmt.setString(count++, String.format("%."+COUNT_DIGITS_AFTER_COMMA+"f", fileSrcData.getLATITUDE()));
            stmt.setString(count++, String.format("%."+COUNT_DIGITS_AFTER_COMMA+"f", fileSrcData.getLONGITUDE()));
            //stmt.setInt(count++, fileSrcData.getCALC_LOCALE());
            //stmt.setInt(count++, fileSrcData.getSOMMERZEIT());
            stmt.setInt(count++, fileSrcData.getACTIVE());
            logger.debug(stmt.toString());
            stmt.execute();
            close(null);
        } catch (Exception e) {
            logger.error("insert into " + Dao_DBConnect.tableName, e);
        }
        return false;
    }

    /**
     * execute sql cmd
     * @param sql
     * @return
     */
    ResultSet executeGet(String sql)
    {
        try{
            logger.debug(sql);
            connect();
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        } catch(SQLException e) {
            logger.error(e);
            return null;
        }
    }

    /**
     * get meta data
     * @param sql
     * @return ResultSetMetaData
     */
    ResultSetMetaData getMetaData(String sql){
    	try {
    		logger.info(sql);
    		PreparedStatement stmt = connection.prepareStatement(sql);
    		return stmt.getMetaData();
    	} catch(SQLException e) {
    		logger.error(e);
    		return null;
    	}
    }

    /**
     * execute sql cms
     * @param sql
     */
    void executeSet(String sql){
        try {
            logger.debug(sql);
            connect();
            connection.prepareStatement(sql).executeUpdate();
            close(null);
        } catch(SQLException e) {
        	logger.error(e);
        }
    }

    /**
     * 
     * @param connection
     * @param sql
     * @param values
     * @return
     * @throws SQLException
     */
    public static PreparedStatement prepareStatement(Connection connection, String sql, Object... values)
            throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
        logger.debug(sql + " " + Arrays.asList(values));
        return preparedStatement;
    }

    ArrayList<FileSrcData> getDataFromDB(String sql, boolean admin)
    {
        ResultSet resultSet = executeGet(sql);
    	ArrayList<FileSrcData> data = new ArrayList<FileSrcData>();
        try
        {
            FileSrcData fileSrcData;
            while(resultSet != null && resultSet.next())
            {
                fileSrcData = new FileSrcData();
                fileSrcData.setISO_3166_1_ALPHA_2(
                        resultSet.getString("ISO_3166_1_ALPHA_2")
                );
                fileSrcData.setISO_3166_1_ALPHA_2_REGION_CODE(
                        resultSet.getString("ISO_3166_1_ALPHA_2_REGION_CODE")
                );
                fileSrcData.setREGION1(
                        resultSet.getString("REGION1")
                );
                fileSrcData.setREGION2(
                        resultSet.getString("REGION2")
                );
                fileSrcData.setREGION3(
                        resultSet.getString("REGION3")
                );
                fileSrcData.setREGION4(
                        resultSet.getString("REGION4")
                );
                fileSrcData.setPOSTLEITZAHL(
                        resultSet.getInt("POSTLEITZAHL")
                );
                fileSrcData.setORT(
                        resultSet.getString("ORT")
                );
                fileSrcData.setAREA1(
                        resultSet.getString("AREA1")
                );
                fileSrcData.setAREA2(
                        resultSet.getString("AREA2")
                );
                fileSrcData.setLATITUDE(
                        resultSet.getDouble("LATITUDE")
                );
                fileSrcData.setLONGITUDE(
                        resultSet.getDouble("LONGITUDE")
                );
                fileSrcData.setZEITZONE(
                        resultSet.getString("ZEITZONE")
                );
                fileSrcData.setCALC_LOCALE(
                        resultSet.getInt("CALC_LOCALE")
                );
                fileSrcData.setSOMMERZEIT(
                        Tools.intToBoolean(resultSet.getInt("SOMMERZEIT"))
                );
                fileSrcData.setACTIVE(
                        resultSet.getString("ACTIVE").charAt(0)
                );
                data.add(fileSrcData);
            }
            close(resultSet);
        } catch(SQLException e) {
        	logger.error(e);
        }
        return data;
    }

    /**
     * close db connection
     * @param resultSet
     */
    private void close(ResultSet resultSet)
    {
    	try
    	{
    		if(resultSet != null && !resultSet.isClosed()){
    			resultSet.close();
    		} if(connection != null){
    			connection.close();
    		}
		} catch (SQLException e) {
    		logger.error(e);
		}
	}
}
