package com.qihsoft.webdev.config.exception;

import com.qihsoft.webdev.utils.convert.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @author Tanj
 * @create 2017-10-15 18:55
 * 全局异常捕获
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionAdvice.class);
    private ResponseEntity object;

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return R.error(400, "缺少必要的请求参数");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return R.error(400, "参数解析失败==>" + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return R.error(400, "参数验证失败==>" + e.getMessage());
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity handleBindException(BindException e) {
        return R.error(400, "参数绑定失败");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleServiceException(ConstraintViolationException e) {
        return R.error(400, "约束验证失败");
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleValidationException(ValidationException e) {
        return R.error(400, "参数验证失败");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return R.error(405, String.format("该API不支持[%s]请求方法", e.getMethod()));
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity handleHttpMediaTypeNotSupportedException(Exception e) {
        return R.error(415, "不支持当前请求数据类型");
    }

    /**
     * 500 - Internal Server Error
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(ServiceException.class)
//    public ResponseEntity handleServiceException(ServiceException e) {
//        return R.error(500,"业务服务异常");
//    }

    /**
     * 500 - Internal Server Error
     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handleException(Exception e) {
//        return R.error(500,"服务器异常==>"+ e.fillInStackTrace());
//    }

    /**
     * 操作数据库出现异常:名称重复，外键关联
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleException(DataIntegrityViolationException e) {
        return R.error(500, "操作数据库出现异常,数据回滚初始状态.\n" + e.getLocalizedMessage());
    }
}
