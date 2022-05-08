package com.chargev.eve.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdapterApplicationPropertiesTest {

    @Autowired
    AdapterApplicationProperties applicationProperties;

    @Test
    void configConfirm() {
        System.out.println("socket server address : " + applicationProperties.getSocketServerAddress());
        System.out.println("socket server port : " + applicationProperties.getSocketServerPort());

        System.out.println("api server address : " + applicationProperties.getApiServerAddress());
        System.out.println("api server port : " + applicationProperties.getApiServerPort());

        System.out.println("biz server address : " + applicationProperties.getBizServerAddress());
        System.out.println("biz server port : " + applicationProperties.getBizServerPort());
    }
}