package br.com.joaovac.banking.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do titular é obrigatório")
    @Column(nullable = false)
    private String holderName;

    @PositiveOrZero(message = "Saldo não pode ser negativo")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    public Account() {
        this.balance = BigDecimal.ZERO;
    }

    public Account(String holderName, BigDecimal balance) {
        this.holderName = holderName;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
