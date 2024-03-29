package ru.deturpant.forum.store.repositories;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.deturpant.forum.store.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
