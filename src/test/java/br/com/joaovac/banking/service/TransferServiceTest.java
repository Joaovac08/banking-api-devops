package br.com.joaovac.banking.service;

import br.com.joaovac.banking.domain.Account;
import br.com.joaovac.banking.exception.BusinessException;
import br.com.joaovac.banking.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    private Account source;
    private Account target;

    @BeforeEach
    void setUp() {
        source = new Account("João", new BigDecimal("1000.00"));
        source.setId(1L);

        target = new Account("Maria", new BigDecimal("500.00"));
        target.setId(2L);
    }

    @Test
    @DisplayName("Deve transferir com sucesso entre contas")
    void shouldTransferSuccessfully() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(target));

        transferService.transfer(1L, 2L, new BigDecimal("300.00"));

        assertEquals(new BigDecimal("700.00"), source.getBalance());
        assertEquals(new BigDecimal("800.00"), target.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando saldo for insuficiente")
    void shouldThrowWhenInsufficientBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(source));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(target));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> transferService.transfer(1L, 2L, new BigDecimal("5000.00")));

        assertEquals("Saldo insuficiente para transferência", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor for negativo ou zero")
    void shouldThrowWhenAmountIsInvalid() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.ZERO));

        assertTrue(ex.getMessage().contains("maior que zero"));
    }

    @Test
    @DisplayName("Deve lançar exceção quando origem e destino forem iguais")
    void shouldThrowWhenSameAccount() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> transferService.transfer(1L, 1L, new BigDecimal("100.00")));

        assertEquals("Não é possível transferir para a mesma conta", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção quando conta não existir")
    void shouldThrowWhenAccountNotFound() {
        when(accountRepository.findById(99L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> transferService.transfer(99L, 2L, new BigDecimal("100.00")));

        assertEquals("Conta de origem não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve criar conta com sucesso")
    void shouldCreateAccount() {
        Account saved = new Account("Carlos", new BigDecimal("100.00"));
        saved.setId(3L);

        when(accountRepository.save(any(Account.class))).thenReturn(saved);

        Account result = transferService.createAccount("Carlos", new BigDecimal("100.00"));

        assertNotNull(result);
        assertEquals("Carlos", result.getHolderName());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
    }
}
