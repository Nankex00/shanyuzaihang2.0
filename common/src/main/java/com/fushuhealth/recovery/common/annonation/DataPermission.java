package com.fushuhealth.recovery.common.annonation;

import java.lang.annotation.*;

/**
 * 数据权限组
 *
 * @author Zhuanz
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    DataColumn[] value();

}
