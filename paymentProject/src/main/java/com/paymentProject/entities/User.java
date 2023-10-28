package com.paymentProject.entities;

import com.paymentProject.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String firstName;
    public String lastName;
    public String document;
    public String email;
    public String password;
    @Enumerated(EnumType.STRING)
    public UserType userType;
}
