package ru.deturpant.forum.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.deturpant.forum.store.entities.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
