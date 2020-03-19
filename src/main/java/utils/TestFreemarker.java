package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFreemarker {
    public static void process(List list) {
        String dir = "C:\\Users\\Administrator\\IdeaProjects\\parsing_package\\src\\main\\java\\utils";
        Configuration conf = new Configuration();
        try {
            //加载模板文件(模板的路径)
            conf.setDirectoryForTemplateLoading(new File(dir));
            // 加载模板
            Template template = conf.getTemplate("\\template.ftl");
            // 定义数据
            Map root = new HashMap();
            root.put("test", list);
            // 定义输出
            Writer out = new FileWriter(dir + "/freemarker.html");
            template.process(root, out);
            System.out.println("转换成功");
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}