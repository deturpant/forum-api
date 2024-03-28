package ru.deturpant.forum.api.factories;

import org.springframework.stereotype.Component;
import ru.deturpant.forum.api.dto.UserDto;
import ru.deturpant.forum.store.entities.UserEntity;

@Component
public class UserDtoFactory {
    public UserDto makeUserDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .username(entity.getUsername())
                .build();
    }
}
