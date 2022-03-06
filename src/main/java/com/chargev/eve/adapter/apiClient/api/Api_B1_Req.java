package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Api_B1_Req {
    private String mode;
    private String modeType;
    private String soundSet;
    private String chargeKwh;
}
