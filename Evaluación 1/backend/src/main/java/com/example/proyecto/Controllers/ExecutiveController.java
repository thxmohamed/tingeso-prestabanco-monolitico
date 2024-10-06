package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.ExecutiveEntity;
import com.example.proyecto.Services.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/executive")
@CrossOrigin("*")
public class ExecutiveController {
    @Autowired
    ExecutiveService executiveService;

    @GetMapping("/")
    public ResponseEntity<List<ExecutiveEntity>> listUsers(){
        List<ExecutiveEntity> users = executiveService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecutiveEntity> getUserById(@PathVariable Long id){
        ExecutiveEntity user = executiveService.getExecutiveById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")

    public ResponseEntity<?> registerCustomer(@RequestBody ExecutiveEntity executive){
        try {
            ExecutiveEntity userDB = executiveService.getByEmail(executive.getEmail());
            if(userDB != null){
                return ResponseEntity.badRequest().body("Email already exists");
            }
            ExecutiveEntity createdUser = executiveService.registerExecutive(executive);
            return ResponseEntity.ok().body("customer successfully registered");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error registering customer: " + e.getMessage());
        }
    }
}
