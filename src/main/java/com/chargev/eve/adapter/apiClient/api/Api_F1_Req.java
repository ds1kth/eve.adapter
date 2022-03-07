package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.URI;

@Getter @Setter @Builder @ToString
public class Api_F1_Req {
    private URI location;
}
