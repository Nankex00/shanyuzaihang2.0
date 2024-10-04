package com.fushuhealth.recovery.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fushuhealth.recovery.dal.entity.Risks;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/27
 */
public interface IRisksService extends IService<Risks> {

    String RisksExChanged(List<Long> ids);
}
