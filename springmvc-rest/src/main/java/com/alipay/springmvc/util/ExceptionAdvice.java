package com.alipay.springmvc.util;

import com.alipay.springmvc.model.ResponseObj;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;

/**
 * Created by tangkun.tk on 2016/8/30.
 * 利用AOP 对所有 controller 进行异常处理
 */
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseObj handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        System.out.println("bad request");
        return new ResponseObj().failure("could not read json");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseObj handleValidationException(ValidationException e){
        System.out.println("参数验证,解析失败");
        return new ResponseObj().failure("could not read json");
    }


//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseObj handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
//        System.out.println("参数解析失败");
//        return new ResponseObj().failure("could not read json");
//    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseObj handleException(Exception e){
        System.out.println("Exception, 参数解析失败");
        return new ResponseObj().failure("500 error ");
    }

}
