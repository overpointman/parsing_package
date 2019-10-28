package utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/25 09:15
 **/
public class Test2 {
    public static void main(String[] args) throws DocumentException {
        String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:esb=\"http://esb.webservice\"><soapenv:Header><version>1</version><timestamp>20191001091221</timestamp><sign>dmVyc2lvbj0xJnRpbWVzdGFtcD0yMDE5MTAwMTA5MTIyMSZyZXF1ZXN0X2lkPTEyMzQ1NiZzb3VyY2Vfc3lzdGVtPWhpcyZvYmplY3Rfc3lzdGVtPWxpcyZhY3Rpb249cmVxdWVzdF9jYWxsJmNvZGU9bGlzNDE=</sign><request_id>123456</request_id><source_system>his</source_system><object_system>lis</object_system><action>request_call</action><code>lis41</code></soapenv:Header><soapenv:Body><esb:callBussiness><message><![CDATA[<?xml version=\"1.0\" encoding=\"utf-8\"?><reqxml><head></head><body><data><request><request_no>ha201102113366668</request_no><patient_type>1</patient_type><org_code>c001</org_code><system_source>1</system_source><org_name>不要删数据</org_name><visit_id>22a0f9e0-4454-11dc-a6be-3603d6866807</visit_id><visit_no>123456</visit_no><patient_id>1</patient_id><patient_no>456789</patient_no><is_baby></is_baby><patient_name>王患者</patient_name><gender_code>1</gender_code><age>1.0</age><age_unit>1</age_unit><med_card_no></med_card_no><id_card>1234567890</id_card><health_card_no>1234567890</health_card_no><health_file_no>12345678901234567</health_file_no><purpose>keepsystolicbloodpressurebe-tween90and110mmhg.</purpose><telphone></telphone><present_address></present_address><nation_code></nation_code><nation_name></nation_name><nationality_code></nationality_code><nationality_name></nationality_name><marriage_code></marriage_code><present_history>对患者本次疾病相关的主要症状及其持续时间的描述</present_history><complanit></complanit><symptoms_info>对患者出现症状的详细描述</symptoms_info><diag_code></diag_code><diag_name></diag_name><request_code>120109197706015518</request_code><requester>李医生</requester><request_dept_code>1234567890</request_dept_code><request_dept_name>呼吸内科</request_dept_name><request_date>2012-02-0203:03:03</request_date><ward_code>001</ward_code><ward_name>1病区</ward_name><room_no>001</room_no><bed_no>001</bed_no><dept_code>001</dept_code><dept_name>呼吸内科</dept_name><is_emergency>0</is_emergency><birthday>1957-03-2300:00:00</birthday><visit_count></visit_count><maxitem_id></maxitem_id><maxitem_code></maxitem_code><maxitem_name></maxitem_name><pay_flag></pay_flag><sum_cost></sum_cost><specimen_type_id></specimen_type_id><specimen_no>sp1234567890</specimen_no><specimen_type></specimen_type><specimen_bar_no></specimen_bar_no><speciment_tube_color></speciment_tube_color><specimen_flag>对受检标本状态的描述</specimen_flag><specimen_pixative_name>福尔马林</specimen_pixative_name><bar_code_build_date></bar_code_build_date><exec_dept_code></exec_dept_code><exec_dept_name></exec_dept_name><cancel_flag>0</cancel_flag><exec_flag></exec_flag><sampler_id></sampler_id><sampler></sampler><sample_date>2013-03-0302:04:04</sample_date><sender_id></sender_id><sender></sender><send_date></send_date><signer_id></signer_id><signer></signer><sign_date>2013-03-0304:04:04</sign_date><accept_id></accept_id><accept></accept><accept_date></accept_date><executor_id></executor_id><executor></executor><execute_date></execute_date><plan_start_date>2011-02-0203:03:03</plan_start_date><plan_end_date>2011-02-0203:03:03</plan_end_date><print_flag></print_flag><print_count></print_count><auditor_id>120109197706015518</auditor_id><auditor>王医生</auditor><audit_date>2012-02-0203:03:03</audit_date><reporter_id></reporter_id><reporter></reporter><report_date></report_date><gc_flag></gc_flag><appointment_flag></appointment_flag><remark>对下达申请单的补充说明和注意事项提示</remark></request><request_details><detail><request_no>test</request_no><patient_type>2</patient_type><item_serial_no>test</item_serial_no><org_code>test</org_code><system_source>test</system_source><org_name>test</org_name><patient_id>test</patient_id><visit_id>test</visit_id><order_id>test</order_id><item_id></item_id><item_code></item_code><item_name></item_name><cost></cost><sampler_id></sampler_id><sampler></sampler><sample_date></sample_date><sender_id></sender_id><sender></sender><send_date></send_date><signer_id></signer_id><signer></signer><sign_date></sign_date><accept_id></accept_id><accept></accept><accept_date></accept_date><saver_id></saver_id><saver></saver><save_date></save_date><first_trial_id></first_trial_id><first_trial></first_trial><first_trial_date></first_trial_date><auditor_id></auditor_id><auditor></auditor><audit_date></audit_date><reporter_id></reporter_id><reporter></reporter><report_date></report_date><report_dept_code></report_dept_code><report_dept_name></report_dept_name><exec_dept_code></exec_dept_code><exec_dept_name></exec_dept_name><exec_flag></exec_flag><cancel_flag></cancel_flag><device_name></device_name><device_code></device_code></detail></request_details></data></body></reqxml>]]></message></esb:callBussiness></soapenv:Body></soapenv:Envelope>";
        String head = getHead(xml);
        System.out.println(head);
        System.out.println(checkHead(head));
    }

    public static String getHead(String soapXml) throws DocumentException {
        Document document = DocumentHelper.parseText(soapXml);
        Element root = document.getRootElement();
        for (Element el : (List<Element>) root.elements()) {
            if (el.getName().toLowerCase().contains("header")) {
                return el.asXML();
            }
        }
        return null;
    }

    public static boolean checkHead(String xml) {
        if (xml != null) {
            Document document = null;
            try {
                document = DocumentHelper.parseText(xml);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Element rootElement = document.getRootElement();
            List<Element> els = rootElement.elements();
            StringBuilder sb = new StringBuilder();
            int listSize = els.size();
            String sign = "";
            for (int i = 0; i < listSize; i++) {
                if (!els.get(i).getName().equals("sign")) {
                    sb.append(els.get(i).getName() + "="
                            + els.get(i).getTextTrim());
                    if (i != listSize - 1)
                        sb.append("&");
                } else {
                    sign = els.get(i).getTextTrim();
                }
            }
            // JDK6的 base64,仅适用于JDK6,7 不建议JDK8使用
            String jdk6EncodeRes = javax.xml.bind.DatatypeConverter
                    .printBase64Binary(sb.toString().getBytes());
            // 解密String jdk6DecodeRes = new
            // String(javax.xml.bind.DatatypeConverter.parseBase64Binary(jdk6EncodeRes));
            if (sign.equals(jdk6EncodeRes)) {
                return true;
            }
        }
        return false;
    }
}
