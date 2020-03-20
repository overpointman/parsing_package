/**
 *
 */
package utils;

import com.jyyh.dsb.xml.core.DefaultXmlConverter;
import com.jyyh.dsb.xml.core.XmlConverter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.xml.xpath.XPathExpression;
import org.springframework.xml.xpath.XPathExpressionFactory;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * @author jiafeng
 * <p>
 * xpath 工具类
 */
public class XpathUtil {
    private static XPathExpression xPathExpression;
    /**
     * XmlConverter默认实现
     **/
    private static XmlConverter xmlConverter = new DefaultXmlConverter();

    private XpathUtil() {

    }

    /**
     * 获取节点值
     *
     * @param namespace
     *            格式："ns1:www.example.org,ns2:www.example2.org"
     * @param expression
     * @param xml
     * @return
     */
    public static String getNodeValue(String namespace, String expression,
                                      String xml) {
        if (namespace == null) {
            xPathExpression = XPathExpressionFactory
                    .createXPathExpression(expression);
        } else {
            Map<String, String> namespaceMap = new HashMap<String, String>(16);
            StringTokenizer tokenizer = new StringTokenizer(namespace, ",");
            while (tokenizer.hasMoreElements()) {
                String[] str = tokenizer.nextToken().split(":");
                namespaceMap.put(str[0], str[1]);
            }
            if (!namespaceMap.isEmpty()) {
                xPathExpression = XPathExpressionFactory.createXPathExpression(
                        expression, namespaceMap);
            }
        }
        // 获取节点
        Node node = xmlConverter.convertToNode(xml);
        return xPathExpression.evaluateAsString(node);
    }

    public static void main(String[] args) throws XPathExpressionException, DocumentException {
        String xml = "<reqxml>    <head></head>    <body>        <data>            <request>                <visit_id>test</visit_id>                <org_code>test</org_code>                <system_source>test</system_source>                <org_name>test</org_name>                <age>1.0</age>                <patient_id>test</patient_id>                <checkup_type_code>2</checkup_type_code>                <patient_name>test</patient_name>                <gender_code>1</gender_code>                <age_unit>test</age_unit>                <birthday>2019-09-10 16:40:25</birthday>                <marriage_code>1</marriage_code>                <occupation_name>test</occupation_name>                <occupation_type_code>1</occupation_type_code>                <habit_work_exercies>test</habit_work_exercies>                <habit_sleep>test</habit_sleep>                <habit_diet>test</habit_diet>                <habit_other>test</habit_other>                <diseases_history>test</diseases_history>                <operation_history>test</operation_history>                <rtum_history>test</rtum_history>                <trauma_history>test</trauma_history>                <menophania>test</menophania>                <menstrual_cycle></menstrual_cycle>                <leucorrhea>test</leucorrhea>                <pausimenia>test</pausimenia>                <abortion>test</abortion>                <parturition_history>test</parturition_history>                <family_history>test</family_history>                <allergy_history>test</allergy_history>                <symptom_code>1</symptom_code>                <duty_physician_name>test</duty_physician_name>                <health_summary>test</health_summary>                <health_suggest>test</health_suggest>                <all_checkup_date>2019-09-10 16:40:48</all_checkup_date>                <all_checkup_doc_code></all_checkup_doc_code>                <all_checkup_doc_name>test</all_checkup_doc_name>                <checkup_type>1</checkup_type>                <team></team>                <team_checkup_time></team_checkup_time>                <team_appointment_no></team_appointment_no>                <checkup_date>2019-09-10 16:40:54</checkup_date>                <checkup_register_date>2019-09-10 16:40:58</checkup_register_date>                <checkup_register_dept>test</checkup_register_dept>                <checkup_package_code>test</checkup_package_code>                <checkup_package>test</checkup_package>                <checkup_report_no>test</checkup_report_no>                <checkup_report_date>2019-09-10 16:41:03</checkup_report_date>                <reporter_code>test</reporter_code>                <reporter_name>test</reporter_name>                <exception_result></exception_result>            </request>            <request_details>                <detail>                    <visit_id>test</visit_id>                    <item_detail_id>test</item_detail_id>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <checkup_dept_code>test</checkup_dept_code>                    <checkup_dept_name>test</checkup_dept_name>                    <item_type_code></item_type_code>                    <item_type_name></item_type_name>                    <portfolio_code>test</portfolio_code>                    <portfolio_name>test</portfolio_name>                    <checkup_date>2019-09-10 16:47:57</checkup_date>                    <checkup_person_code>test</checkup_person_code>                    <checkup_person_name>test</checkup_person_name>                    <item_summary></item_summary>                </detail>            </request_details>            <request_results>                <resultdetail>                    <result_detail_id>test</result_detail_id>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <visit_id>test</visit_id>                    <portfolio_code>test</portfolio_code>                    <portfolio_name>test</portfolio_name>                    <detail_item_name>test</detail_item_name>                    <detail_item_name2>test</detail_item_name2>                    <checkup_result>test</checkup_result>                    <unit></unit>                    <reference_range></reference_range>                    <tip></tip>                    <other></other>                </resultdetail>                <resultdetail>                    <result_detail_id>test</result_detail_id>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <visit_id>test</visit_id>                    <portfolio_code>test</portfolio_code>                    <portfolio_name>test</portfolio_name>                    <detail_item_name>test</detail_item_name>                    <detail_item_name2>test</detail_item_name2>                    <checkup_result>test</checkup_result>                    <unit></unit>                    <reference_range></reference_range>                    <tip></tip>                    <other></other>                </resultdetail>            </request_results>            <request_deptreports>                <deptreport>                    <visit_id>test</visit_id>                    <dept_report_no>test</dept_report_no>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <dept_code></dept_code>                    <dept_name></dept_name>                    <summary></summary>                    <diag></diag>                    <prevention_suggest></prevention_suggest>                    <record_date></record_date>                    <doc_code></doc_code>                    <doc_name></doc_name>                    <operator></operator>                    <operator_name></operator_name>                    <report_physician_code></report_physician_code>                    <report_physician_name></report_physician_name>                    <remark></remark>                </deptreport>                <deptreport>                    <visit_id>test</visit_id>                    <dept_report_no>test</dept_report_no>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <dept_code></dept_code>                    <dept_name></dept_name>                    <summary></summary>                    <diag></diag>                    <prevention_suggest></prevention_suggest>                    <record_date></record_date>                    <doc_code></doc_code>                    <doc_name></doc_name>                    <operator></operator>                    <operator_name></operator_name>                    <report_physician_code></report_physician_code>                    <report_physician_name></report_physician_name>                    <remark></remark>                </deptreport>                <deptreport>                    <visit_id>test</visit_id>                    <dept_report_no>test</dept_report_no>                    <org_code>test</org_code>                    <system_source>test</system_source>                    <org_name>test</org_name>                    <patient_id>test</patient_id>                    <dept_code></dept_code>                    <dept_name></dept_name>                    <summary></summary>                    <diag></diag>                    <prevention_suggest></prevention_suggest>                    <record_date></record_date>                    <doc_code></doc_code>                    <doc_name></doc_name>                    <operator></operator>                    <operator_name></operator_name>                    <report_physician_code></report_physician_code>                    <report_physician_name></report_physician_name>                    <remark></remark>                </deptreport>            </request_deptreports>        </data>    </body></reqxml>";
        XPath xpath = XPathFactory.newInstance().newXPath();
        Document doc = DocumentHelper.parseText(xml);
        Object obj = xpath.compile("/reqxml/body/data").evaluate(doc, XPathConstants.NODESET);

        System.out.println();
    }
}
