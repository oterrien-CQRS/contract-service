package com.ote.contract;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ServerRestController {

    @GetMapping("/test")
    public String home() {
        String message = "Message from server";
        log.warn(message);
        return message;
    }
}