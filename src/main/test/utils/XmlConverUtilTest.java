package utils;

import org.dom4j.DocumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlConverUtilTest {

    public static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\parsing_package\\src\\main\\resources\\";

    public static final String FILE_NAME = FILE_PATH + "main_exam_call.xml";

    List domToMap;
    Map<String, String> tableName;
    String xml = FileTool.readStringFromFile(FILE_NAME, "UTF-8");
    long startTime;
    long endTime;

    @Before
    public void before() {
        tableName = new LinkedHashMap<String, String>();
        tableName.put("request", "mris_main");
        tableName.put("detail", "mris_detail");
        tableName.put("resultdetail", "mris_resultdetail");
        tableName.put("deptreport", "mris_deptreport");
        long startTime = System.currentTimeMillis();   //获取开始时间
    }

    @After
    public void after() {
        endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");
    }

    @Test
    public void listToXml() {
        String mapToDom = XmlConverUtil.listToXml(domToMap);
        System.out.println("map to xml:" + mapToDom);
    }

    @Test
    public void xmlToList() {
        try {
            domToMap = XmlConverUtil.xmlToList(xml, tableName);
            System.out.println();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("xml to map:" + domToMap);
    }

    @Test
    public void testXmlToList() {
        try {
            domToMap = XmlConverUtil.xmlToList(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("xml to map:" + domToMap);
    }
}