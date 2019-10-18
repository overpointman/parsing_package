package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
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
    public static final String BEAN_PATH = "bean.";

    /**
     * map 转 XML Document
     * @param list map的key是xml的标签<>名，val是标签下的内容
     * @return
     */
    public static String MapToDom(List list) throws IOException, DocumentException {
        Map map = new LinkedHashMap();
        map.put("data", list.get(0));
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
                boolean isPresents = isPresent(getFirstKeyOrNull(map));
                for (Object obj : value) {
                    Element element = root.addElement(en.getKey());
                    if (isPresents) {
                        String s = getDocument(obj);
                        Document document = DocumentHelper.parseText(s);
                        for (Element el : (List<Element>) document.getRootElement().elements()) {
                            Element typeElement1 = element.addElement(el.getName());
                            typeElement1.setText(el.getText());//添加节点内容
                        }
                    } else {
                        if (obj instanceof Map) {
                            MapToElement(element, (Map<String, ?>) obj);
                        } else {
                            element.add(DocumentHelper.createText(obj.toString()));
                        }
                    }
                }
            } else {
                Element element = root.addElement(en.getKey());
                if (isPresent(en.getKey())) {
                    String s = getDocument(en.getValue());
                    Document document = DocumentHelper.parseText(s);
                    for (Element el : (List<Element>) document.getRootElement().elements()) {
                        Element typeElement1 = element.addElement(el.getName());
                        typeElement1.setText(el.getText());//添加节点内容
                    }
                } else {
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
    public static List DomToMap(String xml) throws DocumentException, InstantiationException, IllegalAccessException {
        Document document = DocumentHelper.parseText(xml);
        return DomToMap(document);
    }

    /**
     * document 转 Map
     * @param document
     * @return
     */
    public static List DomToMap(Document document) throws IllegalAccessException, InstantiationException {
        Element rootElement = document.getRootElement();
        Map<String, Object> map = new LinkedHashMap();
        ElementToMap(rootElement, map);
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
    public static Object ElementToMap(Element element, Map<String, Object> map) throws InstantiationException, IllegalAccessException {
        List<Element> elements = element.elements();
        if (elements.size() == 0) {
            if (null != map) {
                map.put(element.getName(), element.getTextTrim());
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
                    if (isPresent(currentName)) {
                        list.add(Xml2BeanUtils.xmlStrToBean(element2.asXML(), getClassInstance(currentName)));
                    } else {
                        list.add(ElementToMap(element2, null));
                    }
                } else if (map2.containsKey(currentName)) {
                    // 第二次 包含这个key了需要改成List
                    repetitionName = currentName;
                    Object remove = map2.remove(repetitionName);
                    List list = new ArrayList();
                    list.add(remove);
                    if (isPresent(currentName)) {
                        list.add(Xml2BeanUtils.xmlStrToBean(element2.asXML(), getClassInstance(currentName)));
                        map2.put(currentName, list);
                    } else {
                        list.add(ElementToMap(element2, null));
                        map2.put(currentName, list);
                    }
                } else {
                    // 没有这个key
                    if (isPresent(currentName)) {
                        map2.put(currentName, Xml2BeanUtils.xmlStrToBean(element2.asXML(), getClassInstance(currentName)));
                        ElementToMap(element2, map2);
                    } else {
                        ElementToMap(element2, map2);
                    }
                }
            }
            return map2;
        }
    }

    /**
     * 查询当前是否有这个bean
     * @param className
     * @return java.lang.Class<?>
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/18 09:24
     **/
    public static boolean isPresent(String className) {
        try {
            Thread.currentThread().getContextClassLoader().loadClass(BEAN_PATH + className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取当前类的实例
     * @param className
     * @return java.lang.Class<?>
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/18 09:25
     **/
    public static Class getClassInstance(String className) {
        try {
            if (isPresent(className))
                return Class.forName(BEAN_PATH + className);
            else return null;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
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

    /**
     * 将对象转换成xml
     * @param b
     * @return org.dom4j.Document
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/18 10:56
     **/
    public static String getDocument(Object b) {
        Document document = DocumentHelper.createDocument();
        try {
// 创建根节点元素
            Element root = document.addElement(b.getClass().getSimpleName());
            Field[] field = b.getClass().getDeclaredFields(); // 获取实体类b的所有属性，返回Field数组
            for (int j = 0; j < field.length; j++) { // 遍历所有有属性
                String name = field[j].getName(); // 获取属属性的名字
                if (!name.equals("serialVersionUID")) {//去除串行化序列属性
                    String getsetname = name.substring(0, 1).toUpperCase()
                            + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    Method m = b.getClass().getMethod("get" + getsetname);
                    String propertievalue;
                    if (field[j].getType().toString().contains("Date")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        propertievalue = sdf.format(m.invoke(b));// 获取属性值
                    } else {
                        propertievalue = (String) m.invoke(b);// 获取属性值
                    }
                    Element propertie = root.addElement(name);
                    propertie.setText(propertievalue);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return document.getRootElement().asXML();
    }
}