package ru.deturpant.forum.api.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TopicRequest {
    String name;
    String message;
    Long owner_id;

}
