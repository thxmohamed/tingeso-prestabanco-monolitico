package com.example.proyecto.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class CustomerEntity extends UserEntity{
    private float salary;
    private int age;
}
