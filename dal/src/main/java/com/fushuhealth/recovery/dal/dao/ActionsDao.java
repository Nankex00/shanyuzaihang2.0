package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.annonation.DataColumn;
import com.fushuhealth.recovery.common.annonation.DataPermission;
import com.fushuhealth.recovery.dal.entity.Actions;
import org.springframework.stereotype.Repository;

@Repository
@DataPermission({
        @DataColumn(key = "deptName",value = "t.dept_id"),
        @DataColumn(key = "userName",value = "user_id")
})
public interface ActionsDao extends BaseMapper<Actions> {
}
