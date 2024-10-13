package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.*;
import com.example.proyecto.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/")
    public ResponseEntity<List<CustomerEntity>> listUsers(){
        List<CustomerEntity> users = customerService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getUserById(@PathVariable Long id){
        CustomerEntity user = customerService.getCustomerById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerEntity customer){
        try {
            CustomerEntity userDB = customerService.getByEmail(customer.getEmail());
            if(userDB != null){
                return ResponseEntity.badRequest().body("Email already exists");
            }
            CustomerEntity createdUser = customerService.registerUser(customer);
            return ResponseEntity.ok().body("customer successfully registered");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error registering customer: " + e.getMessage());
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody CustomerEntity customer){
        CustomerEntity customerDB = customerService.login(customer);
        if (customerDB != null) {
            return ResponseEntity.ok().body("Successful login");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials");
        }
    }
}
