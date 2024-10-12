package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fushuhealth.recovery.dal.entity.TrainingPlan;
import com.fushuhealth.recovery.dal.vo.TrainingPlanVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanDao extends BaseMapper<TrainingPlan> {

    @Select("SELECT tp.id, tp.plan_name, tp.diagnosis, tp.user_id, tp.patient_id, u.name patientName, tp.video_count," +
            "tp.time_consuming, tp.days FROM training_plan tp LEFT JOIN user u ON tp.patient_id = u.id ${ew.customSqlSegment}")
    List<TrainingPlanVo> selectTrainingPlanList(@Param(Constants.WRAPPER) Wrapper wrapper);
}