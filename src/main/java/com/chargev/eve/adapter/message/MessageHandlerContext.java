package com.chargev.eve.adapter.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Getter @Setter @ToString @Slf4j
public class MessageHandlerContext {
    private final Message message;    
    //private Message respMessage;
    private final String serverUrl;    
    //private String respMessage; // INS + ML + VD
    private RespMessage respMessage;
    //private Object payloadObject;
    //private ValidInfo validInfo = new ValidInfo(true, "00");
    //private Message response;


    public MessageHandlerContext(Message message, String serverUrl) {
        this.message = message;
        this.serverUrl = serverUrl;
    }

    /**
     *
     * @param body
     * @param url
     * @return
     */
    static private ResponseEntity<String> _sendRequest(Object body, String url) {
        log.info("[uri] : {}", url);

        String serialized = null;

        if(body != null){
            try {
                serialized = new ObjectMapper().writeValueAsString(body);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        log.info("[json] : {}", serialized);

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3 * 1000);
        factory.setReadTimeout(3 * 1000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
        headers.setBasicAuth("aW50ZXJuYWw6ZXZlaW50ZXJuYWw=");

        HttpEntity<String> request = new HttpEntity<>(serialized, headers);

//        ResponseEntity<String> response
//                = restTemplate.postForEntity(url, serialized, String.class);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, //{요청할 서버 주소}
                    HttpMethod.POST, //{요청할 방식}
                    request, // {요청할 때 보낼 데이터}
                    String.class); //{요청시 반환되는 데이터 타입
            return response;
        }        
        catch (Exception e){        
            log.error("[Exception] {} {}", e.getMessage());
        }

        return null;
    }

    /**
     *
     * @param body
     * @param url
     * @param cmd
     * @return
     */
    static public ResponseEntity<String> sendRequest(Object body, String url, String cmd) {
        log.info("[web("+ cmd + ")][REQ] Body : {}", body);

        return _sendRequest(body, url);
    }

    /**
     *
     * @param body
     * @param url
     * @param cmd
     * @param toCmd
     * @return
     */
    static public ResponseEntity<String> sendRequest(Object body, String url, String cmd, String toCmd) {
        if(toCmd != null) {
            log.info("[web("+ cmd + ") to " + toCmd + "][REQ] Body : {}", body);
        }
        else {
            log.info("[(web"+ cmd + ")][REQ] Body : {}", body);
        }

        return _sendRequest(body, url);
    }

    /**
     *
     * @param in
     * @return
     */
    public String makeUrl(String...in) {
        String out = serverUrl;
        for(String a:in)
            out += a;
        return out;
    }
}
