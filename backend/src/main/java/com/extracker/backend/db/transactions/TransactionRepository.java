package com.extracker.backend.db.transactions;

import com.extracker.backend.db.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {
    List<TransactionEntity> findByUser(UserEntity user);
    List<TransactionEntity> findByUserAndDateGreaterThanEqual(UserEntity user, LocalDate startDate);
    List<TransactionEntity> findByUserAndDateLessThanEqual(UserEntity user, LocalDate endDate);
    List<TransactionEntity> findByUserAndDateBetween(UserEntity user, LocalDate startDate, LocalDate endDate);
}
