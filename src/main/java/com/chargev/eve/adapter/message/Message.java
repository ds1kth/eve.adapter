package com.chargev.eve.adapter.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

@Getter @Setter @Builder
public class Message {
    //String stationId;
    String chargerId;   // stationId + chargerId = 10bytes
    String cmd;
    String payload;
}
