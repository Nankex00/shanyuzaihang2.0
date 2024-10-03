//package com.fushuhealth.recovery.device.interceptor;
//
//import java.lang.reflect.Method;
//
//import com.alibaba.fastjson.JSON;
//import com.fushuhealth.recovery.common.annonation.RepeatSubmit;
//import com.fushuhealth.recovery.common.api.AjaxResult;
//import com.fushuhealth.recovery.common.util.ServletUtils;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
///**
// * 防止重复提交拦截器
// *
// * @author Zhuanz
// */
//@Component
//public abstract class RepeatSubmitInterceptor implements HandlerInterceptor
//{
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
//    {
//        if (handler instanceof HandlerMethod)
//        {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
//            if (annotation != null)
//            {
//                if (this.isRepeatSubmit(request, annotation))
//                {
//                    AjaxResult ajaxResult = AjaxResult.error(annotation.message());
//                    ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
//                    return false;
//                }
//            }
//            return true;
//        }
//        else
//        {
//            return true;
//        }
//    }
//
//    /**
//     * 验证是否重复提交由子类实现具体的防重复提交的规则
//     *
//     * @param request 请求信息
//     * @param annotation 防重复注解参数
//     * @return 结果
//     * @throws Exception
//     */
//    public abstract boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit annotation);
//
//}
