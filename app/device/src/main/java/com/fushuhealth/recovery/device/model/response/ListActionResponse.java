package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.dal.vo.ActionListVo;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListActionResponse {

    private List<ActionListVo> actions;

    private PageVo page;
}
