package com.chargev.eve.adapter.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Getter @Setter @ToString @Slf4j
public class MessageHandlerContext {
    private final Message message;
    //private Message respMessage;
    private final String serverUrl;
    //private Object payloadObject;
    //private ValidInfo validInfo = new ValidInfo(true, "00");
    //private Message response;


    public MessageHandlerContext(Message message, String serverUrl) {
        this.message = message;
        this.serverUrl = serverUrl;
    }

    static public ResponseEntity<String> sendRequest(Object body, String url, String cmd) {

        String serialized = null;

        if(body != null){
            log.info("["+ cmd + "][REQ] {}", body);
            try {
                serialized = new ObjectMapper().writeValueAsString(body);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3 * 1000);
        factory.setReadTimeout(3 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

        HttpEntity<String> request = new HttpEntity<>(serialized, headers);

//        ResponseEntity<String> response
//                = restTemplate.postForEntity(url, serialized, String.class);

        ResponseEntity<String> response = restTemplate.exchange(
                url, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                request, // {요청할 때 보낼 데이터}
                String.class); //{요청시 반환되는 데이터 타입

        return response;
    }

    public String makeUrl(String...in) {
        String out = serverUrl;
        for(String a:in)
            out += a;
        return out;
    }
}
