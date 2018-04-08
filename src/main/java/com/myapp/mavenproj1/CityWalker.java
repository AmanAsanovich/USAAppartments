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

import java.io.IOException;
import java.util.logging.Level;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Aman Mambetov "asanbay"@mail.ru
 */
public class CityWalker {
    
    /**
     * 
     *
     * 
     * @param sSubLinkToCity
     * @throws java.io.IOException
     */
    public void execute(String sSubLinkToCity) throws IOException {

        String sCityLinkFirst, sCityLink;
        Document dDoc;

        String sCity;

        sCityLinkFirst = DScraping.sTargetSiteUrl.concat(sSubLinkToCity);
        dDoc = Jsoup.connect(sCityLinkFirst).get();                              // Get city page
        sCity = dDoc.select(".SiOL_ span[data-test-id=breadcrumb]").text();

        System.out.println("\nCity: "+sCity+"\n===");

        Row row = DScraping.theSheet.createRow(DScraping.iRowNumber++);     // Title (city name) in the xls file
        Cell cell = row.createCell(0, CellType.STRING);
        cell.setCellValue(sCity);
        
        ScrapWebPage pagesScraper = new ScrapWebPage();
        pagesScraper.Scrap(dDoc, sCity);                    // Scrap the current page
        
        Elements ePagination = dDoc.select(".tYnqB li a");  // How many pages for the city?
        if (!ePagination.isEmpty()) {
        // There are more pages
            int iPaginationCount;
            String sLastIndex = "";
            if (ePagination.size() == 10) {         // More than or eq 10
                try {
                    sLastIndex = ePagination.get(8).text();     // Get number of pages. 10 links, statrs with 2, last link is "Next"
                }
                catch (IndexOutOfBoundsException e) {
                    DScraping.LOG.log(Level.SEVERE, null, e);
                }
                iPaginationCount = Integer.parseInt(sLastIndex);
            }
            else {
                iPaginationCount = ePagination.size();      // Less than or eq 10
            }

            for (int i = 2; i <= iPaginationCount; i++) {
                sCityLink = sCityLinkFirst.concat("?page=");        
                sCityLink = sCityLink.concat(String.valueOf(i));
                System.out.println("Page "+ Integer.toString(i));

                dDoc = Jsoup.connect(sCityLink).get();
                pagesScraper.Scrap(dDoc, sCity);            // Scrap another page

                DScraping.myDelay(1,2);                      // Be polite plz
            }
        }
        
        row = DScraping.theSheet.createRow(DScraping.iRowNumber++);     // Add empty row
        
    }

}
