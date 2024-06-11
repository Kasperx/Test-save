
package main.java.com.mywebsite.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map.Entry;

import main.java.com.mywebsite.Data.FileSrcData;
import org.apache.logging.log4j.LogManager;

public class DatabaseFile extends Database implements Serializable
{  
    int id;
    String name;
    String lastname;
    String pw;
    boolean admin;
    public DatabaseFile()
    {
        logger = LogManager.getLogger(DatabaseFile.class.getName());
        path = System.getProperty("user.dir")+"/test";
        File dbFile = new File(path);
        try
        {
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
            connect();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public DatabaseFile(int id, String name, String lastname, String pw, boolean admin)
    {
        path = System.getProperty("user.dir")+"/test";
        File dbFile = new File(path);
        try{
            if(!dbFile.exists())
            {
                dbFile.createNewFile();
            }
        } catch(Exception e) {
            logger.error(e);
        }
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.pw = pw;
        this.admin = admin;
    }
	public ArrayList<FileSrcData> getData(){
        ArrayList<FileSrcData> data = new ArrayList<>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path))){
            DatabaseFile db;
            Object obj;
            FileSrcData fileSrcData = new FileSrcData();
            while((obj = inFile.readObject()) != null){
                db = new DatabaseFile();
                db = (DatabaseFile)obj;
                data.add(fileSrcData);
            }
            return data;
        } catch(ClassNotFoundException | IOException e) {
            logger.error(e);
        }
        return data;
    }
    public int getId(String name)
    {
        return -1;
    }
    public ArrayList<FileSrcData> getAllData(){
        ArrayList<FileSrcData> data = new ArrayList<>();
        try(ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path))){
            return data;
        } catch(Exception e) {
            logger.error(e);
        }
        return data;
    }

    public void insertData(){
        /*
        int id=0;
        HashMap <String[], Integer> result = getNewData();
        ///////////////////////////////////////////////////////////
        ArrayList <DatabaseFile> data = new ArrayList<DatabaseFile>();
        data.add(new DatabaseFile());
        for(Entry <String[], Integer> entry: result.entrySet()){
            data.add(new DatabaseFile(
                  id++,
                  entry.getKey()[0],
                  entry.getKey()[1],
                  String.valueOf(entry.getValue()),
                  false
                  ));
        }
        try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path))){
            for(DatabaseFile temp: data){
                write.writeObject((Object)temp);
            }
        } catch(IOException e) {
            logger.error(e);
        }
        */
    }

    @Override
    public void insertData(List<FileSrcData> data) {

    }

    @Override
    public void insertData(List<FileSrcData> data, boolean test) {

    }

    @Override
    public int getCountOfData() {
        return 0;
    }

    @Override
    public boolean insertData(FileSrcData data) {
        return false;
    }

    @Override
	public void connect(){}

    @Override
    public boolean createDatabaseIfNotExists() {
        return false;
    }
}

