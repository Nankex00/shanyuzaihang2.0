package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.dal.entity.PredictWarn;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Mapper
public interface PredictWarnMapper extends MPJBaseMapper<PredictWarn> {
    @Update("UPDATE predict_warn" +
            "    SET warn_status = " +
            "        CASE" +
            "            WHEN #{currentTime} < warn_start AND warn_status != 2 THEN 4" +
            "            WHEN #{currentTime} >= warn_start AND #{currentTime} <= warn_end AND warn_status != 2 THEN 3" +
            "            WHEN #{currentTime} > warn_end AND warn_status != 2 THEN 1\n" +
            "            ELSE warn_status" +
            "        END")
    int updateWarnStatusByTime(@Param("currentTime") LocalDateTime currentTime);
}
