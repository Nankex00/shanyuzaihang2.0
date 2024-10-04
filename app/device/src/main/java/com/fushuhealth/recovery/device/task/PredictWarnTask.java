package com.fushuhealth.recovery.device.task;

import com.fushuhealth.recovery.device.service.IPredictWarnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Zhuanz
 * @date 2024/10/4
 */
@Component
@Slf4j
public class PredictWarnTask {
    @Autowired
    private IPredictWarnService iPredictWarnService;

    @Scheduled(cron = "0 0 0 * * *")
    public void changeStatus(){
        iPredictWarnService.updateStatus();
        log.info("成功更新当前数据表状态");
    }
}
