package com.fushuhealth.recovery.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long operatedId;
    private Date operatedTime;
    private List<Long> beforeDiagnose;
    private List<Long> diagnoseDetail;
    private Long childId;
}
