package com.fushuhealth.recovery.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fushuhealth.recovery.common.constant.BaseStatus;
import com.fushuhealth.recovery.dal.dao.ActionTagMapDao;
import com.fushuhealth.recovery.dal.dao.TagsDao;
import com.fushuhealth.recovery.dal.entity.ActionTagMap;
import com.fushuhealth.recovery.dal.entity.Tags;
import com.fushuhealth.recovery.dal.vo.TagWithChildVo;
import com.fushuhealth.recovery.dal.vo.TagsVo;
import com.fushuhealth.recovery.device.model.vo.LoginVo;
import com.fushuhealth.recovery.device.service.TagService;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagsDao tagsDao;

    @Autowired
    private ActionTagMapDao actionTagMapDao;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TagsVo> listTags(byte type, byte classify) {
        QueryWrapper<Tags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BaseStatus.NORMAL.getStatus());
        queryWrapper.eq("parent_id", type);
        queryWrapper.eq("classify", classify);
        List<Tags> tags = tagsDao.selectList(queryWrapper);
        return tags.stream().map(tag -> convertToVo(tag)).collect(Collectors.toList());
    }

    @Override
    public List<TagsVo> listTagsByParentId(long parentId) {
        QueryWrapper<Tags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BaseStatus.NORMAL.getStatus());
        queryWrapper.eq("parent_id", parentId);
        List<Tags> tags = tagsDao.selectList(queryWrapper);
        return tags.stream().map(tag -> convertToVo(tag)).collect(Collectors.toList());
    }

    @Override
    public List<Long> listActionIdsByTagId(Long tagId) {
        QueryWrapper<ActionTagMap> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_id", tagId);
        List<ActionTagMap> actionTagMaps = actionTagMapDao.selectList(wrapper);
        List<Long> ids = actionTagMaps.stream().map(ActionTagMap::getActionId).distinct().collect(Collectors.toList());
        return ids;
    }
//
//    @Override
//    public List<ActionTagVo> listTagVoByActionId(long actionId) {
//        return tagsDao.selectByActionId(actionId);
//    }
//
//    @Override
//    public List<Tags> listAllTags() {
//        return tagsDao.selectList(null);
//    }
//
    @Override
    public void saveActionTagMap(ActionTagMap actionTagMap) {
        actionTagMapDao.insert(actionTagMap);
    }

    @Override
    public List<Tags> getTagByActionId(Long actionId) {
        QueryWrapper<ActionTagMap> wrapper = new QueryWrapper<>();
        wrapper.eq("action_id", actionId);
        ActionTagMap actionTagMap = actionTagMapDao.selectOne(wrapper);
        Tags secondTags = tagsDao.selectById(actionTagMap.getTagId());
        Tags firstTags = tagsDao.selectById(secondTags.getParentId());
        ArrayList<Tags> tags = new ArrayList<>();
        tags.add(secondTags);
        tags.add(firstTags);
        return tags;
    }

    @Override
    public List<TagWithChildVo> listAllTagVo(LoginVo loginVo) {
        QueryWrapper<Tags> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", BaseStatus.NORMAL.getStatus()).eq("parent_id", 0);
        List<Tags> tags = tagsDao.selectList(queryWrapper);
        ArrayList<TagWithChildVo> tagsVos = new ArrayList<>();
        for (Tags tag : tags) {

            QueryWrapper<ActionTagMap> actionTagMapQueryWrapper = new QueryWrapper<>();
            actionTagMapQueryWrapper.in("action_id", loginVo.getActionIds());
            List<ActionTagMap> actionTagMaps = actionTagMapDao.selectList(actionTagMapQueryWrapper);

            if (CollectionUtils.isNotEmpty(actionTagMaps)) {

                List<Long> tagIds = actionTagMaps.stream().map(ActionTagMap::getTagId).collect(Collectors.toList());

                List tagIdsAfterDistinct = removeDuplicationByHashSet(tagIds);

                QueryWrapper<Tags> wrapper = new QueryWrapper<>();
                wrapper.eq("status", BaseStatus.NORMAL.getStatus());
                wrapper.eq("parent_id", tag.getId());
                wrapper.in("id", tagIdsAfterDistinct);
                List<Tags> children = tagsDao.selectList(wrapper);

                if (CollectionUtils.isNotEmpty(children)) {
                    ArrayList<TagsVo> childrenList = new ArrayList<>();
                    for (Tags child : children) {
                        TagsVo vo = convertToVo(child);
                        childrenList.add(vo);
                    }
                    TagWithChildVo tagsVo = convertToTagWithChildVo(tag);
                    tagsVo.setChild(childrenList);
                    tagsVos.add(tagsVo);
                }
            }
        }
        return tagsVos;
    }

//    @Override
//    public Tags getById(long id) {
//        return tagsDao.selectById(id);
//    }
//
    private  List removeDuplicationByHashSet(List<Long> list) {
        HashSet set = new HashSet(list);
        //把List集合所有元素清空
        list.clear();
        //把HashSet对象添加至List集合
        list.addAll(set);
        return list;
    }


    private TagsVo convertToVo(Tags tags) {
        return modelMapper.map(tags, TagsVo.class);
    }

    private TagWithChildVo convertToTagWithChildVo(Tags tags) {
        TagWithChildVo vo = new TagWithChildVo();
        vo.setId(tags.getId());
        vo.setName(tags.getName());
        return vo;
    }
}
