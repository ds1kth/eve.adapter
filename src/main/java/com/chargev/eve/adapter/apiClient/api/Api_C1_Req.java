package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString
public class Api_C1_Req {
    String mbrCardNo;
    String charge;  // current
    String chargeTime;
    String paymentType;
    String pay;
    String plugType;
}
