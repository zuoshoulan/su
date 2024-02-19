package com.su.dubbo_interface;

import com.su.dubbo_interface.dto.UserInfoDTO;
import com.su.dubbo_interface.param.UserQueryParam;

public interface DubboUserService {

    UserInfoDTO queryUserInfo(UserQueryParam param);

    String sayHello(String name);


}
