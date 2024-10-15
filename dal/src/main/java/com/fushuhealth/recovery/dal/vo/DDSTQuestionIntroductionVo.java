package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DDSTQuestionIntroductionVo {

    private String name;

    private List<String> videos;

    private List<String> pictures;

    private String introduction;
}
