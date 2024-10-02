package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.constant.EnumInfo;
import com.fushuhealth.recovery.common.constant.MonthType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@RestController("/enum")
public class EnumController {
    @GetMapping("/monthAgeType")
    public AjaxResult monthAgeType(){
        return AjaxResult.success(new BaseResponse<List<EnumInfo>>(MonthType.generateMonthList(),null));
    }
}
