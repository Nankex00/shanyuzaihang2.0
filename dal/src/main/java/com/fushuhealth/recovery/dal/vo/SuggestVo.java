package com.fushuhealth.recovery.dal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestVo {

    private String content;

    private List<SuggestButtonVo> button;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuggestVo suggestVo = (SuggestVo) o;
        return content.equals(suggestVo.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
