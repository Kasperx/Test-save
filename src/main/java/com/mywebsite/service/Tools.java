package main.java.com.mywebsite.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.NoArgsConstructor;
import main.java.com.mywebsite.Data.FileSrcData;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Tools {

    static Logger logger = LogManager.getLogger(Tools.class.getName());

    /*
    public static void writeToCsv() {
        String csv = "data.csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));
            String[] record = "2,Virat,Kohli,India,30".split(",");
            writer.writeNext(record);
            writer.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }
     */
    public static void readCSVFile(String path) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            logger.error(e);
        }
        List<String[]> allRows = new ArrayList<>();
        try {
            allRows = reader.readAll();
        } catch (IOException | CsvException e) {
            logger.error(e);
        }
        /*
        for (String[] row : allRows) {
            logger.info(Arrays.toString(row));
        }
        */
    }

    public static List<String[]> readLineByLine(String path) {
        Path filePath = Paths.get(path);
        List<String[]> list = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(filePath)) {
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    list.add(line);
                }
            } catch (Exception e){
                logger.error(e);;
            }
        } catch (Exception e){
            logger.error(e);;
        }
        logger.info("Read "+list.size()+" lines from file '"+path+"'.");
        return list;
    }

    public static List<FileSrcData> getObjectFromFile(String path) {
        logger.info("Reading info, converting data to object ...");
        if(StringUtils.isNotBlank(path)) {
            List<FileSrcData> fileSrcDataList = new ArrayList<>();
            FileSrcData fileSrcData = new FileSrcData();
            List<String[]> list = readLineByLine(path);
            for (String[] line : list) {
                if(list.indexOf(line) == 0){
                    continue;
                }
                int count = 0;
                fileSrcData.setISO_3166_1_ALPHA_2(line[count++]);
                fileSrcData.setISO_3166_1_ALPHA_2_REGION_CODE(line[count++]);
                fileSrcData.setREGION1(line[count++]);
                fileSrcData.setREGION2(line[count++]);
                fileSrcData.setREGION3(line[count++]);
                fileSrcData.setREGION4(line[count++]);
                fileSrcData.setPOSTLEITZAHL(
                        Integer.parseInt(line[count++]));
                fileSrcData.setORT(line[count++]);
                fileSrcData.setAREA1(line[count++]);
                fileSrcData.setAREA2(line[count++]);
                fileSrcData.setLONGITUDE(
                        Double.parseDouble(line[count++]));
                fileSrcData.setLATITUDE(
                        Double.parseDouble(line[count++]));
                //fileSrcData.setZEITZONE();
                //fileSrcData.setCALC_LOCALE();
                fileSrcData.setSOMMERZEIT(
                        stringToBoolean(line[count++])
                );
                fileSrcData.setACTIVE(line[count++].charAt(0));
                fileSrcDataList.add(fileSrcData);
            }
            logger.info("Got "+fileSrcDataList.size()+" objects from file.");
            return fileSrcDataList;
        } else {
            logger.error("Got no objects from file.");
            return null;
        }
    }

    public static boolean stringToBoolean (String bool){
        return StringUtils.isNotBlank(bool)
                ? bool.equalsIgnoreCase("true")
                : null;
    }
    public static boolean intToBoolean (int bool){
        return bool == 1;
    }
}
