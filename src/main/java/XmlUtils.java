import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/16 14:39
 **/
public class XmlUtils {
    public static void main(String[] args) throws IOException, DocumentException, URISyntaxException, ClassNotFoundException {
        SAXReader reader = new SAXReader();
        URL url = Package.class.getClassLoader().getResource("test.xml");
        String str = Package.readFileByBytes(url.toURI()).trim();
        Document doc = DocumentHelper.parseText(str);
        //System.out.println(doc.asXML());
        long beginTime = System.currentTimeMillis();
        Map<String, Object> map = XmlUtils.Dom2Map(doc);
        System.out.println(map.toString());

        FileOutputStream fos = new FileOutputStream("d.txt", true);
        //文件的序列化
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        //writeObject 方法用于将对象写入流中。所有对象（包括 String 和数组）都//可以通过 writeObject 写入。
        oos.writeObject(map);
        oos.close();

//        FileInputStream fis = new FileInputStream("d.txt");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        Map<String, Object> map = (HashMap<String, Object>) ois.readObject();
//        ois.close();
// System.out.println(map);
//        System.out.println(map);
//        for (String key : map.keySet()) {
//            System.out.println(map.get(key) instanceof Map);
//        }

        //System.out.println(converter(map));
    }


    @SuppressWarnings("unchecked")
    public static Map<String, Object> Dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        if (root.elements().size() > 0) {
            map.put(root.getName(), Dom2Map(root));
        } else
            map.put(root.getName(), root.getText());
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Map Dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (Object o : list) {
                Element iter = (Element) o;
                List mapList = new ArrayList();
                if (iter.elements().size() > 0) {
                    Map m = Dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }

}
