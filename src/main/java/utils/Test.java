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

    public static final String FILE_NAME = FILE_PATH + "result_call.xml";

    public static void main(String[] args) throws DocumentException, IOException, IllegalAccessException, InstantiationException {
        String xml = FileTool.readStringFromFile(FILE_NAME, "UTF-8");
        List domToMap = DomToMap(xml);
        System.out.println("xml to map:" + domToMap);
        String mapToDom = MapToDom(domToMap);
        System.out.println("map to xml:" + mapToDom);
    }
}
