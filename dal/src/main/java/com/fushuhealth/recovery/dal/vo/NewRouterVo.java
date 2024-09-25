package com.fushuhealth.recovery.dal.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Zhuanz
 * @date 2024/9/25
 */
@Data
public class NewRouterVo {
    private String label;

    private String key;

    private String rules;

    private String icon;

    private List<NewRouterVo> children;

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    private Boolean alwaysShow;

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    private boolean hidden;

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    private String redirect;

}
