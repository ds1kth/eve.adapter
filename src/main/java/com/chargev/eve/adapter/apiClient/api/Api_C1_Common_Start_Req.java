package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class Api_C1_Common_Start_Req {
    String mbrCardNo;
    String chargeReqAmount;
    String chargeTime;
    String payMethod;
    String chargePrice;
    String tid;
    String chargerType;;
    String chargingCommand;
    String chargingProfile;
    String connectorId;
}
