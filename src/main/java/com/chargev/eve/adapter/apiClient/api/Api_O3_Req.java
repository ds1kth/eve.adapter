package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URI;
import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class Api_O3_Req {
//    private URI location;
    private String location;
    private Integer retries;
//    private Date retrieveDate;
    private String retrieveDate;
    private Integer retryInterval;
}
