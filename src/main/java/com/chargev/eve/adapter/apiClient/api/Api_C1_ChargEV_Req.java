package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class Api_C1_ChargEV_Req {
    String mbrCardNo;
    String tid;
    String chargingCommand;
    String chargedTime;
}
