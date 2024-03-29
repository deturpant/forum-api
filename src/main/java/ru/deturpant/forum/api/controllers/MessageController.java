package ru.deturpant.forum.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.deturpant.forum.api.dto.MessageDto;
import ru.deturpant.forum.api.exceptions.NotFoundException;
import ru.deturpant.forum.api.exceptions.UnathorizedException;
import ru.deturpant.forum.api.factories.MessageDtoFactory;
import ru.deturpant.forum.api.requests.DelMessageRequest;
import ru.deturpant.forum.api.requests.EditMessageRequest;
import ru.deturpant.forum.api.requests.MessageRequest;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class MessageController {
    MessageRepository messageRepository;
    MessageDtoFactory messageDtoFactory;
    TopicRepository topicRepository;
    UserRepository userRepository;

    @Autowired
    UserService userService;
    static final String MESSAGE_IN_TOPIC = "/api/users/{user_id}/topics/{topic_id}/messages";
    static final String CREATE_MESSAGE = "/api/messages";
    static final String GET_MESSAGES = "/api/topics/{topic_id}/messages";

    static final String EDIT_MESSAGE = "/api/messages";
    static final String DEL_MESSAGE = "/api/messages";

    @PutMapping(EDIT_MESSAGE)
    @Operation(summary = "Edit message", description = "Edit message")
    @SecurityRequirement(name = "Bearer Authentication")
    MessageDto editMessage(
            @RequestBody EditMessageRequest editMessageRequest
            )
    {
        String text = editMessageRequest.getText();
        Long owner_id = editMessageRequest.getOwner_id();
        Long message_id = editMessageRequest.getMessage_id();
        userService.validateUserAuthentication(owner_id);
        MessageEntity messageEntity = messageRepository.findById(message_id)
                .orElseThrow(() -> new NotFoundException("Message not found"));
        if (text!= null) {
            messageEntity.setText(text);
        }
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        return messageDtoFactory.makeMessageDto(messageEntity);
    }

    @DeleteMapping(DEL_MESSAGE)
    @Operation(summary = "Delete message", description = "Delete message")
    @SecurityRequirement(name = "Bearer Authentication")
    ResponseEntity<?> deleteMessage(
            @RequestBody DelMessageRequest delMessageRequest
    )
    {
        Long owner_id = delMessageRequest.getOwner_id();
        Long message_id = delMessageRequest.getMessage_id();
        userService.validateUserAuthentication(owner_id);
        MessageEntity messageEntity = messageRepository.findById(message_id)
                .orElseThrow(() -> new NotFoundException("Message not found"));
        messageRepository.delete(messageEntity);
        return ResponseEntity.ok().build();
    }

    @GetMapping(GET_MESSAGES)
    @Operation(summary = "Get all messages from topic", description = "Get all messages from topic")
    List<MessageDto> getMessages(
            @PathVariable("topic_id") Long topicId
    ) {
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));
        List<MessageEntity> messages = topic.getMessages();
        return messages.stream()
                .map(messageDtoFactory::makeMessageDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_MESSAGE)
    @Operation(summary = "Create message", description = "Create message")
    @SecurityRequirement(name = "Bearer Authentication")
    MessageDto createMessage(
            @RequestBody MessageRequest messageRequest

            ){
        Long topicId = messageRequest.getTopic_id();
        Long userId = messageRequest.getOwner_id();
        userService.validateUserAuthentication(userId);
        String message = messageRequest.getMessage();
        TopicEntity topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new NotFoundException("Topic not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        MessageEntity messageEntity = messageRepository.saveAndFlush(
                MessageEntity.builder()
                        .text(message)
                        .topic(topic)
                        .createdAt(Instant.now())
                        .owner(user)
                        .build()
        );
        return messageDtoFactory.makeMessageDto(messageEntity);
    }
}
