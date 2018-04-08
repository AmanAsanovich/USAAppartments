/*
 * Copyright 2018 Aman Mambetov "asanbay"@mail.ru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.myapp.mavenproj1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author Aman Mambetov "asanbay"@mail.ru
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
