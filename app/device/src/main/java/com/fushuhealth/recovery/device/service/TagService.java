package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.entity.ActionTagMap;
import com.fushuhealth.recovery.dal.entity.Tags;
import com.fushuhealth.recovery.dal.vo.TagWithChildVo;
import com.fushuhealth.recovery.dal.vo.TagsVo;
import com.fushuhealth.recovery.device.model.vo.LoginVo;

import java.util.List;

public interface TagService {

    List<TagsVo> listTags(byte type, byte classify);

    List<TagsVo> listTagsByParentId(long parentId);

    List<Long> listActionIdsByTagId(Long tagId);

//    List<ActionTagVo> listTagVoByActionId(long actionId);
//
//    List<Tags> listAllTags();
//
    void saveActionTagMap(ActionTagMap actionTagMap);

    List<Tags> getTagByActionId(Long actionId);

    List<TagWithChildVo> listAllTagVo(LoginVo loginVo);

//    Tags getById(long id);
}
