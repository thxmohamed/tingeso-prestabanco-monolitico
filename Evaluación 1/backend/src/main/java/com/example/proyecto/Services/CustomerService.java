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

    public CustomerEntity getByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public CustomerEntity registerUser(CustomerEntity user){
        return customerRepository.save(user);
    }

    public CustomerEntity getCustomerById(Long id){
        return customerRepository.findById(id).get();
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
}