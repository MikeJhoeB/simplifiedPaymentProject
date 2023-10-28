package com.paymentProject.entities;

import com.paymentProject.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity(name = "transactions")
@Table(name = "transactions")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public BigDecimal value;
    public TransactionStatus status;
    @ManyToOne
    @JoinColumn(name = "sending_user_id")
    public User sendingUser;
    @ManyToOne
    @JoinColumn(name = "receiving_user_id")
    public User receivingUser;

}
