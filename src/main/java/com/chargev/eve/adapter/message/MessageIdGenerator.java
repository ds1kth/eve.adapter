package com.chargev.eve.adapter.message;

import java.util.UUID;

public class MessageIdGenerator {

    private MessageIdGenerator() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}