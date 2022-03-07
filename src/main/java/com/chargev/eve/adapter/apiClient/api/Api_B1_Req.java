package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class Api_B1_Req {
    private String mode;
    private String modeType;
    private String soundSet;
    private String chargeKwh;
}
