package com.cpp.supplychain.gateway.router.task;

import com.cpp.supplychain.gateway.router.r2dbc.UrlWhiteListEntity;
import com.cpp.supplychain.gateway.router.r2dbc.UrlWhiteListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/21 20:34
 */
@Component
@Slf4j
public class UrlWhiteListTask {


    public static List<UrlWhiteListEntity> urlWhiteListEntityList = Collections.synchronizedList(new ArrayList<>());

    @Resource
    private UrlWhiteListRepository urlWhiteListRepository;

    @PostConstruct
    public synchronized void init() {
        log.info("UrlWhiteListTask init");
        loadFromMysql();
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public synchronized void loadFromMysql() {
        try {
            List<UrlWhiteListEntity> tmpList = Collections.synchronizedList(new ArrayList<>());
            //todo 后续用id来遍历
            urlWhiteListRepository.findAll()
                    .doOnNext(new Consumer<UrlWhiteListEntity>() {
                        @Override
                        public void accept(UrlWhiteListEntity urlWhiteListEntity) {
                            log.info("urlWhiteListEntity:{}", urlWhiteListEntity);
                            tmpList.add(urlWhiteListEntity);
                        }
                    })
                    .then(new Mono<Object>() {
                        @Override
                        public void subscribe(CoreSubscriber<? super Object> actual) {
                            urlWhiteListEntityList = tmpList;
                        }
                    })
                    .subscribe()
            ;
        } catch (Exception e) {
            log.error("UrlWhiteListTask Exception", e);
        }
    }
}
