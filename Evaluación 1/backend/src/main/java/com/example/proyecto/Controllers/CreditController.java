package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;

    @GetMapping("/")
    public ResponseEntity<List<CreditEntity>> listCredits(){
        List<CreditEntity> credits = creditService.getCredits();
        return ResponseEntity.ok(credits);
    }

    @PostMapping("/save")
    public ResponseEntity<CreditEntity> saveCredit(@RequestBody CreditEntity credit){
        return ResponseEntity.ok(creditService.saveCredit(credit));
    }

}
