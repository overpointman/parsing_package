package utils;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static utils.XmlConverUtil.DomToMap;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/18 16:27
 **/
public class Test {

    public static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\parsing_package\\src\\main\\resources\\";

    public static final String FILE_NAME = FILE_PATH + "main_exam_call.xml";

    public static void main(String[] args) throws DocumentException, IOException, IllegalAccessException, InstantiationException {
//        String xml = FileTool.readStringFromFile(FILE_NAME, "UTF-8");
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><reqxml><head></head><body><data><request><request_no>ha201102113366668</request_no><patient_type>1</patient_type><org_code>c001</org_code><system_source>1</system_source><org_name>不要删数据</org_name><visit_id>22a0f9e0-4454-11dc-a6be-3603d6866807</visit_id><visit_no>123456</visit_no><patient_id>1</patient_id><patient_no>456789</patient_no><is_baby></is_baby><patient_name>王患者</patient_name><gender_code>1</gender_code><age>1.0</age><age_unit>1</age_unit><med_card_no></med_card_no><id_card>1234567890</id_card><health_card_no>1234567890</health_card_no><health_file_no>12345678901234567</health_file_no><purpose>keepsystolicbloodpressurebetween90and110mmhg.</purpose><telphone></telphone><present_address></present_address><nation_code></nation_code><nation_name></nation_name><nationality_code></nationality_code><nationality_name></nationality_name><marriage_code></marriage_code><present_history>对患者本次疾病相关的主要症状及其持续时间的描述</present_history><complanit></complanit><symptoms_info>对患者出现症状的详细描述</symptoms_info><diag_code></diag_code><diag_name></diag_name><request_code>120109197706015518</request_code><requester>李医生</requester><request_dept_code>1234567890</request_dept_code><request_dept_name>呼吸内科</request_dept_name><request_date>2012-02-0203:03:03</request_date><ward_code>001</ward_code><ward_name>1病区</ward_name><room_no>001</room_no><bed_no>001</bed_no><dept_code>001</dept_code><dept_name>呼吸内科</dept_name><is_emergency>0</is_emergency><birthday>1957-03-2300:00:00</birthday><visit_count></visit_count><maxitem_id></maxitem_id><maxitem_code></maxitem_code><maxitem_name></maxitem_name><pay_flag></pay_flag><sum_cost></sum_cost><specimen_type_id></specimen_type_id><specimen_no>sp1234567890</specimen_no><specimen_type></specimen_type><specimen_bar_no></specimen_bar_no><speciment_tube_color></speciment_tube_color><specimen_flag>对受检标本状态的描述</specimen_flag><specimen_pixative_name>福尔马林</specimen_pixative_name><bar_code_build_date></bar_code_build_date><exec_dept_code></exec_dept_code><exec_dept_name></exec_dept_name><cancel_flag>0</cancel_flag><exec_flag></exec_flag><sampler_id></sampler_id><sampler></sampler><sample_date>2013-03-0302:04:04</sample_date><sender_id></sender_id><sender></sender><send_date></send_date><plan_start_date>2011-02-0203:03:03</plan_start_date><plan_end_date>2011-02-0203:03:03</plan_end_date><print_flag></print_flag><print_count></print_count><gc_flag></gc_flag><appointment_flag></appointment_flag><remark>对下达申请单的补充说明和注意事项提示</remark></request><request_details><detail><request_no>test</request_no><patient_type>2</patient_type><item_serial_no>test</item_serial_no><org_code>test</org_code><system_source>test</system_source><org_name>test</org_name><patient_id>test</patient_id><visit_id>test</visit_id><order_id>test</order_id><item_id></item_id><item_code></item_code><item_name></item_name><cost></cost><sampler_id></sampler_id><sampler></sampler><sample_date></sample_date><sender_id></sender_id><sender></sender><send_date></send_date><cancel_flag></cancel_flag></detail><detail><request_no>test</request_no><patient_type>2</patient_type><item_serial_no>test</item_serial_no><org_code>test</org_code><system_source>test</system_source><org_name>test</org_name><patient_id>test</patient_id><visit_id>test</visit_id><order_id>test</order_id><item_id></item_id><item_code></item_code><item_name></item_name><cost></cost><sampler_id></sampler_id><sampler></sampler><sample_date></sample_date><sender_id></sender_id><sender></sender><send_date></send_date><cancel_flag></cancel_flag></detail></request_details></data></body></reqxml>";
        Map<String, String> tableName = new LinkedHashMap<String, String>();
        tableName.put("detail", "PT_LIS_EXAMINE_DETAIL");
        tableName.put("request", "PT_LIS_EXAMINE");
        tableName.put("deptreport", "PT_LIS_EXAMINE");
        tableName.put("resultdetail", "PT_LIS_EXAMINE");
        long startTime = System.currentTimeMillis();   //获取开始时间
        List domToMap = DomToMap(xml, tableName);
        System.out.println("xml to map:" + domToMap);
        long endTime = System.currentTimeMillis(); //获取结束时间
        System.out.println("程序运行时间： " + (endTime - startTime) + "ms");

        Map<String, Object> obj = (Map) ((Map) ((Map) domToMap.get(0)).get("body")).get("data");
        List realList = new ArrayList();

        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            if (entry.getKey().equals("request")) {
                realList.add(entry.getValue());
            } else {
                Map<String, Object> map2 = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> entry2 : map2.entrySet()) {
                    if (entry2.getValue() instanceof List) {
                        List list2 = (List) entry2.getValue();
                        realList.addAll(list2);
                    } else if (entry2.getValue() instanceof Map) {
                        realList.add(entry2.getValue());
                    }
                }
            }
        }

        System.out.println();

//        long startTime2 = System.currentTimeMillis();   //获取开始时间
//        String mapToDom = MapToDom(domToMap);
//        System.out.println("map to xml:" + mapToDom);
//        long endTime2 = System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： " + (endTime2 - startTime2) + "ms");
    }
}