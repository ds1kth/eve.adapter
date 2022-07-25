package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Api_O1_Req {
    Integer connectorId;
    String idTag;    
    String chargingProfile;    
}