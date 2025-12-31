package com.jpmc.midascore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class TransactionRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "recipient_id")
  private UserRecord recipient;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private UserRecord sender;

  private BigDecimal transactionAmount;

  private BigDecimal transactionIncentive;

  public TransactionRecord() {}

  public TransactionRecord(UserRecord recipient, UserRecord sender, BigDecimal transactionAmount, BigDecimal transactionIncentive) {
    this.recipient = recipient;
    this.sender = sender;
    this.transactionAmount = transactionAmount;
    this.transactionIncentive = transactionIncentive;
  }

  public Long getId() {
    return id;
  }

  @Override
  public String toString() {
    return "TransactionRecord{" +
        "id=" + id +
        ", recipient=" + recipient +
        ", sender=" + sender +
        ", transactionAmount=" + transactionAmount +
        ", transactionIncentive=" + transactionIncentive +
        '}';
  }
}
