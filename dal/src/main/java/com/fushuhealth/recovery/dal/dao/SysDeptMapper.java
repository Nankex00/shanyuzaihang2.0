package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.annonation.DataColumn;
import com.fushuhealth.recovery.common.annonation.DataPermission;
import com.fushuhealth.recovery.dal.entity.SysDept;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author Zhuanz
 * @date 2024/9/24
 */
//@DataPermission({
//        @DataColumn(key = "deptName",value = "t.dept_id"),
//        @DataColumn(key = "userName",value = "user_id")
//})
public interface SysDeptMapper extends MPJBaseMapper<SysDept> {

}
