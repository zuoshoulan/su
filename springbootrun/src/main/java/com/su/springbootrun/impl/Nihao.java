package com.su.springbootrun.impl;

import com.su.springbootrun.Hello;

import java.time.ZonedDateTime;

/**
 * @Author Weikang Lan
 * @Created 2019-03-05 15:18
 */
public class Nihao implements Hello {

    @Override
    public String sayHi() {

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println("zonedDateTime:" + zonedDateTime);
        return zonedDateTime.toString();
    }
}
