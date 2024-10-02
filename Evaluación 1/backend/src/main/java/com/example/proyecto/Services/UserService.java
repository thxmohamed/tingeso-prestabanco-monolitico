package com.example.proyecto.Services;

import com.example.proyecto.Repositories.UserRepository;
import com.example.proyecto.Entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public ArrayList<UserEntity> getUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    public UserEntity saveUser(UserEntity user){
        return userRepository.save(user);
    }
}