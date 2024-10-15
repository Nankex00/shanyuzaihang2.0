package com.fushuhealth.recovery.device.model.request;

import com.fushuhealth.recovery.dal.dto.FileDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//import javax.validation.constraints.NotNull;

/**
 * @author Zhuanz
 * @date 2024/9/30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepeatFiltrateRecordRequest {
    @NotNull(message = "儿童id不能为空")
    private Long childId;
    @NotNull(message = "月龄不能为空")
    private Byte monthAge;
    @NotNull(message = "AQS结果不能为空")
    private String aqsResult;
    private List<FileDto> aqs;
    @NotNull(message = "DDST结果不能为空")
    private String ddstResult;
    private List<FileDto> ddst;
    private String otherResult;
    private List<FileDto> other;
}
