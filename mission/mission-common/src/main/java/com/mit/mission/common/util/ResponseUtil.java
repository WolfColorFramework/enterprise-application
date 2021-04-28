package com.mit.mission.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mit.mission.common.web.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  @autor: gaoy
 *  @date: 2021/4/22 15:27
 *  @description: Response工具类
 */
public class ResponseUtil {

    public static void out(HttpServletResponse response, ResponseMessage r) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
