package com.fushuhealth.recovery.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbnormalActionVideo {

    private String name;

    private List<VideoDto> videos;
}
