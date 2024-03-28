package ru.deturpant.forum.api.factories;

import org.springframework.stereotype.Component;
import ru.deturpant.forum.api.dto.TopicDto;
import ru.deturpant.forum.store.entities.TopicEntity;

@Component
public class TopicDtoFactory {
    public TopicDto makeTopicDto(TopicEntity entity) {
        return TopicDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
