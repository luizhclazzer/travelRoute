package com.dijkstra.travelRoute.utils;

import com.dijkstra.travelRoute.service.RouteService;
import com.dijkstra.travelRoute.utils.Exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import java.io.*;
import java.util.Scanner;

public class UtilFile {

    public static final String CSV_SEPARATOR = ",";

    public UtilFile() {
    }

    @Autowired
    ApplicationArguments applicationArguments;

    public static void writeLineToCSVFile(String line) throws FileException {

        String fileName = RouteService.getCsvFile();

        try ( //@formatter:off
              FileWriter fstream = new FileWriter(fileName, true);
              BufferedWriter out = new BufferedWriter(fstream);
        ) { //@formatter:on
            out.write(line);
            out.newLine();
        }

        catch (final Exception e) {
            throw new FileException("Error at read file: " + fileName);
        }

    }

}
