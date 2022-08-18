package com.cpp.supplychain.gateway.router.r2dbc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author zhengweikang@hz-cpp.com
 * @date 2022/6/21 16:46
 */
@Data
@Table("url_white_list")
public class UrlWhiteListEntity {
    @Id
    private Integer whiteListId;

    private String createBy;

    private LocalDateTime createTime;

    private String httpMethed;

    private Boolean isEffective;

    private String updateBy;

    private LocalDateTime updateTime;

    private String url;

}
