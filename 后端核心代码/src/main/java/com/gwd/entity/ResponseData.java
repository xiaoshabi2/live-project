package com.gwd.entity;

import java.io.Serializable;

public class ResponseData implements Serializable {


    public static final  int STATUS_OK = 200;
    public static final  String STATUS_OK_MSG = "操作成功";

    public static final int STATUS_OTHER = 202;
   // private static final  String STATUS_ERROR_OTHER = "其他信息";


    public static final int STATUS_NOLOGIN = 301;
    public static final String STATUS_NOLOGIN_MSG = "请先登录";

    public static final int STATUS_ERROR = 404;
    public static final String STATUS_ERROR_MSG = "操作失败";



    private Integer code;
    private String msg;
    private Object data;

    public ResponseData() {
        this.code=STATUS_OK;
        this.msg=STATUS_OK_MSG;
    }


    public void setStatusError(){
        this.code=STATUS_ERROR;
        this.msg=STATUS_ERROR_MSG;
    }

    public void setStatusNoLogin(){
        this.code=STATUS_NOLOGIN;
        this.msg=STATUS_NOLOGIN_MSG;
    }

    public void setStatusOther(String msg){
        this.code=STATUS_OTHER;
        this.msg=msg;
    }


    @Override
    public String toString() {
        return "ResponseData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
