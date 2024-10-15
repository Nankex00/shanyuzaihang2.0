package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.common.annonation.DataColumn;
import com.fushuhealth.recovery.common.annonation.DataPermission;
import com.fushuhealth.recovery.dal.entity.EvaluateRecord;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@DataPermission({
        @DataColumn(key = "deptName",value = "t.dept_id"),
        @DataColumn(key = "userName",value = "user_id")
})
@Mapper
public interface EvaluateRecordMapper extends MPJBaseMapper<EvaluateRecord> {
}
