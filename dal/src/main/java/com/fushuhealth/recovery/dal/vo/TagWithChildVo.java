package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

@Data
public class TagWithChildVo extends TagsVo{
    private List<TagsVo> child;
}
