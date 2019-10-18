package utils;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.List;

import static utils.XmlConverUtil.DomToMap;
import static utils.XmlConverUtil.MapToDom;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/18 16:27
 **/
public class Test {

    public static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\parsing_package\\src\\main\\resources\\";

    public static final String FILE_NAME = FILE_PATH + "main_exam_call.xml";

    public static void main(String[] args) throws DocumentException, IOException, IllegalAccessException, InstantiationException {
        String xml = FileTool.readStringFromFile(FILE_NAME, "UTF-8");

        long startTime = System.currentTimeMillis();   //获取开始时间
        List domToMap = DomToMap(xml);
        System.out.println("xml to map:" + domToMap);
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

        long startTime2 = System.currentTimeMillis();   //获取开始时间
        String mapToDom = MapToDom(domToMap);
        System.out.println("map to xml:" + mapToDom);
        long endTime2 = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime2 - startTime2) + "ms");



    }
}
