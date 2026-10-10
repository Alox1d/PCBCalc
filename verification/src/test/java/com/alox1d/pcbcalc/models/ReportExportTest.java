package com.alox1d.pcbcalc.models;
import com.alox1d.pcbcalc.utils.ApachePOIHelper;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;
import org.junit.BeforeClass;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class ReportExportTest {
    @BeforeClass public static void configureXmlProvider() {
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }

    @Test public void everyTemplateReplacesAllBookmarksAndSurvivesReload() throws Exception {
        String[] templates = {"mechanical_impacts_vstable.docx", "mechanical_impacts_vdurable.docx", "dependability_failure_rate.docx"};
        for (String name : templates) {
            File source = new File(System.getProperty("pcbcalc.templates"), name);
            List<String> keys = new ArrayList<>();
            try (XWPFDocument original = new XWPFDocument(new FileInputStream(source))) {
                for (XWPFParagraph p : original.getParagraphs()) p.getCTP().getBookmarkStartList().forEach(b -> keys.add(b.getName()));
            }
            assertFalse(keys.isEmpty());
            ApachePOIHelper helper = new ApachePOIHelper();
            helper.openDocument(new FileInputStream(source));
            for (String key : keys) helper.findAndReplace(key, "value<&>=" + key);
            File result = new File(helper.saveCurrentDocument("", name), name);
            try (XWPFDocument reloaded = new XWPFDocument(new FileInputStream(result))) {
                StringBuilder text = new StringBuilder();
                for (XWPFParagraph p : reloaded.getParagraphs()) text.append(p.getText());
                for (String key : keys) assertTrue(name + ": " + key, text.toString().contains("value<&>=" + key));
                assertFalse(text.toString().contains(": —"));
            }
        }
    }
}
