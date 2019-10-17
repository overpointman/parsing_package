import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Xml2MapTool {
    public static void main(String[] args) {
        String input = FileTool.readStringFromFile("C:\\Users\\Administrator\\Desktop\\parsing_package\\src\\main\\resources\\request_call.xml", "UTF-8");
        Map<String, Object> map = xml2map(input);
        System.out.println("最终生成的map如下:\n" + map);
    }

    /*  1. 有上下层次关系,必然递归最适合
        2. 递归原则 : 一个map对应一个xml节点,key为当前xml节点名,value为当前xml节点子集
        3. 如果xml节点没有子节点(叶子节点),那么map的key为xml节点名,value为xml节点内文本内容
        4. 如果xml节点有子节点,那么让子节点和新生成子map去递归(子节点和新map会关联到一起)
        5. 然后就要把子map或子list置入上一层map中
        5.2如果子map第一次生成,用上层map直接添加子map
        5.3如果子map第二次生成,用新list把两份子map添加,再用上一层map添加新list
        5.4如果子map第三/多次,用list把第三/多次子map直接添加 */

    public static Map<String, Object> xml2map(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element rootElement = doc.getRootElement();
        element2map(rootElement, map);
        return map;
    }

    /**
     * @param elmt 当前元素
     * @param map  主键为当前元素的节点名,值为当前元素的所有直接子元素
     */
    public static void element2map(Element elmt, Map<String, Object> map) {
        if (null == elmt) {
            return;
        }
        String name = elmt.getName();
        if (elmt.isTextOnly()) {
            map.put(name, elmt.getText());
        } else {
            Map<String, Object> mapSub = new HashMap<String, Object>();
            List<Element> elements = (List<Element>) elmt.elements();
            for (Element elmtSub : elements) {
                element2map(elmtSub, mapSub);
            }
            Object first = map.get(name);
            if (null == first) {
                map.put(name, mapSub);
            } else {
                if (first instanceof List<?>) {
                    ((List) first).add(mapSub);
                } else {
                    List<Object> listSub = new ArrayList<Object>();
                    listSub.add(first);
                    listSub.add(mapSub);
                    map.put(name, listSub);
                }
            }
        }
    }
}