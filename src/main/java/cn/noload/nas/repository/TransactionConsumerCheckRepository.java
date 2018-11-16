package cn.noload.nas.repository;


import cn.noload.nas.domain.TransactionConsumerCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionConsumerCheckRepository extends JpaRepository<TransactionConsumerCheck, String> {

    Optional<TransactionConsumerCheck> findByTransactionId(String transactionId);
}
