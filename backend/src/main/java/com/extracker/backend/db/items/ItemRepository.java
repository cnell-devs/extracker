package com.extracker.backend.db.items;

import com.extracker.backend.db.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, String> {
    List<ItemEntity> findByUser(UserEntity user);
}
