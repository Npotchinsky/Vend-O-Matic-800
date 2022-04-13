package com.techelevator;

import java.io.*;

public class VendingLog {
    static File log = new File("capstone/Log.txt");

    public static void Log(String s) {
        try (FileWriter logger = new FileWriter(log, true)) {
            logger.append(s +"\n");
        }
        catch (IOException e) {
            System.err.println("File not found");
        }
    }
}