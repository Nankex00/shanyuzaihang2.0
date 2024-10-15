package com.fushuhealth.recovery.device.model.request;

import com.fushuhealth.recovery.dal.dto.FileDetailDto;
import com.fushuhealth.recovery.dal.dto.FileDto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

//import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class EvaluateRecordEditRequest {
    @NotNull
    private Long id;
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "月龄不能为空")
    private Byte monthAge;
    @NotNull(message = "geSell结果不能为空")
    private String geSellResult;
    private List<FileDto> geSellUrls;
    @NotNull(message = "s-s结果不能为空")
    private String sSResult;
    private List<FileDto> sSUrls;
    private String otherResult;
    private List<FileDto> otherUrls;
}
