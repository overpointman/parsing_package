package bean;

import lombok.Data;

/**
 * @author yuntian 317526763@qq.com
 * @version 1.0
 * @date 2019/10/18 11:27
 **/
@Data
public class resultdetail {
    private String result_detail_id;
    private String org_code;
    private String system_source;
    private String org_name;
    private String patient_id;
    private String visit_id;
    private String portfolio_code;
    private String portfolio_name;
    private String detail_item_name;
    private String detail_item_name2;
    private String checkup_result;
    private String unit;
    private String reference_range;
    private String tip;
    private String other;
}
