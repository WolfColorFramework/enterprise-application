package com.mit.mission.security.controller;

import com.mit.mission.common.web.ResponseMessage;
import com.mit.mission.security.util.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    TokenManager tokenManager;

    @RequestMapping(value = "/refresh-token", method = RequestMethod.POST)
    public ResponseMessage refresh(@RequestHeader("token") String token) {
        return ResponseMessage.OK(tokenManager.refreshToken(token));
    }

}
