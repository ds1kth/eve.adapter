package com.chargev.eve.adapter.message;

public interface MessageHandler<T, R> {
    R serve(T payloadObject);
}