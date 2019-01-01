package com.ote.mandate.service.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
@NoArgsConstructor
public final class EventDocument<T extends InnerEventDocument> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdTime;

    private T event = null;
}
