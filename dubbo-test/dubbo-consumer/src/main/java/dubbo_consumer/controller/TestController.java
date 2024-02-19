package dubbo_consumer.controller;

import com.su.dubbo_interface.AddressService;
import com.su.dubbo_interface.DubboUserService;
import com.su.dubbo_interface.HelloService;
import com.su.dubbo_interface.param.UserQueryParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.ZonedDateTime;

@RequestMapping("/test")
@RestController
public class TestController {

    @DubboReference
    DubboUserService dubboUserService;
    @DubboReference
    AddressService addressService;
    @DubboReference(scope = "SCOPE_REMOTE")
    HelloService helloService;

    @GetMapping("/test")
    public Object test() {
        return "dubbo-consumer:" + ZonedDateTime.now();
    }

    @GetMapping("/userInfo")
    public Object userInfo(UserQueryParam userQueryParam) {
        return dubboUserService.queryUserInfo(userQueryParam);
    }

    @GetMapping("/address")
    public Object address(String name) {
        return addressService.queryAddressByName(name);
    }

    @GetMapping("/hi")
    public Object hi(String name) {
        return helloService.sayHi(name);
    }
}
