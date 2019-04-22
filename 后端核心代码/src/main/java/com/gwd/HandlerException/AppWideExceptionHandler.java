package com.gwd.HandlerException;

import com.gwd.entity.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class AppWideExceptionHandler {

   @ExceptionHandler(Exception.class)
    public ResponseData HandlerException(Exception e){
      //  System.out.println("全局异常处理");
        e.printStackTrace();
        ResponseData responseData = new ResponseData();
        e.printStackTrace();
        responseData.setStatusOther("传的参数有问题");
        responseData.setData(e.getMessage());
        return responseData;
    }
}
