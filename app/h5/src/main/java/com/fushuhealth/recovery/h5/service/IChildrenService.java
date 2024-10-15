package com.fushuhealth.recovery.h5.service;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.dal.entity.Children;
import com.fushuhealth.recovery.dal.vo.ChildrenVo;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/26
 */
public interface IChildrenService {
//    BaseResponse<List<ChildrenResponse>> searchList(ChildrenRequest request);
//
//    ChildrenDetail searchDetail(Long id);
//
//    BaseResponse<List<ChildrenResponse>> searchListHighRisk(HighRiskChildrenRequest request);

    Children getChildrenById(long id);

    ChildrenVo getChildren(Long id);

}
