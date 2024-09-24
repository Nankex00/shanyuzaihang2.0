package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.AjaxResult;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.response.InstitutionResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
public interface ISysDeptService {

    List<SysDept> list(InstitutionRequest request);

    int createDept(SysDeptBo bo);

    int deleteDept(Long id,Long userId);

    int updateDept(SysDeptBo bo);

    InstitutionResponse searchDetail(Long id);
}
