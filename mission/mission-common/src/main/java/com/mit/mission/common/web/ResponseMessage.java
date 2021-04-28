package com.mit.mission.common.web;

import com.mit.mission.common.exception.CustomException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Setter
@Getter
@Accessors(chain = true)
public class ResponseMessage<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;

    private ResponseMessage(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseMessage OK(String message, T data) {
        return new ResponseMessage(HttpServletResponse.SC_OK, message, data);
    }

    public static ResponseMessage OK() {
        return new ResponseMessage(HttpServletResponse.SC_OK, null, null);
    }

    public static ResponseMessage OK(String message) {
        return new ResponseMessage(HttpServletResponse.SC_OK, message, null);
    }

    public static ResponseMessage Error(Integer code, String message) {
        return new ResponseMessage(code, message, null);
    }

    public static ResponseMessage Error(CustomException customException) {
        return new ResponseMessage(customException.getCode(), customException.getMessage(), null);
    }
}