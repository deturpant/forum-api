package ru.deturpant.forum.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.deturpant.forum.store.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
