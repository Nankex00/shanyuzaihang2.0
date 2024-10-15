package com.fushuhealth.recovery.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fushuhealth.recovery.common.constant.OrderUseWay;
import com.fushuhealth.recovery.dal.entity.ScaleOrder;
import com.fushuhealth.recovery.dal.vo.h5.CheckVideoOrderVo;
import com.fushuhealth.recovery.dal.vo.h5.ScaleOrderVo;

public interface ScaleOrderService extends IService<ScaleOrder> {

//    CheckOrderVo createScaleOrder(LoginVo user, long childrenId, CreateScaleOrderRequest request);
//
//    Object payScaleOrder(long id, long userId, String ip);
//
//    boolean notifyOrder(String xmlResult, String appId);
//
//    boolean notifyOrder(HzsHlwyyPayNotifyRequest request, String appId);
//
//    ListScaleOrderResponse listScaleOrder(long userId, int pageNo, int pageSize, byte orderType);

    ScaleOrderVo getScaleOrderVo(long id);

    void useScaleOrder(long id, long evId, long childrenId);
//
//    void cancelScaleOrder(long id);
//
//    CheckOrderVo checkPaidOrder(long userId, byte scaleCode, PaymentType paymentType, OrderType orderType, OrderUseWay useWay);
//
    CheckVideoOrderVo checkVideoOrder(long userId, String resourceId);

//    CheckVideoOrderVo createVideoOrder(LoginVo user, String resourceId, String productId);
//
//    Long saveOrder(ScaleOrder scaleOrder);
//
//    ScaleOrder getByUserIdAndScaleCodeAndStatus(long userId, byte scaleCode, byte status, byte orderType, byte useWay);
}
