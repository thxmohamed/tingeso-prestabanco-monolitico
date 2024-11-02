package com.example.proyecto.Services;

import com.example.proyecto.Repositories.UserRepository;
import com.example.proyecto.Entities.UserEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Data
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    public ArrayList<UserEntity> getUsers(){
        return (ArrayList<UserEntity>) userRepository.findAll();
    }

    public UserEntity getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public UserEntity registerUser(UserEntity user){
        return userRepository.save(user);
    }

    public UserEntity getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public UserEntity login(UserEntity user){
        UserEntity userDB = userRepository.findByEmail(user.getEmail());
        if(userDB != null){
            if(userDB.getPassword().equals(user.getPassword())){
                return userDB;
            }
        }
        return null;
    }

}
