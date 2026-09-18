package br.com.joaovac.banking.service;

import br.com.joaovac.banking.domain.Account;
import br.com.joaovac.banking.exception.BusinessException;
import br.com.joaovac.banking.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(Long sourceId, Long targetId, BigDecimal amount) {
        // Validações de negócio
        if (sourceId == null || targetId == null) {
            throw new BusinessException("IDs de origem e destino são obrigatórios");
        }
        if (sourceId.equals(targetId)) {
            throw new BusinessException("Não é possível transferir para a mesma conta");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor da transferência deve ser maior que zero");
        }

        Account source = accountRepository.findById(sourceId)
                .orElseThrow(() -> new BusinessException("Conta de origem não encontrada"));

        Account target = accountRepository.findById(targetId)
                .orElseThrow(() -> new BusinessException("Conta de destino não encontrada"));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("Saldo insuficiente para transferência");
        }

        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(target);
    }

    public Account createAccount(String holderName, BigDecimal initialBalance) {
        if (holderName == null || holderName.isBlank()) {
            throw new BusinessException("Nome do titular é obrigatório");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo inicial não pode ser negativo");
        }
        Account account = new Account(holderName, initialBalance);
        return accountRepository.save(account);
    }

    public Account findAccount(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));
    }
}
