package com.chargev.eve.adapter;

import com.chargev.eve.adapter.socketServer.ChargevService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
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

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private ChargevService chargevService;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info(MARKER_CSMS, "Start Application => version: {}", buildProperties.getVersion());
        chargevService.start();
    }
}
