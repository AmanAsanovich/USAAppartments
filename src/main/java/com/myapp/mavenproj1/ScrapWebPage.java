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

import java.util.logging.Level;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.common.usermodel.HyperlinkType;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author Aman Mambetov "asanbay"@mail.ru
 */
public class ScrapWebPage {
    
    public void Scrap(Document dDoc, String sTheCity) {
        Elements eAppartments;
        String sAppartmentTitle, sStreetAddress, sWebLink;
        
        Cell cell;
        Row row;
        CreationHelper createHelper = DScraping.book.getCreationHelper();
        CellStyle hlinkstyle = DScraping.book.createCellStyle();
        Font hlinkfont = DScraping.book.createFont();
        hlinkfont.setColor(IndexedColors.BLUE.getIndex());
        hlinkstyle.setFont(hlinkfont);

        eAppartments = dDoc.select("._1GY9b");
        for (Element el : eAppartments) {
            
            sAppartmentTitle = el.select("._3Ycqy").text();
            sStreetAddress = el.select("._2o6Dl").text();
            sWebLink = el.select("._3Ycqy a").attr("href");
            sWebLink = DScraping.sTargetSiteUrl.concat(sWebLink);
            
            if (sStreetAddress.matches(".+,\\s"+sTheCity+".+")) {
                row = DScraping.theSheet.createRow(DScraping.iRowNumber++);
                cell = row.createCell(0, CellType.STRING);
                cell.setCellValue(sAppartmentTitle);
                cell = row.createCell(1, CellType.STRING);
                cell.setCellValue(sStreetAddress);

                //URL Link
                cell = row.createCell(2, CellType.STRING);
                cell.setCellValue(sWebLink);
                Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
                link.setAddress(sWebLink);
                cell.setHyperlink(link);
                cell.setCellStyle(hlinkstyle);
                
                System.out.println(sAppartmentTitle);
                System.out.println(sStreetAddress);
                System.out.println(sWebLink);
                System.out.println("");

//                DScraping.iRowNumber++;
            }
        }
    }
    
}
