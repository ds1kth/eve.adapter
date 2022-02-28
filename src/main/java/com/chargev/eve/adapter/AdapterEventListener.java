package com.chargev.eve.adapter;

import com.chargev.eve.adapter.socketServer.ChargevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdapterEventListener {

    private static final Marker MARKER_CSMS = MarkerFactory.getMarker("[Adapter]");
    private static final Logger LOGGER = LoggerFactory.getLogger(AdapterEventListener.class);

//    @Autowired
//    AdapterApplicationProperties applicationProperties;

    @Autowired
    private ChargevService chargevService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        LOGGER.info(MARKER_CSMS, "Start Application => startTime: {}", new Date(event.getTimestamp()));
//
//        // socket, api, biz 서버의 주소, 포트 정보
//        LOGGER.info(MARKER_CSMS, "socket server address : {}", applicationProperties.getSocketServerAddress());
//        LOGGER.info(MARKER_CSMS, "socket server port : {}", applicationProperties.getSocketServerPort());
//
//        LOGGER.info(MARKER_CSMS, "api server address : {}", applicationProperties.getApiServerAddress());
//        LOGGER.info(MARKER_CSMS, "api server port : {}", applicationProperties.getApiServerPort());
//
//        LOGGER.info(MARKER_CSMS, "biz server address : {}", applicationProperties.getBizServerAddress());
//        LOGGER.info(MARKER_CSMS, "biz server port : {}", applicationProperties.getBizServerPort());

        // socket server 를 시작한다.
//        GreetServer server = new GreetServer();
//        server.start(6666);

        // serverRunner.start();

        chargevService.start();
    }
}
