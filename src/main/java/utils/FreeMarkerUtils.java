package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2020/3/12 10:16
 **/
public class FreeMarkerUtils {
    public static String buildXml(String filePath, String ftlName, Map dataMap) throws IOException, TemplateException {

        //设置配置类
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        //设置模板所在的目录
        configuration.setDirectoryForTemplateLoading(new File(filePath));
        //设置字符集
        configuration.setDefaultEncoding("utf-8");
        //加载模板
        Template template = configuration.getTemplate(ftlName);
        //创建字符输出
        StringWriter writer = new StringWriter();
        //输出数据模型
        template.process(dataMap, writer);
        String xml = writer.toString();
        writer.close();
        return xml;
    }
}
