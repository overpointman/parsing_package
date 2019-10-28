package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/25 15:24
 **/
public class Test4 {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(new Date()));

        System.out.println(sdf.parse("2019-09-10 12:20:30"));

        System.out.println(new SimpleDateFormat("yyyy-MM-ddhh:mm:ss").parse("2019-09-10 12:20:30"));
    }
}
