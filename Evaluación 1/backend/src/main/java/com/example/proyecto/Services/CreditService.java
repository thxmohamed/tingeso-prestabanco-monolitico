package com.example.proyecto.Services;

import com.example.proyecto.Repositories.CreditRepository;
import com.example.proyecto.Entities.CreditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
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
}
