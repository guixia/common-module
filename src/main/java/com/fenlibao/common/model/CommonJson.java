package com.fenlibao.common.model;

public class CommonJson {

    public String message;//状态提示消息
    public String code;//请求状态码

    public CommonJson(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
