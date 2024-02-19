package com.su.dubbo_provider;

import com.su.dubbo_interface.AddressService;
import com.su.dubbo_interface.DubboUserService;
import com.su.dubbo_provider.service.AddressServiceImpl;
import com.su.dubbo_provider.service.DubboUserServiceImpl;
import lombok.SneakyThrows;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

public class ProviderMain {
    @SneakyThrows
    public static void main(String[] args) {

        {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setApplication(new ApplicationConfig("main-provider"));
            serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
            serviceConfig.setInterface(DubboUserService.class);
            serviceConfig.setRef(new DubboUserServiceImpl());
            ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 20881);
            serviceConfig.setProtocol(protocolConfig);
            serviceConfig.export();
        }
        {
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.setApplication(new ApplicationConfig("main-provider"));
            serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
            serviceConfig.setInterface(AddressService.class);
            serviceConfig.setRef(new AddressServiceImpl());
            ProtocolConfig protocolConfig = new ProtocolConfig("dubbo", 20881);
            serviceConfig.setProtocol(protocolConfig);
            serviceConfig.export();
        }
        Thread.sleep(Long.MAX_VALUE);

    }
}
