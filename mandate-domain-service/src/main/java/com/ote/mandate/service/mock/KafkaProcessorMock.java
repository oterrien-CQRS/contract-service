package com.ote.mandate.service.mock;

import com.ote.mandate.business.model.event.MandateCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Sink.class)
@Slf4j
public class KafkaProcessorMock {

    private final Sink sink;

    public KafkaProcessorMock(@Autowired Sink sink) {
        this.sink = sink;
    }

    @StreamListener(value = Sink.INPUT)
    public void consume(Message<MandateCreatedEvent> message) {
        log.warn("Received <-- " + message.getPayload().toString());
    }
}