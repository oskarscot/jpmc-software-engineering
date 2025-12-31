package com.jpmc.midascore.service;

import com.jpmc.midascore.dto.BalanceResponse;
import com.jpmc.midascore.dto.IncentiveResponse;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

  private final UserRepository userRepository;
  private final TransactionRecordRepository transactionRecordRepository;
  private final RestTemplate restTemplate;

  public TransactionService(
      UserRepository userRepository,
      TransactionRecordRepository transactionRecordRepository, RestTemplate restTemplate
  ) {
    this.userRepository = userRepository;
    this.transactionRecordRepository = transactionRecordRepository;
    this.restTemplate = restTemplate;
  }

  public void createTransactionRecord(Transaction transaction) {
    UserRecord sender =
        this.userRepository
            .findById(transaction.getSenderId())
            .orElseThrow(() -> new IllegalArgumentException("Sender id not found"));
    UserRecord recipient =
        this.userRepository
            .findById(transaction.getRecipientId())
            .orElseThrow(() -> new IllegalArgumentException("Recipient id not found"));

    if (sender.getBalance() <= transaction.getAmount()) {
      return;
    }

    IncentiveResponse incentiveResponse = handleIncentive(transaction);

    sender.setBalance(sender.getBalance() - transaction.getAmount());
    recipient.setBalance(recipient.getBalance() + (transaction.getAmount() + incentiveResponse.amount().floatValue()));
    this.userRepository.save(sender);
    this.userRepository.save(recipient);

    TransactionRecord transactionRecord = new TransactionRecord(
        sender,
        recipient,
        new BigDecimal(transaction.getAmount()),
        incentiveResponse.amount()
    );

    this.transactionRecordRepository.save(transactionRecord);
  }

  private IncentiveResponse handleIncentive(Transaction transaction) {
    return this.restTemplate.postForObject(
        "http://localhost:8080/incentive", transaction, IncentiveResponse.class);
  }

  public BalanceResponse getBalance(Long userId) {
    Optional<UserRecord> user = this.userRepository.findById(userId);
    return user.map(userRecord -> new BalanceResponse(new BigDecimal(userRecord.getBalance())))
        .orElseGet(() -> new BalanceResponse(BigDecimal.ZERO));
  }
}
