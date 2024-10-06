package com.example.proyecto.Services;

import com.example.proyecto.Entities.ExecutiveEntity;
import com.example.proyecto.Repositories.ExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ExecutiveService {
    @Autowired
    ExecutiveRepository executiveRepository;
    public ArrayList<ExecutiveEntity> getUsers(){
        return (ArrayList<ExecutiveEntity>) executiveRepository.findAll();

    }

    public ExecutiveEntity getByEmail(String email){
        return executiveRepository.findByEmail(email);
    }

    public ExecutiveEntity registerExecutive(ExecutiveEntity user){
        return executiveRepository.save(user);
    }

    public ExecutiveEntity getExecutiveById(Long id){
        return executiveRepository.findById(id).get();
    }

    public ExecutiveEntity login(ExecutiveEntity user){
        ExecutiveEntity userDB = executiveRepository.findByEmail(user.getEmail());
        if(userDB != null){
            if(userDB.getPassword().equals(user.getPassword())){
                return userDB;
            }
        }
        return null;
    }
}