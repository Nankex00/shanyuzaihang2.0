//package com.fushuhealth.recovery.device.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.fushuhealth.recovery.common.api.ResultCode;
//import com.fushuhealth.recovery.common.constant.*;
//import com.fushuhealth.recovery.common.core.domin.SysUser;
//import com.fushuhealth.recovery.common.exception.OldServiceException;
//import com.fushuhealth.recovery.common.util.DateUtil;
//import com.fushuhealth.recovery.common.util.StringUtil;
//import com.fushuhealth.recovery.dal.dao.ScaleOrderDao;
//import com.fushuhealth.recovery.dal.entity.ScaleOrder;
//import com.fushuhealth.recovery.dal.entity.ScaleTable;
//import com.fushuhealth.recovery.dal.vo.h5.CheckVideoOrderVo;
//import com.fushuhealth.recovery.dal.vo.h5.ScaleOrderVo;
//import com.fushuhealth.recovery.device.h5.service.ScaleEvaluationRecordService;
//import com.fushuhealth.recovery.device.h5.service.ScaleTableService;
//import com.fushuhealth.recovery.device.service.ISysUserService;
//import com.fushuhealth.recovery.device.service.ScaleOrderService;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.beans.Transient;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ScaleOrderServiceImpl extends ServiceImpl<ScaleOrderDao, ScaleOrder> implements ScaleOrderService {
//
//    private final static Logger log = LoggerFactory.getLogger(ScaleOrderServiceImpl.class);
//
//    @Autowired
//    private ScaleOrderDao scaleOrderDao;
//
//    @Autowired
//    private ScaleEvaluationRecordService scaleEvaluationRecordService;
//
////    @Autowired
////    private ChildrenService childrenService;
//
//    @Autowired
//    private ScaleTableService scaleTableService;
//
////    @Autowired
////    private WxPayService wxPayService;
////
////    @Autowired
////    private HzsHlwyyPay hzsHlwyyPay;
////
////    @Autowired
////    private WxUserService wxUserService;
//
////    @Autowired
////    private XiaoeService xiaoeService;
//
//    @Autowired
//    private ISysUserService userService;
////
////    @Autowired
////    private ReserveService reserveService;
////
////    @Autowired
////    private OrganizationService organizationService;
//
////    @Value("${wx.pay.notifyUrl}")
////    private String wxPayNotifyUrl;
//
//    @Value("${spring.profiles.active}")
//    private String env;
//
////    @Value("${sms.code.pay-success-template-id}")
////    private String paySuccessTemplateId;
////
////    @Value("${sms.code.pay-success-admin-notify-template-id}")
////    private String paySuccessAdminNotifyTemplateId;
////
////    @Value("${sms.code.pay-success-admin-template-id}")
////    private String paySuccessAdminTemplateId;
//
////    @Autowired
////    private SmsSender smsSender;
////
////    @Autowired
////    private ScaleTablePriceService scaleTablePriceService;
//
////    @Override
////    public CheckOrderVo createScaleOrder(LoginVo user, long childrenId, CreateScaleOrderRequest request) {
////
////        //1.检查儿童是否符合年龄
//////        Children children = childrenService.getChildren(childrenId);
//////        List<Long> monthBetween = DateUtil.getMonthBetween(children.getBirthday(), DateUtil.getCurrentTimeStamp());
//////        int ageOfMonth = monthBetween.size();
//////        if (ageOfMonth > 5) {
//////            throw new ServiceException(ResultCode.SCALE_AGE_MORE_THAN_5_MONTH);
//////        }
////        //2.检查该用户账号下的儿童有没有在 7 天内做过脑瘫评测
//////        long time = DateUtil.getCurrentTimeStamp() - (7 * 24 * 60 * 60);
//////        int count = scaleEvaluationRecordService
//////                .countByUserIdAndScaleCodeAndChildrenIdAndTime(userId, ScaleTableConstant.ScaleTableCode.LEIBO_CEREBRAL_PALSY.getCode(), childrenId, time);
//////        if (count == 0) {
//////            throw new ServiceException(ResultCode.SCALE_NO_CEREBRAL_PALSY_IN_7_DAYS);
//////        }
////
////        ScaleTablePrice scaleTablePrice = scaleTablePriceService.getById(request.getPriceId());
////        if (scaleTablePrice == null) {
////            throw new ServiceException(ResultCode.PARAM_ERROR);
////        }
////        String title = "订单";
////        OrderType orderType = OrderType.SCALE_TABLE_ORDER;
////        WorkScheduleType type = WorkScheduleType.getType(scaleTablePrice.getType());
////        if (type == WorkScheduleType.AI_EVALUATE) {
////            ScaleTable scaleTable = scaleTableService.getScaleTable(request.getScaleTableCode());
////            if (scaleTable == null) {
////                throw new ServiceException(ResultCode.PARAM_ERROR);
////            }
////            title = scaleTable.getName();
////            orderType = OrderType.SCALE_TABLE_ORDER;
////        } else if (type == WorkScheduleType.CLINIC_EVALUATE) {
////            title = "门诊评估";
////            orderType = OrderType.CLINIC_EVALUATE;
////        } else if (type == WorkScheduleType.VIDEO_GUIDE) {
////            title = "视频指导";
////            orderType = OrderType.VIDEO_GUIDE;
////        }
////
////        CheckOrderVo vo = new CheckOrderVo();
////
////        OrderUseWay orderUseWay = OrderUseWay.getOrderUseWay(request.getUseWay());
////
////        //检查该用户账号下有没有未付款的订单，如果有，返回此订单，没有的话新建
////        ScaleOrder scaleOrder = getByUserIdAndScaleCodeAndStatus(user.getId(),
////                request.getScaleTableCode(), ScaleOrderStatus.WAIT_PAY.getCode(),
////                orderType.getCode(), orderUseWay.getCode());
////
////        if (scaleOrder != null) {
////            scaleOrder.setOrderStatus(ScaleOrderStatus.CANCELED.getCode());
////            scaleOrderDao.updateById(scaleOrder);
////        }
////
////
////        //4.创建订单
////        scaleOrder = new ScaleOrder();
////        scaleOrder.setChildrenId(0l);
////        scaleOrder.setCreated(DateUtil.getCurrentTimeStamp());
////        scaleOrder.setOrderNo(StringUtil.getOrderNo());
////        scaleOrder.setPaidFee(0);
////        scaleOrder.setPayChannel("");
////        scaleOrder.setPayNo("");
////        scaleOrder.setPayTime(0l);
////        scaleOrder.setRefundable(0);
////        scaleOrder.setRefunded(0);
////        scaleOrder.setScaleEvaluationRecordId("");
////        scaleOrder.setScaleTableCode(request.getScaleTableCode());
////        scaleOrder.setStatus(BaseStatus.NORMAL.getStatus());
//////            Integer price = scaleTableService.getPrice(scaleCode);
////        scaleOrder.setTotalFee(scaleTablePrice.getSalePrice());
////        scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
////        scaleOrder.setUserId(user.getId());
////        scaleOrder.setOrderType(orderType.getCode());
////        scaleOrder.setTitle(title);
////        scaleOrder.setProductId("");
////        scaleOrder.setResourceId("");
////        scaleOrder.setXiaoeOrderId("");
////        scaleOrder.setAvailableTimes(scaleTablePrice.getAvailableTimes());
////        scaleOrder.setTotalTimes(scaleTablePrice.getAvailableTimes());
////        scaleOrder.setPayment(PaymentType.ON_LINE.getCode());
////        scaleOrder.setInvoiceFileId("");
////        scaleOrder.setUsedTimes(0);
////        scaleOrder.setReason("");
////        scaleOrder.setOrganizationId(user.getOrganizationId());
////        scaleOrder.setAppId(AuthContext.getAppId());
////        scaleOrder.setChannel(AuthContext.getChannel());
////        scaleOrder.setOrgId(AuthContext.getOrgId());
////        scaleOrder.setUseWay(orderUseWay.getCode());
////        if (scaleOrder.getTotalFee() == 0) {
////            scaleOrder.setOrderStatus(ScaleOrderStatus.PAID.getCode());
////            vo.setHasPaidOrder(true);
////        } else {
////            scaleOrder.setOrderStatus(ScaleOrderStatus.WAIT_PAY.getCode());
////            vo.setHasPaidOrder(false);
////        }
////        scaleOrderDao.insert(scaleOrder);
////        vo.setOrderId(scaleOrder.getId());
////        vo.setOrderStatus(scaleOrder.getOrderStatus());
////        vo.setOrderStatusStr(ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus()).getDesc());
////        return vo;
////    }
////
////    @Override
////    public Object payScaleOrder(long id, long userId, String ip) {
////
////        WxUser wxUser = wxUserService.getByUserId(userId, PlatformEnum.WX_MINI_APP);
////        if (wxUser == null) {
////            throw new ServiceException(ResultCode.STATUS_ERROR);
////        }
////        ScaleOrder scaleOrder = scaleOrderDao.selectById(id);
////        if (scaleOrder == null) {
////            throw new ServiceException(ResultCode.PARAM_ERROR);
////        }
////        if (scaleOrder.getOrderStatus() != ScaleOrderStatus.WAIT_PAY.getCode()) {
////            throw new ServiceException(ResultCode.SCALE_ORDER_STATUS_ERROR);
////        }
//////        ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
//////        if (scaleTable == null) {
//////            throw new ServiceException(ResultCode.PARAM_ERROR);
//////        }
////
////        try {
////
////            if (AuthContext.getAppId().equals(AppConstant.ERTONGSHENJINGFAYU)) {
////                String cashier = hzsHlwyyPay.pay(scaleOrder);
////                return cashier;
////            } else {
////                WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
////
////                orderRequest.setBody(scaleOrder.getTitle());
////                orderRequest.setOutTradeNo(scaleOrder.getOrderNo() + StringUtil.getRandomNumber(4));
////                orderRequest.setTotalFee(scaleOrder.getTotalFee());  //单位分
////                orderRequest.setOpenid(wxUser.getOpenId());
////                orderRequest.setSpbillCreateIp(ip);
////
////                String notifyPath = "/fushu/notify";
////                if (AuthContext.getAppId().equals(AppConstant.LEIBO)) {
////                    notifyPath = "/leibo/notify";
////                } else if (AuthContext.getAppId().equals(AppConstant.YINUOYUNJIAN)) {
////                    notifyPath = "/yinuoyunjian/notify";
////                } else if (AuthContext.getAppId().equals(AppConstant.YUJINGPING)) {
////                    notifyPath = "/yujingping/notify";
////                } else if (AuthContext.getAppId().equals(AppConstant.TEYANGXINXI)) {
////                    notifyPath = "/teyangxinxi/notify";
////                }
////                orderRequest.setNotifyUrl(wxPayNotifyUrl + notifyPath);
////                orderRequest.setTradeType("JSAPI");
////
////                wxPayService.switchover(AuthContext.getAppId());
////                WxPayMpOrderResult payResult = wxPayService.createOrder(orderRequest);
////
////                WxUnifiedOrderVO wxUnifiedOrderVO = new WxUnifiedOrderVO();
////                wxUnifiedOrderVO.setAppId(payResult.getAppId());
////                wxUnifiedOrderVO.setNonceStr(payResult.getNonceStr());
////                wxUnifiedOrderVO.setPackageValue(payResult.getPackageValue());
////                wxUnifiedOrderVO.setPaySign(payResult.getPaySign());
////                wxUnifiedOrderVO.setSignType(payResult.getSignType());
////                wxUnifiedOrderVO.setTimeStamp(payResult.getTimeStamp());
////                wxUnifiedOrderVO.setOrderId(scaleOrder.getId());
////                wxUnifiedOrderVO.setPrice(BaseWxPayResult.fenToYuan(scaleOrder.getTotalFee()));
////
////                return wxUnifiedOrderVO;
////            }
////        } catch (WxPayException e) {
////            log.error("微信支付失败！订单号：{},原因:{}", scaleOrder.getOrderNo(), e.getMessage());
////            throw new ServiceException(ResultCode.WX_PAY_ERROR);
////        }
////    }
////
////    @Transactional
////    @Override
////    public boolean notifyOrder(String xmlResult, String appId) {
////
////        try {
////            log.debug("微信通知文本:{}", xmlResult);
////            wxPayService.switchover(appId);
////            WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlResult);
////            String orderNo = result.getOutTradeNo().substring(0, 22);
////            log.info("微信通知订单号:{}", orderNo);
////            ScaleOrder scaleOrder = getByOrderNo(orderNo);
////            if (scaleOrder == null) {
////                log.info("订单号:{} 不存在", orderNo);
////                return false;
////            }
////            log.info("订单号:{} 订单:{}", orderNo, JSON.toJSONString(scaleOrder));
////            if (ScaleOrderStatus.PAID.getCode() == scaleOrder.getOrderStatus()
////                    || ScaleOrderStatus.USED.getCode() == scaleOrder.getOrderStatus()) {
////                return true;
////            }
////            int totalFee = result.getTotalFee();
////            scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
////            scaleOrder.setRefundable(totalFee);
////            scaleOrder.setPayTime(DateUtil.getCurrentTimeStamp());
////            scaleOrder.setPayNo(result.getTransactionId());
////            scaleOrder.setPayChannel("WXPAY");
////            scaleOrder.setPaidFee(totalFee);
////            scaleOrder.setOrderStatus(ScaleOrderStatus.PAID.getCode());
////
////            long reserveId = 0;
////            OrderType orderType = OrderType.getOrderType(scaleOrder.getOrderType());
////            if (orderType == OrderType.VIDEO_ORDER) {
////                User user = userService.getUser(scaleOrder.getUserId());
////                String resourceId = "";
////                if (StringUtils.isNotBlank(scaleOrder.getResourceId())) {
////                    List<String> strings = Arrays.asList(scaleOrder.getResourceId().split(","));
////                    if (CollectionUtils.isNotEmpty(strings)) {
////                        resourceId = strings.get(0);
////                    }
////                }
//////                CreateOrderData data = xiaoeService.createOrder(user.getXiaoeUserId(), scaleOrder.getProductId(), resourceId, scaleOrder.getOrderNo());
//////                scaleOrder.setXiaoeOrderId(data.getOrderId());
////            } else if (orderType == OrderType.VIDEO_GUIDE || orderType == OrderType.CLINIC_EVALUATE || orderType == OrderType.SCALE_TABLE_ORDER) {
////                String productId = scaleOrder.getResourceId();
////
////                if (StringUtils.isNotBlank(productId)) {
////                    reserveId = Long.parseLong(productId);
////                    //修改预约状态
////                    reserveService.updateReserve(reserveId, ReserveStatus.WAITING_REVIEW);
////                }
////            }
////            scaleOrderDao.updateById(scaleOrder);
////
////            User user = userService.getUser(scaleOrder.getUserId());
////            if (orderType == OrderType.SCALE_TABLE_ORDER) {
////                try {
////                    //给用户发送短信通知
////                    HashMap<String, String> userMap = new HashMap<>();
////                    ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
////                    if (scaleTable != null) {
////                        userMap.put("name", scaleTable.getName());
////                        smsSender.send(paySuccessTemplateId, user.getPhone(), userMap);
////                    }
////                } catch (Exception e) {
////                    log.error("给{}发送支付成功通知短信失败:{}", user.getPhone(), e);
////                }
////
////                try {
////                    //给用户发送短信通知
////                    HashMap<String, String> map = new HashMap<>();
////                    ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
////                    OrganizationVo organizationVo = organizationService.getOrganizationVo(scaleOrder.getOrganizationId());
////                    if (scaleTable != null) {
////                        for (String phone : organizationVo.getSmsPhone()) {
////                            map.put("phone", user.getPhone().substring(7, 11));
////                            map.put("name", scaleTable.getName());
////                            map.put("time", DateUtil.getYMDHMSDate(DateUtil.getCurrentTimeStamp()));
////                            map.put("organization", organizationVo.getName());
////                            smsSender.send(paySuccessAdminNotifyTemplateId, phone, map);
////                        }
////                    }
////                } catch (Exception e) {
////                    log.error("给{}发送支付成功通知短信失败:{}", user.getPhone(), e);
////                }
////            }
////
////            //门诊评估给管理员发送短信
////            if (orderType == OrderType.CLINIC_EVALUATE) {
////                try {
////                    String reserveTime = DateUtil.getYMDHMSDate(DateUtil.getCurrentTimeStamp());
////                    OrganizationVo organizationVo = organizationService.getOrganizationVo(scaleOrder.getOrganizationId());
////                    if (reserveId != 0) {
////                        Reserve reserve = reserveService.getReserve(reserveId);
////                        reserveTime = DateUtil.getYMDHMDate(reserve.getReserveStartTime());
////                    }
////
////                    for (String phone : organizationVo.getSmsPhone()) {
////                        HashMap<String, String> adminMap = new HashMap<>();
////                        adminMap.put("phone", user.getPhone().substring(7, 11));
////                        adminMap.put("time", DateUtil.getYMDHMSDate(DateUtil.getCurrentTimeStamp()));
////                        adminMap.put("name", organizationVo.getName());
////                        adminMap.put("type", "门诊评估");
////                        adminMap.put("reserve", reserveTime);
////                        smsSender.send(paySuccessAdminTemplateId, phone, adminMap);
////                    }
////                    log.info("给管理员发送支付成功通知短信成功");
////                } catch (Exception e) {
////                    log.error("给管理员发送支付成功通知短信失败:{}", e);
////                }
////            }
////            return true;
////        } catch (WxPayException e) {
////            log.error("处理微信通知失败:{}", e.getMessage());
////            return false;
////        }
////    }
////
////    @Override
////    public boolean notifyOrder(HzsHlwyyPayNotifyRequest request, String appId) {
////
////        try {
////            log.info("收到支付回调:{}", JSON.toJSONString(request));
////            if (request == null) return false;
////            String respCode = request.getRespCode();
////            String respDesc = request.getRespDesc();
////            log.info("respCode:{},respDesc:{}", respCode, respDesc);
////            if (!respCode.equals("00")) {
////                return false;
////            }
////            String outOrderNo = request.getOutOrderNo();
////            String payStatus = request.getPayStatus();
////
////            log.info("微信通知订单号:{},支付状态:{}", outOrderNo, payStatus);
////
////            //1是支付成功
////            if (payStatus.equals("1")) {
////                String orderNo = outOrderNo.substring(0, 22);
////                log.info("微信通知订单号:{}", orderNo);
////                ScaleOrder scaleOrder = getByOrderNo(orderNo);
////                if (scaleOrder == null) {
////                    log.info("订单号:{} 不存在", orderNo);
////                    return false;
////                }
////                log.info("订单号:{} 订单:{}", orderNo, JSON.toJSONString(scaleOrder));
////                if (ScaleOrderStatus.PAID.getCode() == scaleOrder.getOrderStatus()
////                        || ScaleOrderStatus.USED.getCode() == scaleOrder.getOrderStatus()) {
////                    return true;
////                }
////
////                Long totalFee = StringUtil.yuanToFen(request.getTotalAmount());
////                scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
////                scaleOrder.setRefundable(totalFee.intValue());
////                scaleOrder.setPayTime(DateUtil.getCurrentTimeStamp());
////                scaleOrder.setPayNo(request.getSettleOrderNo());
////                scaleOrder.setPayChannel("WXPAY");
////                scaleOrder.setPaidFee(totalFee.intValue());
////                scaleOrder.setOrderStatus(ScaleOrderStatus.PAID.getCode());
////
////                long reserveId = 0;
////                OrderType orderType = OrderType.getOrderType(scaleOrder.getOrderType());
////
////                if (orderType == OrderType.VIDEO_GUIDE || orderType == OrderType.CLINIC_EVALUATE || orderType == OrderType.SCALE_TABLE_ORDER) {
////                    String productId = scaleOrder.getResourceId();
////
////                    if (StringUtils.isNotBlank(productId)) {
////                        reserveId = Long.parseLong(productId);
////                        //修改预约状态
////                        reserveService.updateReserve(reserveId, ReserveStatus.REVIEWED);
////                    }
////                }
////                scaleOrderDao.updateById(scaleOrder);
////
////                User user = userService.getUser(scaleOrder.getUserId());
////                if (orderType == OrderType.SCALE_TABLE_ORDER) {
////                    try {
////                        //给用户发送短信通知
////                        HashMap<String, String> userMap = new HashMap<>();
////                        ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
////                        if (scaleTable != null) {
////                            userMap.put("name", scaleTable.getName());
////                            smsSender.send(paySuccessTemplateId, user.getPhone(), userMap);
////                        }
////                    } catch (Exception e) {
////                        log.error("给{}发送支付成功通知短信失败:{}", user.getPhone(), e);
////                    }
////
////                    try {
////                        //给用户发送短信通知
////                        HashMap<String, String> map = new HashMap<>();
////                        ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
////                        OrganizationVo organizationVo = organizationService.getOrganizationVo(scaleOrder.getOrganizationId());
////                        if (scaleTable != null) {
////                            for (String phone : organizationVo.getSmsPhone()) {
////                                map.put("phone", user.getPhone().substring(7, 11));
////                                map.put("name", scaleTable.getName());
////                                map.put("time", DateUtil.getYMDHMSDate(DateUtil.getCurrentTimeStamp()));
////                                map.put("organization", organizationVo.getName());
////                                smsSender.send(paySuccessAdminNotifyTemplateId, phone, map);
////                            }
////                        }
////                    } catch (Exception e) {
////                        log.error("给{}发送支付成功通知短信失败:{}", user.getPhone(), e);
////                    }
////                }
////                return true;
////            } else {
////                log.info("支付状态为不成功，调过");
////                return true;
////            }
////        } catch (Exception e) {
////            log.error("处理支付回调失败:{}", e);
////        }
////        return false;
////    }
////
////    @Override
////    public ListScaleOrderResponse listScaleOrder(long userId, int pageNo, int pageSize, byte orderType) {
////        Page<ScaleOrder> page = new Page<>(pageNo, pageSize);
////        QueryWrapper<ScaleOrder> wrapper = new QueryWrapper<>();
////        wrapper.eq("user_id", userId);
////        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
////        wrapper.eq("order_type", orderType);
////        wrapper.orderByDesc("created");
////        wrapper.eq("app_id", AuthContext.getAppId());
////        wrapper.eq("channel", AuthContext.getChannel());
////        wrapper.eq("org_id", AuthContext.getOrgId());
////        page = scaleOrderDao.selectPage(page, wrapper);
////
////        List<ScaleOrder> records = page.getRecords();
////        List<ScaleOrderListVo> collect = records.stream().map(record -> convertToScaleOrderListVo(record)).collect(Collectors.toList());
////
////        PageVo pageVo = PageVo.builder()
////                .page(page.getCurrent()).totalPage(page.getPages()).totalRecord(page.getTotal()).build();
////        return new ListScaleOrderResponse(pageVo, collect);
////    }
//
//    @Override
//    public ScaleOrderVo getScaleOrderVo(long id) {
//        ScaleOrder scaleOrder = scaleOrderDao.selectById(id);
//        if (scaleOrder == null) {
//            throw new OldServiceException(ResultCode.PARAM_ERROR);
//        }
//        return convertToScaleOrderVo(scaleOrder);
//    }
//
//    @Override
//    public void useScaleOrder(long id, long evId, long childrenId) {
//        ScaleOrder scaleOrder = scaleOrderDao.selectById(id);
//        if (scaleOrder == null) {
//            throw new OldServiceException(ResultCode.PARAM_ERROR);
//        }
//        Integer availableTimes = scaleOrder.getAvailableTimes();
//        if (availableTimes > 0) {
//            if (availableTimes == 1) {
//                scaleOrder.setOrderStatus(ScaleOrderStatus.USED.getCode());
//            }
//            scaleOrder.setAvailableTimes(availableTimes - 1);
//            scaleOrder.setUsedTimes(scaleOrder.getUsedTimes() + 1);
//        }
//        String scaleEvaluationRecordId = scaleOrder.getScaleEvaluationRecordId();
//        if (StringUtils.isNotBlank(scaleEvaluationRecordId)) {
//            scaleEvaluationRecordId = scaleEvaluationRecordId + "," + evId;
//        } else {
//            scaleEvaluationRecordId = String.valueOf(evId);
//        }
//        scaleOrder.setScaleEvaluationRecordId(scaleEvaluationRecordId);
//        scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
//        scaleOrder.setChildrenId(childrenId);
//        scaleOrderDao.updateById(scaleOrder);
//    }
//
////    @Override
////    public void cancelScaleOrder(long id) {
////        ScaleOrder scaleOrder = scaleOrderDao.selectById(id);
////        if (scaleOrder == null) {
////            throw new ServiceException(ResultCode.PARAM_ERROR);
////        }
////        scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
////        scaleOrder.setOrderStatus(ScaleOrderStatus.CANCELED.getCode());
////        scaleOrderDao.updateById(scaleOrder);
////    }
////
////    @Override
////    public CheckOrderVo checkPaidOrder(long userId, byte scaleCode, PaymentType paymentType, OrderType orderType, OrderUseWay useWay) {
////        CheckOrderVo vo = new CheckOrderVo();
////        //检查该用户账号下有没有已付款的订单，如果有，返回此订单
////        ScaleOrder scaleOrder = getByUserIdAndScaleCodeAndStatus(userId, scaleCode, ScaleOrderStatus.PAID.getCode(), orderType.getCode(), useWay.getCode());
////        if (scaleOrder != null) {
////            if (scaleOrder.getAvailableTimes() > 0) {
////                vo.setHasPaidOrder(true);
////                vo.setOrderId(scaleOrder.getId());
////                vo.setOrderStatus(scaleOrder.getOrderStatus());
////                vo.setOrderStatusStr(ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus()).getDesc());
////                return vo;
////            } else {
////                scaleOrder.setOrderStatus(ScaleOrderStatus.CANCELED.getCode());
////                scaleOrderDao.updateById(scaleOrder);
////            }
////
////        }
////        vo.setHasPaidOrder(false);
////        return vo;
////    }
//
//    @Override
//    public CheckVideoOrderVo checkVideoOrder(long userId, String resourceId) {
//        SysUser user = userService.getUser(userId);
//        if (user == null) {
//            throw new OldServiceException(ResultCode.PARAM_ERROR);
//        }
//        //todo:支付细节下一行
////        WxUser wxUser = wxUserService.getByUserId(userId, PlatformEnum.WX_MINI_APP);
//
//
//
//
//
//
//
//
////        if (StringUtils.isBlank(user.getXiaoeUserId())) {
////            String xiaoeUserId = xiaoeService.createUser(user.getPhone(), wxUser.getUnionId());
////            user.setXiaoeUserId(xiaoeUserId);
////            userService.update(user);
////        }
//
////        GetUserData data = xiaoeService.getUser(user.getPhone(), wxUser.getUnionId());
////        if (data != null) {
////            user.setXiaoeUserId(data.getUserId());
////            userService.update(user);
////        }
////        CheckPermissionData checkPermissionData = xiaoeService.checkPermission(user.getXiaoeUserId(), resourceId);
//
//        CheckVideoOrderVo vo = new CheckVideoOrderVo();
//
//        //先查询数据库是否有此 resourceId 的订单
//        ScaleOrder order = getByResourceIdAndUserId(userId, resourceId);
//
//        if (order != null) {
//            if (order.getOrderStatus() == ScaleOrderStatus.WAIT_PAY.getCode()) {
//                vo.setOrderId(order.getId());
//            }
//        }
//
//        //是否有资源权限以小鹅通返回的结果为准
////        if (checkPermissionData != null) {
////            boolean hasPaid = checkPermissionData.getAuthState() == 1;
////            if (hasPaid) {
////                vo.setHasPaidOrder(true);
////                //TODO 补全 page
////                vo.setAppId("wx98dc9b974915de77");
////                vo.setPage("page/home/content/content_video/content_video?id=" + resourceId);
////            } else {
////                vo.setHasPaidOrder(false);
////                vo.setAppId("");
////                vo.setPage("");
////            }
////        } else {
////            vo.setHasPaidOrder(false);
////            vo.setAppId("");
////            vo.setPage("");
////        }
//        vo.setResourceId(resourceId);
//        return vo;
//    }
//
////    @Override
////    public CheckVideoOrderVo createVideoOrder(LoginVo user, String resourceId, String productId) {
////        GetGoodsParam param = new GetGoodsParam();
////        String[] resourceIds = new String[1];
////        List<String> storeResourceIds = new ArrayList<>();
////        if (StringUtils.isNotBlank(productId)) {
////            resourceIds[0] = productId;
////            //6为专栏，详见小鹅通 api
////            param.setType(6);
//////            List<ProductGoodList> list = xiaoeService.getGoodsByProductId(productId, 6);
//////            for (ProductGoodList productGoodList : list) {
//////                storeResourceIds.add(productGoodList.getResourceId());
//////            }
////        } else {
////            resourceIds[0] = resourceId;
////            //3为视频，详见小鹅通 api
////            param.setType(3);
////            storeResourceIds.add(resourceId);
////        }
////        param.setIds(resourceIds);
////        ArrayList<GetGoodsParam> list = new ArrayList<>();
////        list.add(param);
//////        List<GetGoodsData> goods = xiaoeService.getGoods(list);
//////        if (CollectionUtils.isNotEmpty(goods)) {
//////            GetGoodsData getGoodsData = goods.get(0);
//////            ScaleOrder scaleOrder = new ScaleOrder();
//////            scaleOrder.setChildrenId(0l);
//////            scaleOrder.setCreated(DateUtil.getCurrentTimeStamp());
//////            scaleOrder.setOrderNo(StringUtil.getOrderNo());
//////            scaleOrder.setOrderStatus(ScaleOrderStatus.WAIT_PAY.getCode());
//////            scaleOrder.setPaidFee(0);
//////            scaleOrder.setPayChannel("");
//////            scaleOrder.setPayNo("");
//////            scaleOrder.setPayTime(0l);
//////            scaleOrder.setRefundable(0);
//////            scaleOrder.setRefunded(0);
//////            scaleOrder.setScaleEvaluationRecordId("");
//////            scaleOrder.setScaleTableCode((byte)0);
//////            scaleOrder.setStatus(BaseStatus.NORMAL.getStatus());
//////            if (env.equalsIgnoreCase("test")) {
//////                scaleOrder.setTotalFee(1);
//////            } else {
//////                scaleOrder.setTotalFee(getGoodsData.getPriceLow());
//////            }
//////            scaleOrder.setAvailableTimes(0);
//////            scaleOrder.setTotalTimes(0);
//////            scaleOrder.setUsedTimes(0);
//////            scaleOrder.setUpdated(DateUtil.getCurrentTimeStamp());
//////            scaleOrder.setUserId(user.getId());
//////            scaleOrder.setOrderType(OrderType.VIDEO_ORDER.getCode());
//////
//////            //这个地方如果是专栏的话，把专栏下所有视频的 resourceId 全部保存到里面
//////            scaleOrder.setResourceId(StringUtils.join(storeResourceIds.toArray(), ","));
//////            scaleOrder.setProductId(productId);
//////            scaleOrder.setTitle(getGoodsData.getGoodsName());
//////            scaleOrder.setXiaoeOrderId("");
//////            scaleOrder.setInvoiceFileId("");
//////            scaleOrder.setPayment(PaymentType.ON_LINE.getCode());
//////            scaleOrder.setReason("");
//////            scaleOrder.setOrganizationId(user.getOrganizationId());
//////            scaleOrderDao.insert(scaleOrder);
//////
//////            CheckVideoOrderVo vo = new CheckVideoOrderVo();
//////            vo.setResourceId(resourceId);
//////            vo.setAppId("");
//////            vo.setPage("");
//////            vo.setHasPaidOrder(false);
//////            vo.setOrderId(scaleOrder.getId());
//////
////////            User user = userService.getUser(userId);
////////            CreateOrderData data = xiaoeService.createOrder(user.getXiaoeUserId(), productId, resourceId, scaleOrder.getOrderNo());
//////            return vo;
//////        } else {
//////            throw new ServiceException(ResultCode.INTERNAL_SERVER_ERROR);
//////        }
////        return null;
////    }
////
////    @Override
////    public Long saveOrder(ScaleOrder scaleOrder) {
////        scaleOrderDao.insert(scaleOrder);
////        return scaleOrder.getId();
////    }
//
//    private ScaleOrder getByOrderNo(String orderNo) {
//        QueryWrapper<ScaleOrder> wrapper = new QueryWrapper<>();
//        wrapper.eq("order_no", orderNo);
//        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
//        return scaleOrderDao.selectOne(wrapper);
//    }
//
////    private ScaleOrderListVo convertToScaleOrderListVo(ScaleOrder scaleOrder) {
////        ScaleOrderListVo vo = new ScaleOrderListVo();
////        vo.setCreated(DateUtil.getYMDHMSDate(scaleOrder.getCreated()));
////        vo.setId(scaleOrder.getId());
//////        String title = "";
//////        if (scaleOrder.getOrderType() == OrderType.SCALE_TABLE_ORDER.getCode()) {
//////            ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
//////            title = scaleTable != null ? scaleTable.getName() : "";
//////        }
////        vo.setScaleTableCode(scaleOrder.getScaleTableCode());
////        vo.setName(scaleOrder.getTitle());
////        vo.setStatus(scaleOrder.getOrderStatus());
////        vo.setStatusString(ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus()).getDesc());
////        vo.setTotalFee(BaseWxPayResult.fenToYuan(scaleOrder.getTotalFee()));
////        vo.setOrderType(scaleOrder.getOrderType());
////        return vo;
////    }
//
//    private ScaleOrderVo convertToScaleOrderVo(ScaleOrder scaleOrder) {
//        ScaleOrderVo vo = new ScaleOrderVo();
//        vo.setCreated(DateUtil.getYMDHMSDate(scaleOrder.getCreated()));
//        vo.setId(scaleOrder.getId());
//        String title = "";
//        if (scaleOrder.getOrderType() == OrderType.SCALE_TABLE_ORDER.getCode()) {
//            ScaleTable scaleTable = scaleTableService.getScaleTable(scaleOrder.getScaleTableCode());
//            title = scaleTable.getName();
//        }
//        vo.setScaleTableCode(scaleOrder.getScaleTableCode());
//        vo.setName(title);
//        vo.setOrderType(scaleOrder.getOrderType());
//        vo.setStatus(scaleOrder.getOrderStatus());
//        vo.setStatusString(ScaleOrderStatus.getScaleOrderStatus(scaleOrder.getOrderStatus()).getDesc());
//        //todo:微信支付
////        vo.setTotalFee(BaseWxPayResult.fenToYuan(scaleOrder.getTotalFee()));
//        vo.setOrderNo(scaleOrder.getOrderNo());
//        //todo:微信支付
////        vo.setPaidFee(BaseWxPayResult.fenToYuan(scaleOrder.getPaidFee()));
//        vo.setPaidTime(scaleOrder.getPayTime() == 0 ? "" : DateUtil.getYMDHMSDate(scaleOrder.getPayTime()));
//        if (StringUtils.isBlank(scaleOrder.getScaleEvaluationRecordId())) {
//            vo.setUsedTime("");
//        } else {
////            ScaleRecordVo scaleRecordVo = scaleEvaluationRecordService.getRecordReport(scaleOrder.getScaleEvaluationRecordId());
//            vo.setUsedTime(DateUtil.getYMDHMSDate(scaleOrder.getUpdated()));
//        }
//        return vo;
//    }
//
////    @Override
////    public ScaleOrder getByUserIdAndScaleCodeAndStatus(long userId, byte scaleCode, byte status, byte orderType, byte useWay){
//////        QueryWrapper<ScaleOrder> wrapper = new QueryWrapper<>();
//////        wrapper.eq("user_id", userId);
//////        if (scaleCode != 0) {
//////            wrapper.eq("scale_table_code", scaleCode);
//////        }
//////        wrapper.eq("order_status", status);
//////        if (orderType != 0) {
//////            wrapper.eq("order_type", orderType);
//////        }
//////        wrapper.eq("status", BaseStatus.NORMAL.getStatus());
////
////        LambdaQueryWrapper<ScaleOrder> wrapper = new QueryWrapper<ScaleOrder>().lambda();
////        wrapper.eq(ScaleOrder::getUserId, userId);
////        if (scaleCode != 0) {
////            wrapper.eq(ScaleOrder::getScaleTableCode, scaleCode);
////        }
////        if (orderType != 0) {
////            wrapper.eq(ScaleOrder::getOrderType, orderType);
////        }
////        if (useWay != 0) {
////            wrapper.eq(ScaleOrder::getUseWay, useWay);
////        }
////        wrapper.eq(ScaleOrder::getOrderStatus, status);
////        wrapper.eq(ScaleOrder::getStatus, BaseStatus.NORMAL.getStatus());
////        //todo:微信支付相关参数修改
//////        wrapper.eq(ScaleOrder::getAppId, AuthContext.getAppId());
//////        wrapper.eq(ScaleOrder::getChannel, AuthContext.getChannel());
//////        wrapper.eq(ScaleOrder::getOrgId, AuthContext.getOrgId());
////
////        List<ScaleOrder> scaleOrders = scaleOrderDao.selectList(wrapper);
////        if (CollectionUtils.isNotEmpty(scaleOrders)) {
////            return scaleOrders.get(0);
////        }
////        return null;
////    }
//
//    private ScaleOrder getByResourceIdAndUserId(Long userId, String resourceId) {
//        LambdaQueryWrapper<ScaleOrder> wrapper = new QueryWrapper<ScaleOrder>().lambda();
//        wrapper.eq(ScaleOrder::getUserId, userId)
//                .in(ScaleOrder::getResourceId, resourceId)
//                .eq(ScaleOrder::getStatus, BaseStatus.NORMAL.getStatus());
//        List<ScaleOrder> scaleOrders = scaleOrderDao.selectList(wrapper);
//        if (CollectionUtils.isNotEmpty(scaleOrders)) {
//            return scaleOrders.get(0);
//        }
//        return null;
//    }
//}
