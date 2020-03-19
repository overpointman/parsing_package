package utils;

import freemarker.template.TemplateException;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class FreeMarkerUtilsTest {

    private Map dataMap = new HashMap();
    private String path = "C:\\Users\\Administrator\\IdeaProjects\\parsing_package\\src\\main\\test\\resources";
    private String ftlName = "template.ftl";

    @Before
    public void before() throws DocumentException {

        List dataList = new ArrayList();

        Map mainMap = new HashMap();
        Map detailMap = new HashMap();

        mainMap.put("request_no", "123");
        mainMap.put("name", "zhang");

        detailMap.put("detail_id", "1234");
        detailMap.put("type_name", "operation");

        dataList.add(mainMap);
        dataList.add(detailMap);

        dataMap.put("test", dataList);
    }

    @Test
    public void buildXml() throws IOException, TemplateException {
        String xml = FreeMarkerUtils.buildXml(path, ftlName, dataMap);
        assertNotNull(xml);
        System.out.println(xml);
    }
}