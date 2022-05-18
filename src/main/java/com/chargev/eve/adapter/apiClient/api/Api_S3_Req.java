package com.chargev.eve.adapter.apiClient.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Api_S3_Req {
    private String chargerId;
    private String deviceType;
    private String contentType;
    private String Version;
    private String fileName;
    private String filePath;
}
