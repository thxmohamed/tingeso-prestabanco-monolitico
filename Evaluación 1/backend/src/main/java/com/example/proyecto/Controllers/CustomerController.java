package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.CustomerEntity;
import com.example.proyecto.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping("/")
    public ResponseEntity<List<CustomerEntity>> listCustomers(){
        List<CustomerEntity> customers = customerService.getUsers();
        return ResponseEntity.ok(customers);
    }
}
