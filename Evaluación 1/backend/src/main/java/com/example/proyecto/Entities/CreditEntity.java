package com.example.proyecto.Entities;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Credit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long clientID;
    private int loanType;
    private float requestedAmount;
    private int yearsLimit;
    private int yearsMax;
    private float interestRate;
    private int status;
    private float monthlyFee;

}
