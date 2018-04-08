/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myapp.mavenproj1;

import java.util.logging.Level;
import org.apache.poi.hssf.util.HSSFColor;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import static org.apache.poi.ss.usermodel.IndexedColors.BLUE;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



/**
 *
 * @author Мен
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
//        hlinkfont.setUnderline(XSSFFont.U_SINGLE);
        hlinkfont.setColor(IndexedColors.BLUE.getIndex());
        hlinkstyle.setFont(hlinkfont);

//        NumberFormat nf = NumberFormat.getNumberInstance();
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
