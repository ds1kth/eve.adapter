package com.chargev.eve.adapter.socketServer.me;

import org.springframework.stereotype.Component;

@Component
public class MeMessagePreProcessor {

    public String process(String cmd) {

        // 같은 동작을 하는 커맨드는 합친다.
        if(cmd.equals("R1"))
            cmd = "I1";

        return cmd;
    }
}
