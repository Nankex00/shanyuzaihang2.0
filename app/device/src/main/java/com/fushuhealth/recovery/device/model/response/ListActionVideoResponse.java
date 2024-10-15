package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.dal.vo.ActionVideoListVo;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListActionVideoResponse {

    private PageVo page;

    private List<ActionVideoListVo> videos;
}
