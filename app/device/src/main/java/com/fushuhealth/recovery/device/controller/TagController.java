package com.fushuhealth.recovery.device.controller;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.api.OldBaseResponse;
import com.fushuhealth.recovery.dal.vo.TagWithChildVo;
import com.fushuhealth.recovery.dal.vo.TagsVo;
import com.fushuhealth.recovery.device.model.vo.LoginVo;
import com.fushuhealth.recovery.device.service.ISysUserService;
import com.fushuhealth.recovery.device.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private ISysUserService userService;

    @Deprecated
    @GetMapping("/bodyPart")
    public OldBaseResponse listBodyPart() {
        List<TagsVo> bodyParts = tagService.listTags((byte) 1, (byte) 1);
        return OldBaseResponse.success(bodyParts);
    }

    @Deprecated
    @GetMapping("/trainingType")
    public OldBaseResponse listTrainingType() {
        List<TagsVo> trainingTypes = tagService.listTags((byte) 5, (byte) 1);
        return OldBaseResponse.success(trainingTypes);
    }

    @GetMapping("/list")
    public OldBaseResponse listTrainingType(@RequestParam(defaultValue = "0") Integer id) {
        List<TagsVo> trainingTypes = tagService.listTagsByParentId(id);
        return OldBaseResponse.success(trainingTypes);
    }

    @GetMapping("/all")
    public OldBaseResponse listAllTags() {
        LoginVo user = userService.getLoginVo();
        List<TagWithChildVo> tagWithChildVos = tagService.listAllTagVo(user);
        return OldBaseResponse.success(tagWithChildVos);
    }

    @GetMapping("/videos")
    public OldBaseResponse listVideoLibraryTags() {
        List<TagsVo> bodyParts = tagService.listTags((byte) 0, (byte) 2);
        return OldBaseResponse.success(bodyParts);
    }
}
