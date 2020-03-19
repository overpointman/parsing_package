package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoapUtils {
    public static String getSoapBody(String soapXml) {
        // 创建 Pattern 对象
        Pattern r = Pattern.compile("(?i)<!\\[cdata\\[(.*)\\]\\]>");
        // 现在创建 matcher 对象
        Matcher m = r.matcher(soapXml);
        if (m.find()) {
            // 成功
            String xml = m.group(0).replaceAll("(?i)<!\\[cdata\\[", "")
                    .replace("]]>", "");
            return xml;
        }
        return null;
    }

    public static String getHead(String soapXml) throws DocumentException {
        Document document = DocumentHelper.parseText(soapXml);
        Element root = document.getRootElement();
        for (Element el : (List<Element>) root.elements()) {
            if (el.getName().toLowerCase().contains("header")) {
                return el.asXML();
            }
        }
        return null;
    }

    public static boolean checkHead(String xml) {
        if (xml != null) {
            Document document = null;
            try {
                document = DocumentHelper.parseText(xml);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element rootElement = document.getRootElement();
            List<Element> els = rootElement.elements();
            StringBuilder sb = new StringBuilder();
            int listSize = els.size();
            String sign = "";
            for (int i = 0; i < listSize; i++) {
                if (!els.get(i).getName().equals("sign")) {
                    sb.append(els.get(i).getName() + "="
                            + els.get(i).getTextTrim());
                    if (i != listSize - 1)
                        sb.append("&");
                } else {
                    sign = els.get(i).getTextTrim();
                }
            }
            // JDK6的 base64,仅适用于JDK6,7 不建议JDK8使用
            String jdk6EncodeRes = javax.xml.bind.DatatypeConverter
                    .printBase64Binary(sb.toString().getBytes());
            // 解密String jdk6DecodeRes = new
            // String(javax.xml.bind.DatatypeConverter.parseBase64Binary(jdk6EncodeRes));
            if (sign.equals(jdk6EncodeRes)) {
                return true;
            }
        }
        return false;
    }

    public static String getAction(String xml) {
        // 创建 Pattern 对象
        Pattern r = Pattern.compile("(?i)<action>(.*)</action>");
        // 现在创建 matcher 对象
        Matcher m = r.matcher(xml);
        if (m.find()) {
            // 成功
            String action = m.group(0).replaceAll("(?i)<action>", "").replace("</action>", "");
            return action;
        }
        return null;
    }
}
