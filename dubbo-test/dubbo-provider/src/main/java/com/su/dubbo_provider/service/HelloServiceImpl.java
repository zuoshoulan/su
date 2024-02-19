package com.su.dubbo_provider.service;

import com.su.dubbo_interface.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHi(String name) {
        return "hi," + name;
    }
}
