package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter @Setter @Builder
public class Api_F1_Req {
    private URI location;
}
