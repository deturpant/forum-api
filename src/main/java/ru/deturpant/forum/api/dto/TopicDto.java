package ru.deturpant.forum.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDto {
    Long id;

    @JsonProperty("created_at")
    Instant createdAt;

    String name;
}
