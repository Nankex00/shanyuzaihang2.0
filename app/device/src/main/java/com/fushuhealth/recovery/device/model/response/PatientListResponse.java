package com.fushuhealth.recovery.device.model.response;

import com.fushuhealth.recovery.dal.vo.UserListVo;
import com.fushuhealth.recovery.device.model.vo.PageVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientListResponse {

    private PageVo page;

    private List<UserListVo> patients;
}
