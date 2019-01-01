package com.ote.mandate.service.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class InnerEventDocument {

    @Field("event_id")
    @Indexed(direction = IndexDirection.ASCENDING, dropDups = true)
    private String id;
}
