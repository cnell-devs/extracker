package com.extracker.backend.db.transactions;

import com.extracker.backend.db.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByUser(UserEntity user);
}