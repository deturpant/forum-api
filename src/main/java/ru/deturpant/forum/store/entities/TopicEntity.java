package ru.deturpant.forum.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "topic")
public class TopicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "topic")
    @Builder.Default
    private List<MessageEntity> messages = new ArrayList<>();

    String name;


    @Builder.Default
    private Instant createdAt = Instant.now();
}
