package ru.deturpant.forum.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.deturpant.forum.store.entities.TopicEntity;

public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
}
