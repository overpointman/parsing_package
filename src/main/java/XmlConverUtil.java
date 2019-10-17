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

    static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\parsing_package\\src\\main\\resources\\result_call.xml";

    public static void main(String[] args) throws DocumentException, IOException {
        String input = FileTool.readStringFromFile(FILE_PATH, "UTF-8");
        Document document = DocumentHelper.parseText(input);
        Map domToMap = DomToMap(document);
        System.out.println("xml to map:" + domToMap);
        String mapToDom = MapToDom(domToMap);
        System.out.println("map to xml:" + mapToDom);
    }


    /**
     * map 转 XML Document
     * @param map map的key是xml的标签<>名，val是标签下的内容
     * @return
     */
    public static String MapToDom(Map map) throws IOException {
        Document document = DocumentHelper.createDocument();
        System.out.println(map.keySet().iterator().next().toString());
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
    private static void MapToElement(Element root, Map<String, ?> map) {
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
                Element element = root.addElement(en.getKey());
                element.add(DocumentHelper.createText(en.getValue().toString()));
            }
        }
        return;
    }

    /**
     * document 转 Map
     * @param document
     * @return
     */
    public static Map DomToMap(Document document) {
        Element rootElement = document.getRootElement();
        Map<String, Object> map = new LinkedHashMap();
        ElementToMap(rootElement, map);
        return map;
    }

    /**
     * 递归调用
     * @param element
     * @param map
     * @return
     */
    private static Object ElementToMap(Element element, Map<String, Object> map) {
        List<Element> elements = element.elements();
        if (elements.size() == 0) {
            if (null != map) {
                map.put(element.getName(), element.getTextTrim());
            }
            return element.getTextTrim();
        } else {
            Map<String, Object> map2 = new LinkedHashMap();
            if (null != map) {
                map.put(element.getName(), map2);
            }
            String repetitionName = "";
            for (Element element2 : elements) {
                if (repetitionName.equals(element2.getName())) {
                    // 第二次以上 包含这个key了 直接添加到List
                    List list = (List) map2.get(repetitionName);
                    list.add(ElementToMap(element2, null));
                } else if (map2.containsKey(element2.getName())) {
                    // 第二次 包含这个key了需要改成List
                    repetitionName = element2.getName();
                    Object remove = map2.remove(repetitionName);
                    List list = new ArrayList();
                    list.add(remove);
                    list.add(ElementToMap(element2, null));
                    map2.put(element2.getName(), list);
                } else {
                    // 没有这个key
                    ElementToMap(element2, map2);
                }
            }
            return map2;
        }
    }
}