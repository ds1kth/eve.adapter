package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Api_K1_Req {
    String bytChargerId;
    String bytUserUID;
    String bytStartDate;
    String bytEndDate;
    String bytDBUniq;
    String bytResCan;
}
