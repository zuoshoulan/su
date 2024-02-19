package com.su.java_zookeeper;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@SpringBootApplication
public class ZookeeperApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);
    }

    @PostConstruct
    public void init() throws QuorumPeerConfig.ConfigException, IOException {

        int port = 2181;

        Properties properties = new Properties();
        File file = new File(System.getProperty("java.io.tmpdir")
                + File.separator + UUID.randomUUID());
        file.deleteOnExit();
        properties.setProperty("dataDir", file.getAbsolutePath());
        properties.setProperty("clientPort", String.valueOf(port));

        QuorumPeerConfig quorumPeerConfig = new QuorumPeerConfig();
        quorumPeerConfig.parseProperties(properties);

        ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
        ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumPeerConfig);

        System.setProperty("zookeeper.admin.enableServer", "false");

        try {
            zkServer.runFromConfig(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
