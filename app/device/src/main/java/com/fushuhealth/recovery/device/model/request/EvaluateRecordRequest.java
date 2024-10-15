package com.fushuhealth.recovery.device.model.request;

import com.fushuhealth.recovery.dal.dto.FileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
public class EvaluateRecordRequest {
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "月龄不能为空")
    private Byte monthAge;
    @NotBlank(message = "geSell结果不能为空")
    private String geSellResult;
    private List<FileDto> geSellUrls;
    @NotBlank(message = "s-s结果不能为空")
    private String sSResult;
    private List<FileDto> sSUrls;
    private String otherResult;
    private List<FileDto> otherUrls;
}
