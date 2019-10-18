package bean;

import lombok.Data;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/18 11:28
 **/
@Data
public class deptreport {
    private String visit_id;
    private String dept_report_no;
    private String org_code;
    private String system_source;
    private String org_name;
    private String patient_id;
    private String dept_code;
    private String dept_name;
    private String summary;
    private String diag;
    private String prevention_suggest;
    private String record_date;
    private String doc_code;
    private String doc_name;
    private String operator;
    private String operator_name;
    private String report_physician_code;
    private String report_physician_name;
    private String remark;
}
