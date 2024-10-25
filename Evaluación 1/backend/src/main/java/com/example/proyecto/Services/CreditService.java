package com.example.proyecto.Services;

import com.example.proyecto.Entities.DocumentEntity;
import com.example.proyecto.Repositories.CreditRepository;
import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CreditService {
    @Autowired
    CreditRepository creditRepository;

    public ArrayList<CreditEntity> getCredits(){
        return (ArrayList<CreditEntity>) creditRepository.findAll();
    }

    public CreditEntity saveCredit(CreditEntity credit){
        return creditRepository.save(credit);
    }

    public ArrayList<CreditEntity> getClientCredits(Long id){
        return creditRepository.findByClientID(id);
    }

    public double creditSimulate(CreditEntity credit){
        double interest = credit.getInterestRate()/12/100;
        int months = credit.getYearsLimit()*12;
        float amount = credit.getRequestedAmount();
        return (amount*interest*Math.pow((1 + interest), months))/
                (Math.pow(1 + interest, months) - 1);
    }

    public Optional<CreditEntity> findCreditByID(Long id){
        return creditRepository.findById(id);
    }

    public void deleteCreditById(Long id){
        creditRepository.deleteById(id);
    }

    public void updateCreditStatus(Long id, CreditEntity.Status status) {
        creditRepository.updateCreditStatus(id, status);
    }





}
