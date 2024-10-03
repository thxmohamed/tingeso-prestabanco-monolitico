package com.example.proyecto.Repositories;

import com.example.proyecto.Entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    public CustomerEntity findByEmail(String email);
}
