package com.fushuhealth.recovery.dal.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fushuhealth.recovery.dal.entity.Tags;
import com.fushuhealth.recovery.dal.vo.ActionTagVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsDao extends BaseMapper<Tags> {

    @Select("SELECT b.name AS tagKey, GROUP_CONCAT(a.name SEPARATOR '|') AS tagValue FROM  tags b LEFT JOIN " +
            "(SELECT id, parent_id, name FROM tags) a ON b.id = a.parent_id " +
            "LEFT JOIN action_tag_map atm ON atm.tag_id = a.id WHERE atm.action_id=#{actionId} " +
            "GROUP BY b.id ORDER BY b.id")
    List<ActionTagVo> selectByActionId(Long actionId);
}