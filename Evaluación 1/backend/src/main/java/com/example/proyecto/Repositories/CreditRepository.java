package com.example.proyecto.Repositories;

import com.example.proyecto.Entities.CreditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity, Long>{
    public ArrayList<CreditEntity> findByLoanType(int type);
    public ArrayList<CreditEntity> findByClientID(Long clientID);
    public ArrayList<CreditEntity> findCreditEntitiesByYearsLimit(int yearsLimit);
}
