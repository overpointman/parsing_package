/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/23 13:57
 **/
public class Test2 {
    public static void main(String[] args) {
//        Resource resource = new ClassPathResource("/base.properties");
//        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        // JDK6的 base64
        String jdk6EncodeRes = javax.xml.bind.DatatypeConverter.printBase64Binary("version=1&timestamp=20191001091221&request_id=123456&source_system=his&object_system=lis&action=request_call&code=lis41".getBytes());
        System.out.println("jdk6 base64 encode:" + jdk6EncodeRes);
        String jdk6DecodeRes = new String(javax.xml.bind.DatatypeConverter.parseBase64Binary(jdk6EncodeRes));
        System.out.println("jdk6 base64 decode:" + jdk6DecodeRes);
    }
}
