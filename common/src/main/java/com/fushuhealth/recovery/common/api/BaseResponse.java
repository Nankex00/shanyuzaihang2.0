package com.fushuhealth.recovery.common.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fushuhealth.recovery.common.locale.LocaleMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private String message;

    private boolean success = true;

    private int code;

    private T data;

    public static BaseResponse error(ResultCode resultCode) {
        return BaseResponse.builder()
                .message(resultCode.getMsg())
                .code(resultCode.code)
                .success(ResultCode.SUCCESS == resultCode ? true : false).build();
    }

    public static BaseResponse success() {
        return BaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true).build();
    }

    public static BaseResponse success(Collection<?> collection) {
        if (collection == null) {
            collection = CollectionUtils.EMPTY_COLLECTION;
        }
        return BaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true)
                .data(collection).build();
    }

    public static <T> BaseResponse success(T t) {
        return BaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true)
                .data(t).build();
    }
}
