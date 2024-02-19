package com.su.dubbo_provider.service;

import com.su.dubbo_interface.DubboUserService;
import com.su.dubbo_interface.dto.UserInfoDTO;
import com.su.dubbo_interface.enums.SexEnum;
import com.su.dubbo_interface.param.UserQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Slf4j
@DubboService
public class DubboUserServiceImpl implements DubboUserService {
    @Override
    public UserInfoDTO queryUserInfo(UserQueryParam param) {
        log.info("param:{}", param);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName("name:" + RandomStringUtils.randomAlphanumeric(5));
        userInfoDTO.setAge(RandomUtils.nextInt(15, 25));
        userInfoDTO.setSexEnum(RandomUtils.nextBoolean() ? SexEnum.MALE : SexEnum.FEMALE);
        log.info("userInfoDTO:{}", userInfoDTO);
        return userInfoDTO;
    }

    @Override
    public String sayHello(String name) {
        return "hello," + name;
    }
}
