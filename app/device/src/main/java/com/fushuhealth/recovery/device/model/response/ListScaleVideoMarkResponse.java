package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.device.model.vo.PageVo;
import com.fushuhealth.recovery.web.model.vo.ScaleVideoMarkListVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListScaleVideoMarkResponse {

    private PageVo page;

    private List<ScaleVideoMarkListVo> marks;
}
