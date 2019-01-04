package com.ote.mandate.service.mock;

import com.ote.framework.IEvent;
import com.ote.framework.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@EnableBinding(Sink.class)
@Slf4j
public class KafkaProcessorMock {

    private final Sink sink;

    public KafkaProcessorMock(@Autowired Sink sink) {
        this.sink = sink;
    }

    @StreamListener(value = Sink.INPUT)
    public void consume(Message<String> message) throws Exception {
        String messageContent = message.getPayload();
        Object eventType = message.getHeaders().get("EventType");
        if (eventType != null) {
            Class eventClass = Class.forName(eventType.toString());
            IEvent event = parse(messageContent, eventClass);
            log.warn("Received <-- " + Optional.ofNullable(event).map(Object::toString).orElse(null));
        } else {
            log.warn("Received <-- " + messageContent);
        }
    }

    @SuppressWarnings("unchecked")
    private IEvent parse(String jsonValue, Class eventClass) throws IOException {
        return JsonUtils.parse(jsonValue, (Class<IEvent>) eventClass);
    }

}