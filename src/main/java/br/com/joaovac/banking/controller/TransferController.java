package br.com.joaovac.banking.controller;

import br.com.joaovac.banking.domain.Account;
import br.com.joaovac.banking.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> body) {
        String holderName = (String) body.get("holderName");
        BigDecimal initialBalance = new BigDecimal(body.getOrDefault("initialBalance", "0").toString());

        Account account = transferService.createAccount(holderName, initialBalance);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.findAccount(id));
    }

    @PostMapping("/transfers")
    public ResponseEntity<Map<String, String>> transfer(@RequestBody Map<String, Object> body) {
        Long sourceId = Long.valueOf(body.get("sourceId").toString());
        Long targetId = Long.valueOf(body.get("targetId").toString());
        BigDecimal amount = new BigDecimal(body.get("amount").toString());

        transferService.transfer(sourceId, targetId, amount);

        return ResponseEntity.ok(Map.of("message", "Transferência realizada com sucesso"));
    }
}
