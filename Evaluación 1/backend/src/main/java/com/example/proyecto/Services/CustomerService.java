package com.example.proyecto.Services;

import com.example.proyecto.Repositories.CustomerRepository;
import com.example.proyecto.Entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    public ArrayList<CustomerEntity> getUsers(){
        return (ArrayList<CustomerEntity>) customerRepository.findAll();

    }

    public CustomerEntity registerUser(CustomerEntity user){
        return customerRepository.save(user);
    }

    public CustomerEntity login(CustomerEntity user){
        CustomerEntity userDB = customerRepository.findByEmail(user.getEmail());
        if(userDB != null){
            if(userDB.getPassword().equals(user.getPassword())){
                return userDB;
            }
        }
        return null;
    }

    public double creditSimulate(float amount, double interest, int years){
        return (amount*interest*Math.pow((1 + interest), years))/
                (Math.pow(1 + interest, years) - 1);
    }
}