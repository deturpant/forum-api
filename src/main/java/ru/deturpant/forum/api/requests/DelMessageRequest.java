package ru.deturpant.forum.api.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DelMessageRequest {
    Long owner_id;
    Long message_id;
}
