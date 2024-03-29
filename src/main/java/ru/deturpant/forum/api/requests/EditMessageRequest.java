package ru.deturpant.forum.api.requests;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EditMessageRequest {
    String text;
    Long owner_id;
    Long message_id;
}
