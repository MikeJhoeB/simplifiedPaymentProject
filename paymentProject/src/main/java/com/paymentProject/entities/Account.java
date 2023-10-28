package com.paymentProject.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "account")
@Table(name = "accounts")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;
    public BigDecimal balance;
}
