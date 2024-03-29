package ru.deturpant.forum.api.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.deturpant.forum.api.dto.MessageDto;
import ru.deturpant.forum.store.entities.MessageEntity;

@Component
public class MessageDtoFactory {

    @Autowired
    private UserDtoFactory userDtoFactory;

    public MessageDto makeMessageDto(MessageEntity entity) {
        return MessageDto.builder()
                .id(entity.getId())
                .owner(
                        userDtoFactory
                                .makeUserDto(entity.getOwner()
                                )
                )
                .createdAt(entity.getCreatedAt())
                .text(entity.getText())
                .build();
    }
}
