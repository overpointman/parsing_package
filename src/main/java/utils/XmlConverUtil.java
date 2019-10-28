package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/16 16:11
 **/
public class XmlConverUtil {

    /**
     * map 转 XML Document
     * @param list map的key是xml的标签<>名，val是标签下的内容
     * @return
     */
    public static String MapToDom(List list) throws IOException, DocumentException {
        Map map = new LinkedHashMap();
        map.put("reqxml", list.get(0));
        Document document = DocumentHelper.createDocument();
        String rootName = map.keySet().iterator().next().toString();
        Element root = document.addElement(rootName);
        MapToElement(root, (Map<String, ?>) map.get(rootName));
        OutputFormat xmlFormat = new OutputFormat();
        xmlFormat.setExpandEmptyElements(true);
        StringWriter sw = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(sw, xmlFormat);
        xmlWriter.write(root);
        xmlWriter.close();
        return sw.toString();
    }

    /**
     * map 转 XML Element map的key是xml的标签<>名，val是标签下的内容
     * @param map
     * @return
     */
    public static void MapToElement(Element root, Map<String, ?> map) throws DocumentException {
        if (null == map) {
            return;
        }
        for (Map.Entry<String, ?> en : map.entrySet()) {
            if (null == en.getValue()) {
                continue;
            }
            if (en.getValue() instanceof Map) {
                Element element = root.addElement(en.getKey());
                MapToElement(element, (Map<String, ?>) en.getValue());
            } else if (en.getValue() instanceof List) {
                List<?> value = (List<?>) en.getValue();
                for (Object obj : value) {
                    Element element = root.addElement(en.getKey());
                    if (obj instanceof Map) {
                        MapToElement(element, (Map<String, ?>) obj);
                    } else {
                        element.add(DocumentHelper.createText(obj.toString()));
                    }
                }
            } else {
                if (!en.getKey().equals("DSQL_TABLENAME")) {
                    Element element = root.addElement(en.getKey());
                    element.add(DocumentHelper.createText(en.getValue().toString()));
                }
            }
        }
    }

    /**
     * document 转 Map
     * @param xml
     * @return
     */
    public static List DomToMap(String xml, Map<String, String> tableName) throws DocumentException, InstantiationException, IllegalAccessException {
        Document document = DocumentHelper.parseText(xml);
        return DomToMap(document, tableName);
    }

    /**
     * document 转 Map
     * @param document
     * @return
     */
    public static List DomToMap(Document document, Map<String, String> tableName) throws IllegalAccessException, InstantiationException {
        Element rootElement = document.getRootElement();
        Map<String, Object> map = new LinkedHashMap();
        ElementToMap(rootElement, map, tableName);
        List list = new ArrayList();
        list.add(getFirstValueOrNull(map));
        return list;
    }

    /**
     * 递归调用
     * @param element
     * @param map
     * @return
     */
    public static Object ElementToMap(Element element, Map<String, Object> map, Map<String, String> tableName) {
        List<Element> elements = element.elements();
        if (elements.size() == 0) {
            if (null != map) {
                map.put(element.getName(), "".equals(element.getTextTrim()) ? null : element.getTextTrim());
            }
            return element.getTextTrim();
        } else {
            Map<String, Object> map2 = new LinkedHashMap();
            if (null != map) {
                if (!(element.getName().equals(getFirstKeyOrNull(map)))) {
                    map.put(element.getName(), map2);
                }
            }
            String repetitionName = "";
            for (Element element2 : elements) {
                String currentName = element2.getName();
                if (repetitionName.equals(currentName)) {
                    // 第二次以上 包含这个key了 直接添加到List
                    List list = (List) map2.get(repetitionName);
                    list.add(ElementToMap(element2, null, tableName));
                } else if (map2.containsKey(currentName)) {
                    // 第二次 包含这个key了需要改成List
                    repetitionName = currentName;
                    Object remove = map2.remove(repetitionName);
                    List list = new ArrayList();
                    list.add(remove);
                    list.add(ElementToMap(element2, null, tableName));
                    map2.put(currentName, list);
                } else {
                    // 没有这个key
                    if (tableName.get(element.getName()) != null) {
                        map2.put("DSQL_TABLENAME", tableName.get(element.getName()));
                    }
                    ElementToMap(element2, map2, tableName);
                }
            }
            return map2;
        }
    }

    /**
     * 获取map中第一个数据值
     * @param map 数据源
     * @return
     */
    public static Object getFirstValueOrNull(Map<String, ?> map) {
        Object obj = null;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

    /**
     * 获取map中第一个key
     * @param map 数据源
     * @return
     */
    public static String getFirstKeyOrNull(Map<String, ?> map) {
        String obj = null;
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

}