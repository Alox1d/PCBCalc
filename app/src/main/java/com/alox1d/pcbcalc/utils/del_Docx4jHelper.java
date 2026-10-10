package com.alox1d.pcbcalc.utils;




//public class Docx4jHelper {
//
//    public static void replaceText(ContentAccessor c) throws Exception {
//
//        for (Object p : c.getContent()) {
//            if (p instanceof ContentAccessor)
//                replaceText((ContentAccessor) p);
//
//            else if (p instanceof JAXBElement) {
//                Object v = ((JAXBElement) p).getValue();
//
//                if (v instanceof ContentAccessor)
//                    replaceText((ContentAccessor) v);
//
//                else if (v instanceof org.docx4j.wml.Text) {
//                    org.docx4j.wml.Text t = (org.docx4j.wml.Text) v;
//                    String text = t.getValue();
//
//                    if (text != null) {
//                        t.setSpace("preserve"); // needed?
//                        t.setValue(replaceParams(text));
//                    }
//                }
//            }
//        }
//    }
//
////    static Pattern paramPatern = Pattern.compile("(?i)(\\$\\{([\\w\\.]+)\\})");
//    static Pattern paramPatern = Pattern.compile("MI_VARIABLE");
//
//    static String replaceParams(String text) {
//        Matcher m = paramPatern.matcher(text);
//
//        if (!m.find())
//            return text;
//
//
//        StringBuffer sb = new StringBuffer();
//        String param, replacement;
//
//        do {
//            param = m.group(2); // why 2???
//
//            if (param != null) {
//                replacement = getParamValue(param);
//                m.appendReplacement(sb, replacement);
//            } else
//                m.appendReplacement(sb, "");
//        }
//        while (m.find());
//
//        m.appendTail(sb); // ????
//        return sb.toString();
//    }
//
//    static String getParamValue(String name) {
//        // replace from map or something else
//        return name;
//    }
//}
