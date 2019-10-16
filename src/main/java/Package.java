import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/16 10:08
 **/
public class Package {

    private static final String xmlName = "test.xml";

    public static void main(String[] args) throws URISyntaxException {
        URL url = Package.class.getClassLoader().getResource("test.xml");
        Map<String, Object> map = multilayerXmlToMap(readFileByBytes(url.toURI()).trim());
        System.out.println(multilayerMapToXml(map, false));
    }

    /**
     * (多层)map转换为xml格式字符串
     * @param map     需要转换为xml的map
     * @param isCDATA 是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    public static String multilayerMapToXml(Map<String, Object> map, boolean isCDATA) {
        String parentName = "xml";
        Document doc = DocumentHelper.createDocument();
        doc.addElement(parentName);
        String xml = recursionMapToXml(doc.getRootElement(), parentName, map, isCDATA);
        return formatXML(xml);
    }

    /**
     * multilayerMapToXml核心方法，递归调用
     * @param element    节点元素
     * @param parentName 根元素属性名
     * @param map        需要转换为xml的map
     * @param isCDATA    是否加入CDATA标识符 true:加入 false:不加入
     * @return xml字符串
     */
    @SuppressWarnings("unchecked")
    public static String recursionMapToXml(Element element, String parentName, Map<String, Object> map, boolean isCDATA) {
        Element xmlElement = element.addElement(parentName);
        for (String key : map.keySet()) {
            Object obj = map.get(key);
            if (obj instanceof Map) {
                recursionMapToXml(xmlElement, key, (Map<String, Object>) obj, isCDATA);
            } else {
                String value = obj == null ? "" : obj.toString();
                if (isCDATA) {
                    xmlElement.addElement(key).addCDATA(value);
                } else {
                    xmlElement.addElement(key).addText(value);
                }
            }
        }
        return xmlElement.asXML();
    }

    /**
     * (多层)xml格式字符串转换为map
     * @param xml xml字符串
     * @return 第一个为Root节点，Root节点之后为Root的元素，如果为多层，可以通过key获取下一层Map
     */
    public static Map<String, Object> multilayerXmlToMap(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == doc) {
            return map;
        }
        // 获取根元素
        Element rootElement = doc.getRootElement();
        recursionXmlToMap(rootElement, map);
        return map;
    }

    /**
     * multilayerXmlToMap核心方法，递归调用
     * @param element 节点元素
     * @param outmap  用于存储xml数据的map
     */
    @SuppressWarnings("unchecked")
    private static void recursionXmlToMap(Element element, Map<String, Object> outmap) {
        // 得到根元素下的子元素列表
        List<Element> list = element.elements();
        int size = list.size();
        if (size == 0) {
            // 如果没有子元素,则将其存储进map中
            outmap.put(element.getName(), element.getTextTrim());
        } else {
            // innermap用于存储子元素的属性名和属性值
            Map<String, Object> innermap = new HashMap<String, Object>();
            // 遍历子元素
            for (Element el : list) {
                recursionXmlToMap(el, innermap);
            }
            outmap.put(element.getName(), innermap);
        }
    }

    /**
     * 格式化xml,显示为容易看的XML格式
     * @param xml 需要格式化的xml字符串
     * @return
     */
    public static String formatXML(String xml) {
        String requestXML = null;
        try {
            // 拿取解析器
            SAXReader reader = new SAXReader();
            Document document = reader.read(new StringReader(xml));
            if (null != document) {
                StringWriter stringWriter = new StringWriter();
                // 格式化,每一级前的空格
                OutputFormat format = new OutputFormat("	", true);
                // xml声明与内容是否添加空行
                format.setNewLineAfterDeclaration(false);
                // 是否设置xml声明头部
                format.setSuppressDeclaration(false);
                // 是否分行
                format.setNewlines(true);
                XMLWriter writer = new XMLWriter(stringWriter, format);
                writer.write(document);
                writer.flush();
                writer.close();
                requestXML = stringWriter.getBuffer().toString();
            }
            return requestXML;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读文件内容
     * @param uri
     * @return java.lang.String
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/16 14:26
     **/
    public static String readFileByBytes(URI uri) {
        File file = new File(uri);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        StringBuffer content = new StringBuffer();

        try {
            byte[] temp = new byte[1024];
            FileInputStream fileInputStream = new FileInputStream(file);
            while (fileInputStream.read(temp) != -1) {
                content.append(new String(temp));
                temp = new byte[1024];
            }

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
