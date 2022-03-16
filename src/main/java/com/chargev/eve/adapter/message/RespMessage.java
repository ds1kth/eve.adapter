package com.chargev.eve.adapter.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class RespMessage {
    String INS;
    String ML;
    String VD;
}