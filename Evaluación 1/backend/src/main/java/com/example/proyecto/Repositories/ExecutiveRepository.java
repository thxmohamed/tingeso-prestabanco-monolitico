package com.example.proyecto.Repositories;

import com.example.proyecto.Entities.ExecutiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<ExecutiveEntity, Long> {
    public ExecutiveEntity findByEmail(String email);
}
