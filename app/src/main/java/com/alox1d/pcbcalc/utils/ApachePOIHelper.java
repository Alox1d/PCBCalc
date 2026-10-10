package com.alox1d.pcbcalc.utils;

import android.os.Environment;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.w3c.dom.Node;
import java.io.*;
import java.util.*;

/** Exports the original public PCBCalc report templates using paragraph bookmarks. */
public class ApachePOIHelper {
    private XWPFDocument doc;

    public void openDocument(InputStream input) throws IOException {
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        if (doc != null) doc.close();
        try (InputStream stream = input) { doc = new XWPFDocument(stream); }
    }

    public void findAndReplace(String name, String value) {
        if (doc == null) throw new IllegalStateException("Open a report first");
        replaceInParagraphs(doc.getParagraphs(), name, value);
        replaceInTables(doc.getTables(), name, value);
    }

    private void replaceInTables(List<XWPFTable> tables, String name, String value) {
        for (XWPFTable table : tables) for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                replaceInParagraphs(cell.getParagraphs(), name, value);
                replaceInTables(cell.getTables(), name, value);
            }
        }
    }

    private void replaceInParagraphs(List<XWPFParagraph> paragraphs, String name, String value) {
        for (XWPFParagraph paragraph : paragraphs) {
            for (CTBookmark bookmark : new ArrayList<>(paragraph.getCTP().getBookmarkStartList())) {
                String key = bookmark.getName();
                if (!key.equals(name) && !key.matches(java.util.regex.Pattern.quote(name) + "_[1-8]")) continue;
                Node start = bookmark.getDomNode();
                Node end = start.getNextSibling();
                while (end != null) {
                    if ("bookmarkEnd".equals(end.getLocalName()) &&
                        bookmark.getId().toString().equals(end.getAttributes().getNamedItemNS(
                            "http://schemas.openxmlformats.org/wordprocessingml/2006/main", "id").getNodeValue())) break;
                    end = end.getNextSibling();
                }
                if (end == null) throw new IllegalArgumentException("Unclosed report bookmark: " + key);
                Node next = start.getNextSibling();
                while (next != end) {
                    Node remove = next; next = next.getNextSibling(); start.getParentNode().removeChild(remove);
                }
                XWPFRun run = paragraph.createRun();
                run.setText(value); run.setFontFamily("Arial"); run.setFontSize(11);
                start.getParentNode().insertBefore(run.getCTR().getDomNode(), end);
            }
        }
    }

    public String saveCurrentDocument(String folderName, String fileName) throws IOException {
        if (doc == null) throw new IllegalStateException("Open a report first");
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!directory.isDirectory() && !directory.mkdirs()) throw new IOException("Cannot create report directory");
        try (OutputStream output = new FileOutputStream(new File(directory, fileName))) { doc.write(output); }
        return directory.getAbsolutePath() + File.separator;
    }
}
