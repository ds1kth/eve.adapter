package com.chargev.eve.adapter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AdapterApplicationProperties {

    @Value(value = "${socketServer.address}")
    private String socketServerAddress;

    @Value(value = "${socketServer.port}")
    private int socketServerPort;

    @Value(value = "${apiServer.address}")
    private String apiServerAddress;

    @Value(value = "${apiServer.port}")
    private int apiServerPort;

    @Value(value = "${bizServer.address}")
    private String bizServerAddress;

    @Value(value = "${bizServer.port}")
    private int bizServerPort;
}
