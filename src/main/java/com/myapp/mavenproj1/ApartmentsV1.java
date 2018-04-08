/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.mavenproj1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Мен
 */
public class ApartmentsV1 {
    private static final Logger LOG = Logger.getLogger(ApartmentsV1.class.getName());

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        String logFile = System.getProperty("java.util.logging.config.file");   // java -Djava.util.logging.config.file=<external-logging.properties> -jar MyProjectJar.jar
        if(logFile == null){
                try {
                    LogManager.getLogManager().readConfiguration(
                            ApartmentsV1.class.getClassLoader().getResourceAsStream("logging.properties"));
                } catch (FileNotFoundException | NullPointerException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
        }                
        
        DScraping bot = new DScraping();
        bot.doJob();
    }
    
}
