package ru.deturpant.forum.api.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.deturpant.forum.api.exceptions.NotFoundException;
import ru.deturpant.forum.api.exceptions.UnathorizedException;
import ru.deturpant.forum.store.entities.UserEntity;
import ru.deturpant.forum.store.repositories.UserRepository;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {
    @Autowired
    UserRepository userRepository;
    public void validateUserAuthentication(Long ownerId) {
        UserEntity user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            if (!auth.getName().equals(user.getUsername())) {
                throw new UnathorizedException("You are not authorized!");
            }
        } else {
            throw new UnathorizedException("You are not authorized!");
        }
    }

}
