package com.fushuhealth.recovery.common.error;

import com.fushuhealth.recovery.common.api.BaseResponse;
import com.fushuhealth.recovery.common.api.ResultCode;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionTranslator {

    static final Logger logger = LoggerFactory.getLogger(GlobalExceptionTranslator.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse handleError(MissingServletRequestParameterException e) {
        logger.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return new BaseResponse().error(ResultCode.PARAM_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse handleError(MethodArgumentNotValidException e) {
        logger.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return new BaseResponse().error(ResultCode.PARAM_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse handleError(ConstraintViolationException e) {
        logger.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return new BaseResponse().error(ResultCode.PARAM_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResponse handleError(NoHandlerFoundException e) {
        logger.error("404 Not Found", e);
        return new BaseResponse().error(ResultCode.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public BaseResponse handleError(ServiceException e) {
        logger.error("Service Exception", e);
        return new BaseResponse().error(e.getResultCode());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public BaseResponse handleError(PermissionDeniedException e) {
        logger.error("Permission Denied", e);
        return new BaseResponse().error(ResultCode.PERMISSION_ERROR);
    }

    @ExceptionHandler(NotLoginException.class)
    public BaseResponse handleError(NotLoginException e) {
        logger.error("Not Login", e);
        return new BaseResponse().error(ResultCode.USER_NOT_LOGIN);
    }

    @ExceptionHandler(Throwable.class)
    public BaseResponse handleError(Throwable e) {
        logger.error("Internal Server Error", e);
        return new BaseResponse().error(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
