package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.device.model.vo.ScaleRecordListVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListScaleRecordResponse {

    private PageVo page;

    private List<ScaleRecordListVo> records;
}
