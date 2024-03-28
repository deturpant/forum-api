package ru.deturpant.forum.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private Instant createdAt = Instant.now();

    private String text;

    @ManyToOne
    @JoinColumn(name="owner_id")
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name="topic_id")
    private TopicEntity topic;
}
