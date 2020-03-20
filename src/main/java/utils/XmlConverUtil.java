package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/16 16:11
 **/
public class XmlConverUtil {

    private XmlConverUtil() {

    }

    /**
     * list 转 xml
     * @param list map的key是xml的标签<>名，val是标签下的内容
     * @return 返回转换后的xml
     */
    @SuppressWarnings("unchecked")
    public static String listToXml(List<Map<String, Object>> list) {
        return listToXml(list, false);
    }

    /**
     * list 转 xml
     * @param list map的key是xml的标签<>名，val是标签下的内容
     * @return 返回转换后的xml
     */
    @SuppressWarnings("unchecked")
    public static String listToXml(List<Map<String, Object>> list, boolean showDSQL) {
        //取list下的map，相当于xml的头结点开始
        Map<String, Object> linkedMap = list.get(0);
        Document document = DocumentHelper.createDocument();
        //头结点名称
        String rootName = linkedMap.keySet().iterator().next();
        Element root = document.addElement(rootName);
        //调用递归，进行数据组装
        MapToElement(root, (Map<String, Object>) linkedMap.get(rootName), showDSQL);
        //返回xml的字符串
        return root.asXML();
    }

    /**
     * map 转 xml
     * 默认不装载DSQL节点
     * @param map
     * @return java.lang.String
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/28 16:48
     **/
    public static String mapToXml(Map<String, Object> map) {
        return mapToXml(map, false);
    }

    /**
     * map 转 xml
     * @param map      集合
     * @param showDSQL 是否装载DSQL节点，是true;否false
     * @return java.lang.String 返回一个xml
     * @throws
     * @author yuntian 317526763@qq.com
     * @date 2019/10/28 16:49
     **/
    public static String mapToXml(Map<String, Object> map, boolean showDSQL) {
        //取list下的map，相当于xml的头结点开始
        Document document = DocumentHelper.createDocument();
        //头结点名称
        String rootName = map.keySet().iterator().next();
        Element root = document.addElement(rootName);
        //调用递归，进行数据组装
        MapToElement(root, (Map<String, Object>) map.get(rootName), showDSQL);
        //返回xml的字符串
        return root.asXML();
    }

    /**
     * 递归调用
     * map 转 XML Element map的key是xml的标签<>名，val是标签下的内容
     * @param map      作为遍历的map
     * @param root     节点,作为遍历的根节点和起始调用
     * @param showDSQL 是否包含DSQL的节点
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void MapToElement(Element root, Map<String, Object> map, boolean showDSQL) {
        //为空则直接返回
        if (null == map) {
            return;
        }
        //将map进行遍历
        for (Map.Entry<String, ?> en : map.entrySet()) {
            //对空节点作处理，值为""
            if (null == en.getValue()) {
                root.addElement(en.getKey()).add(DocumentHelper.createText(""));
                continue;
            }
            //类型判定，map，list，以及具体的字段转换
            if (en.getValue() instanceof Map) {
                Element el = root.addElement(en.getKey());
                MapToElement(el, (Map<String, Object>) en.getValue(), showDSQL);
            } else if (en.getValue() instanceof List) {
                //list转换，判断值类型，map则进行递归，不是map则将值填入
                List<?> value = (List<?>) en.getValue();
                for (Object obj : value) {
                    Element el = root.addElement(en.getKey());
                    if (obj instanceof Map) {
                        MapToElement(el, (Map<String, Object>) obj, showDSQL);
                    } else {
                        el.add(DocumentHelper.createText(obj.toString()));
                    }
                }
            } else {
                //排除DSQL_TABLENAME的转换，避免转换后的xml出现DSQL_TABLENAME节点
                if (en.getKey().equals("DSQL_TABLENAME") && !showDSQL) {
                    continue;
                } else {
                    Element el = root.addElement(en.getKey());
                    el.add(DocumentHelper.createText(en.getValue().toString()));
                }
            }
        }
    }

    /**
     * xml 转 list
     * @param xml 一个传入的xml字符串
     * @return 返回一个list集合
     */
    public static List xmlToList(String xml) throws DocumentException {
        Map<String, String> tableName = new LinkedHashMap<String, String>();
        Document document = DocumentHelper.parseText(xml);
        return xmlToList(document, tableName);
    }

    /**
     * xml 转 list
     * @param xml       一个传入的xml字符串
     * @param tableName 表和节点映射,没有就new一个无内容的map就行
     * @return 返回一个xml
     */
    public static List xmlToList(String xml, Map<String, String> tableName) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        return xmlToList(document, tableName);
    }

    /**
     * document 转 Map
     * @return 返回一个list集合
     */
    public static Map xmlToMap(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element rootElement = document.getRootElement();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        ElementToMap(rootElement, map, null);
        return map;
    }

    /**
     * document 转 list
     * @param document  一个解析后的document对象
     * @param tableName 表和节点映射,没有就new一个无内容的map就行
     * @return 返回一个list集合
     */
    public static List xmlToList(Document document, Map<String, String> tableName) {
        Element rootElement = document.getRootElement();
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        ElementToMap(rootElement, map, tableName);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        return list;
    }

    /**
     * 递归调用
     * 将 xml 装 map，key为节点，value为节点下内容
     * @param el        节点名
     * @param map       装载的map内容
     * @param tableName 表和节点映射,没有就new一个无内容的map就行
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object ElementToMap(Element el, Map<String, Object> map, Map<String, String> tableName) {
        List<Element> els = el.elements();
        //最下层节点，将内容装进去
        if (els.size() == 0) {
            if (null != map) {
                map.put(el.getName(), "".equals(el.getTextTrim()) ? null : el.getTextTrim());
            }
            return el.getTextTrim();
        } else {
            Map<String, Object> innerMap = new LinkedHashMap();
            if (null != map) {
                map.put(el.getName(), innerMap);
            }
            //作为用来判定是否有多个相同节点的变量
            String repetitionName = "";
            for (Element innerEl : els) {
                String currentName = innerEl.getName();
                if (repetitionName.equals(currentName)) {
                    // 第二次以上 包含这个key了 直接添加到List
                    List tempList = (List) innerMap.get(repetitionName);
                    tempList.add(ElementToMap(innerEl, null, tableName));
                } else if (innerMap.containsKey(currentName)) {
                    // 第二次 包含这个key了需要改成List
                    repetitionName = currentName;
                    Object remove = innerMap.remove(repetitionName);
                    List tempList = new ArrayList();
                    tempList.add(remove);
                    tempList.add(ElementToMap(innerEl, null, tableName));
                    innerMap.put(currentName, tempList);
                } else {
                    // 没有这个key
                    if (tableName != null && tableName.get(el.getName()) != null) {
                        innerMap.put("DSQL_TABLENAME", tableName.get(el.getName()));
                    }
                    ElementToMap(innerEl, innerMap, tableName);
                }
            }
            return innerMap;
        }
    }

    /**
     * 获取map中第一个数据值
     * @param map 数据源
     * @return
     */
    public static Object getFirstValueOrNull(Map<String, Object> map) {
        Object obj = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

    /**
     * 通过表达式取得节点下的数据,并将其装成list
     * @param expression 表达式，例如根节点为root，root下有data节点，则表达式为:/root/data
     * @param xml        需要进行解析的xml
     * @return java.util.List
     * @throws DocumentException 文档异常
     * @author yuntian 317526763@qq.com
     * @date 2019/10/28 16:44
     **/
    @SuppressWarnings("unchecked")
    public static List findListWithNodeName(String expression, String xml) throws DocumentException {
        Document doc = DocumentHelper.parseText(xml);
        List<Element> list = doc.selectNodes(expression);
        List returnList = new ArrayList();
        for (Element el : list) {
            Map l = XmlConverUtil.xmlToMap(el.asXML());
            returnList.add(getFirstValueOrNull(l));
        }
        return returnList;
    }

    public static List makeListWithTable(List list) {
        List returnList = new ArrayList();
        if (list == null || list.size() == 0) {
            return null;
        }
        if (list.get(0) instanceof Map) {
            Map map = (Map) list.get(0);
            returnList.addAll(makeListWithTable(map));
        } else if (list.get(0) instanceof List) {
            for (Object obj : list) {
                returnList.addAll(makeListWithTable((List) obj));
            }
        } else {
            return null;
        }
        return returnList;
    }

    @SuppressWarnings("unchecked")
    public static List makeListWithTable(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        List returnList = new ArrayList();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof Map) {
                returnList.addAll(makeListWithTable((Map) obj));
            } else if (obj instanceof List) {
                for (Object objs : (List) obj) {
                    returnList.addAll(makeListWithTable((Map) objs));
                }
            } else if (obj == null) {
            } else {
                returnList.add(map);
                return returnList;
            }
        }
        return returnList;
    }

    public static Pattern pattern = Pattern.compile("^(\\d{4})(-|\\/)(\\d{2})\\2(\\d{2}).*$");

    public static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<Object> getDSQLList(String xml, String namespace, String expression, Map<String, String> tableName)
            throws DocumentException, ParseException {
        //XpathUtil.getNodeValue(namespace, expression, xml);
        List<Object> resultList = new ArrayList<Object>();
        List<Map<String, Object>> list = null;
        list = XmlConverUtil.xmlToList(xml, tableName);
        // 处理map中的需要传的数据
        resultList = XmlConverUtil.makeListWithTable(list);
        for (Object obj : resultList) {
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() != null && entry.getValue().toString().trim().length() == 19 && pattern.matcher((String) entry.getValue()).matches()) {
                    map.put(entry.getKey(), date.parse((String) entry.getValue()));
                }
            }
        }
        return resultList;
    }
}