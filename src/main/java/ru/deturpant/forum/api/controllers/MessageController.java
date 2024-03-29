package ru.deturpant.forum.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.deturpant.forum.api.dto.MessageDto;
import ru.deturpant.forum.api.exceptions.NotFoundException;
import ru.deturpant.forum.api.factories.MessageDtoFactory;
import ru.deturpant.forum.api.requests.DelMessageRequest;
import ru.deturpant.forum.api.requests.EditMessageRequest;
import ru.deturpant.forum.api.requests.MessageRequest;
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

    static final String MESSAGE_IN_TOPIC = "/api/users/{user_id}/topics/{topic_id}/messages";
    static final String CREATE_MESSAGE = "/api/messages";
    static final String GET_MESSAGES = "/api/topics/{topic_id}/messages";

    static final String EDIT_MESSAGE = "/api/messages";
    static final String DEL_MESSAGE = "/api/messages";

    @PutMapping(EDIT_MESSAGE)
    MessageDto editMessage(
            @RequestBody EditMessageRequest editMessageRequest
            )
    {
        String text = editMessageRequest.getText();
        Long owner_id = editMessageRequest.getOwner_id();
        Long message_id = editMessageRequest.getMessage_id();
        MessageEntity messageEntity = messageRepository.findById(message_id)
                .orElseThrow(() -> new NotFoundException("Message not found"));
        if (text!= null) {
            messageEntity.setText(text);
        }
        messageEntity = messageRepository.saveAndFlush(messageEntity);
        return messageDtoFactory.makeMessageDto(messageEntity);
    }

    @DeleteMapping(DEL_MESSAGE)
    ResponseEntity<?> deleteMessage(
            @RequestBody DelMessageRequest delMessageRequest
    )
    {
        Long owner_id = delMessageRequest.getOwner_id();
        Long message_id = delMessageRequest.getMessage_id();
        MessageEntity messageEntity = messageRepository.findById(message_id)
                .orElseThrow(() -> new NotFoundException("Message not found"));
        messageRepository.delete(messageEntity);
        return ResponseEntity.ok().build();
    }

    @GetMapping(GET_MESSAGES)
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
    MessageDto createMessage(
            @RequestBody MessageRequest messageRequest

            ){
        Long topicId = messageRequest.getTopic_id();
        Long userId = messageRequest.getOwner_id();
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
