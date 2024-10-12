package com.fushuhealth.recovery.device.service;

import com.fushuhealth.recovery.dal.entity.Tags;

import java.util.List;

public interface TagService {

//    List<TagsVo> listTags(byte type, byte classify);
//
//    List<TagsVo> listTagsByParentId(long parentId);
//
//    List<Long> listActionIdsByTagId(Long tagId);
//
//    List<ActionTagVo> listTagVoByActionId(long actionId);
//
//    List<Tags> listAllTags();
//
//    void saveActionTagMap(ActionTagMap actionTagMap);

    List<Tags> getTagByActionId(Long actionId);

//    List<TagWithChildVo> listAllTagVo(LoginVo loginVo);
//
//    Tags getById(long id);
}
