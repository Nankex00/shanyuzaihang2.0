package com.fushuhealth.recovery.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OldBaseResponse<T> {

    private String message;

    private boolean success = true;

    private int code;

    private T data;

    public static OldBaseResponse error(ResultCode resultCode) {
        return OldBaseResponse.builder()
                .message(resultCode.getMsg())
                .code(resultCode.code)
                .success(ResultCode.SUCCESS == resultCode ? true : false).build();
    }

    public static OldBaseResponse success() {
        return OldBaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true).build();
    }

    public static OldBaseResponse success(Collection<?> collection) {
        if (collection == null) {
            collection = CollectionUtils.EMPTY_COLLECTION;
        }
        return OldBaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true)
                .data(collection).build();
    }

    public static <T> OldBaseResponse success(T t) {
        return OldBaseResponse.builder()
                .message(ResultCode.SUCCESS.getMsg())
                .code(ResultCode.SUCCESS.code)
                .success(true)
                .data(t).build();
    }
}
