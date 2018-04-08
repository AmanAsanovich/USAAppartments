/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.mavenproj1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Мен
 */
public class DScraping {

    protected static String sTargetSiteUrl;
    protected static SXSSFWorkbook book;
    protected static String sTheState;
    protected static int iRowNumber;
    protected static SXSSFSheet theSheet;
    
    private final String sXlsOutFileName;
    private FileOutputStream outputStream;
    
    private final Elements eStates;
    
    
    
    
    
    public DScraping() {
        this.sTargetSiteUrl = "https://www.apartmentguide.com";

        this.book = new SXSSFWorkbook(100);
        
        long lRdm = Math.round(((Math.random() * 2) + 1)*1000);
        Long.toString(lRdm);
        this.sXlsOutFileName = "USAppatrments-"+Long.toString(lRdm)+".xlsx";
        
        Document dDoc = new Document("");
        try {
            dDoc = Jsoup.connect(sTargetSiteUrl.concat("/apartments")).get();  // Get main page
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        this.eStates = dDoc.select(".browse_links a");       // List of States

}

    
    public void doJob() throws IOException {
        
        String sStateLink;
        Document dCitiesDoc;
                
        int i = 0;
        // For every state get cities
        for (Element elem : eStates) {
            
            // For debuging - Small states 12-13, 20, 30, 40
            i++;
            if (i < 13) {
                continue;
            }
            if (i > 13) {
                break;
            }
            // For debuging - Small states 12-13, 20, 30, 40

            sStateLink = elem.attr("href");
            this.sTheState = sStateLink.replaceFirst("/[\\w-]+/([\\w-]+)/", "$1");  //  /appartments/Alabama/ -> Alabama
            
            this.theSheet = book.createSheet(this.sTheState);
            this.iRowNumber = 0;
            LOG.log(Level.INFO, sTheState);
            
//            System.out.println(sTargetSiteUrl.concat(sStateLink) );
            
            dCitiesDoc = Jsoup.connect(sTargetSiteUrl.concat(sStateLink)).get();  // Open this state page and get cities
            Elements eCities = dCitiesDoc.select(".browse_links a");    // All cities of the state are here in the page, ther is no pagination

            for (Element eCity : eCities) {

                CityWalker wayfarer = new CityWalker();
                wayfarer.execute(eCity.attr("href"));

                DScraping.myDelay(1,1);
            }

        }

        outputStream = new FileOutputStream(new File(sXlsOutFileName));
        book.write(outputStream);
        book.dispose();
    }
    
    public static void myDelay (int min, int max) {
        long lRnd;
        max -= min;
        lRnd = Math.round(((Math.random() * max) + min)*1000);
/*
            System.out.println(lRnd);
            System.out.println("");
*/
        try {
            TimeUnit.MILLISECONDS.sleep(lRnd);
        }
        catch(InterruptedException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    
    protected static final Logger LOG = Logger.getLogger(DScraping.class.getName());
    
}

