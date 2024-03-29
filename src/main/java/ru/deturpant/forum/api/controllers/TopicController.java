package ru.deturpant.forum.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.deturpant.forum.api.dto.TopicDto;
import ru.deturpant.forum.api.exceptions.NotFoundException;
import ru.deturpant.forum.api.exceptions.UnathorizedException;
import ru.deturpant.forum.api.factories.TopicDtoFactory;
import ru.deturpant.forum.api.requests.TopicRequest;
import ru.deturpant.forum.api.services.UserService;
import ru.deturpant.forum.store.entities.MessageEntity;
import ru.deturpant.forum.store.entities.TopicEntity;
import ru.deturpant.forum.store.entities.UserEntity;
import ru.deturpant.forum.store.repositories.MessageRepository;
import ru.deturpant.forum.store.repositories.TopicRepository;
import ru.deturpant.forum.store.repositories.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopicController {
    TopicDtoFactory topicDtoFactory;
    TopicRepository topicRepository;
    UserRepository userRepository;
    MessageRepository messageRepository;

    @Autowired
    UserService userService;
    static final String TOPICS = "/api/topics";

    @GetMapping(TOPICS)
    @Operation(summary = "Get all available topics", description = "Get all available topics")
    List<TopicDto> getTopics()
    {
        List<TopicEntity> topics = topicRepository.findAll();
        return topics.stream()
                .map(topicDtoFactory::makeTopicDto)
                .collect(Collectors.toList());
    }

    @PostMapping(TOPICS)
    @Operation(summary = "Create topic with message", description = "Create topic with message")
    @SecurityRequirement(name = "Bearer Authentication")
    TopicDto createTopic(
            @RequestBody TopicRequest topicRequest
            ) {
        Long owner_id = topicRequest.getOwner_id();
        String name = topicRequest.getName();
        String message = topicRequest.getMessage();
        UserEntity user = userRepository.findById(owner_id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userService.validateUserAuthentication(owner_id);
        TopicEntity topic = topicRepository.saveAndFlush(
                TopicEntity.builder()
                        .createdAt(Instant.now())
                        .name(name)
                .build()
        );
        MessageEntity messageEntity = MessageEntity.builder()
                .createdAt(Instant.now())
                .owner(user)
                .topic(topic)
                .text(message)
                .build();
        messageRepository.saveAndFlush(messageEntity);
        return topicDtoFactory.makeTopicDto(topic);
    }
}
