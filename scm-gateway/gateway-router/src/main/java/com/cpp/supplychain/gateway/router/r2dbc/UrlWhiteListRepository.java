package com.cpp.supplychain.gateway.router.r2dbc;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/21 16:17
 */
public interface UrlWhiteListRepository extends ReactiveCrudRepository<UrlWhiteListEntity, Long>, ReactiveQueryByExampleExecutor<UrlWhiteListEntity> {

}

