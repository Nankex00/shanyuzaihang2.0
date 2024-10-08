package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.SysRoleDept;
import com.fushuhealth.recovery.device.model.bo.SysDeptBo;
import com.fushuhealth.recovery.device.model.request.InstitutionRequest;
import com.fushuhealth.recovery.device.model.request.MyDeptRequest;
import com.fushuhealth.recovery.common.constant.Dict;
import com.fushuhealth.recovery.device.model.response.InstitutionResponse;
import com.fushuhealth.recovery.device.model.response.SysDeptResponse;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
public interface ISysDeptService {

    BaseResponse<List<SysDeptResponse>> list(InstitutionRequest request);

    int createDept(SysDeptBo bo);

    int deleteDept(Long userId);

    int updateDept(SysDeptBo bo);

    InstitutionResponse searchDetail(Long id);

    String selectDeptNameById(Long institutionId);

    BaseResponse<List<Dict>> searchAncestorsDeptByDeptId(Long userId);

    Long selectDeptLevelByDeptId(Long deptId);

    int editMyInstitution(MyDeptRequest bo);

    Long getRoleId(Long deptId);
}
