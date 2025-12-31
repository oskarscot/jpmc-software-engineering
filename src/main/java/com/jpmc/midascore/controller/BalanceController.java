package com.jpmc.midascore.controller;

import com.jpmc.midascore.dto.BalanceResponse;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class BalanceController {

  private final TransactionService transactionService;

  public BalanceController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @GetMapping
  public ResponseEntity<BalanceResponse> get(@RequestParam Long userId) {
    return  ResponseEntity.ok(transactionService.getBalance(userId));
  }
}
