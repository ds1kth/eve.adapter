package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Api_C1_Req {
    String mbrCardNo;
    String charge;  // current
    String chargeTime;
    String paymentType;
    String pay;
    String plugType;
}
