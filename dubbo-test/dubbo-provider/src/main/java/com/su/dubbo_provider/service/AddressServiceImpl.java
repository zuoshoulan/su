package com.su.dubbo_provider.service;

import com.su.dubbo_interface.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService
public class AddressServiceImpl implements AddressService {
    @Override
    public String queryAddressByName(String name) {
        return name+" zhu在浙江省杭州市";
    }
}
