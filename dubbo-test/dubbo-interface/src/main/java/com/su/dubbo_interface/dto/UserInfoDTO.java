package com.su.dubbo_interface.dto;

import com.su.dubbo_interface.enums.SexEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {

    private String userName;

    private Integer age;

    private SexEnum sexEnum;
}
