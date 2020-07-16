package com.owesome.facerecognition.utils;

import java.io.Serializable;

public class HttpResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String description;
    private Object payload;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public static HttpResponse success() {
        HttpResponse response = new HttpResponse();
        return success(null);
    }

    public static HttpResponse success(Object payload) {
        HttpResponse response = new HttpResponse();
        return success("200","success",payload);
    }

    public static HttpResponse success(String code,String description,Object payload) {
        HttpResponse response = new HttpResponse();
        response.code = "200";
        response.description = "success";
        response.payload = payload;
        return response;
    }
}
