package com.chargev.eve.adapter.socketServer;

import com.chargev.eve.adapter.AdapterApplicationProperties;
import com.chargev.eve.adapter.socketServer.me.MeMessageHandler;
import com.chargev.eve.adapter.socketServer.me.MeTcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
public class ChargevService{

    private int listenerPort;

    private AdapterApplicationProperties applicationProperties;
    private MeMessageHandler chargevMessageHandler;
    private MeTcpServer chargevServer;

    @Autowired
    ChargevService(AdapterApplicationProperties applicationProperties, MeMessageHandler chargevMessageHandler){
        this.applicationProperties = applicationProperties;
        this.chargevMessageHandler = chargevMessageHandler;
    }

    public void start() {
        listenerPort = applicationProperties.getSocketServerPort();
        log.info("listener.port: {}", listenerPort);

        chargevServer = new MeTcpServer(listenerPort, chargevMessageHandler);
        new Thread(chargevServer, String.valueOf(listenerPort)).start();
    }

//    @PreDestroy
//    public void shutdown() {
//        chargevServer.shutdown();
//    }

//    @Override
//    public void push(Message message) {
//        Channels.send(message);
//    }
}