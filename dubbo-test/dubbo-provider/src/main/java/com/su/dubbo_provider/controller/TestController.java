package com.su.dubbo_provider.controller;

import com.su.dubbo_interface.DubboUserService;
import com.su.dubbo_interface.param.UserQueryParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@RequestMapping("/test")
@RestController
public class TestController {

    @Resource
    DubboUserService dubboUserService;

    @GetMapping("/test")
    public Object test() {
        return "dubbo-provider:" + ZonedDateTime.now();
    }

    @GetMapping("/userInfo")
    public Object userInfo(UserQueryParam userQueryParam) {
        return dubboUserService.queryUserInfo(userQueryParam);
    }
}
