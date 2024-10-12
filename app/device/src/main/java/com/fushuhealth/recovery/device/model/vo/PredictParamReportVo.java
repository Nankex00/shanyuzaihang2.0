package com.fushuhealth.recovery.device.model.vo;

import com.fushuhealth.recovery.dal.vo.AttachmentVo;
import com.fushuhealth.recovery.dal.vo.PredictParamReportVideoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictParamReportVo {
    private Long id;
    private String aiAnswer;
    private String remark;
    private String predictWarnQuestion;
    private List<AttachmentVo> videoVos;

}
