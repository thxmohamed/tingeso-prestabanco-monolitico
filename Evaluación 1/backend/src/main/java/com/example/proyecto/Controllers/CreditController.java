package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Entities.DocumentEntity;
import com.example.proyecto.Services.CheckRulesService;
import com.example.proyecto.Services.CreditService;
import com.example.proyecto.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/credit")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;
    @Autowired
    DocumentService documentService;
    @GetMapping("/")
    public ResponseEntity<List<CreditEntity>> listCredits(){
        List<CreditEntity> credits = creditService.getCredits();
        return ResponseEntity.ok(credits);
    }

    @PostMapping("/save")
    public ResponseEntity<CreditEntity> saveCredit(@RequestBody CreditEntity credit){
        return ResponseEntity.ok(creditService.saveCredit(credit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<CreditEntity>> listCreditDocuments(@PathVariable Long id){
        List<CreditEntity> credits = creditService.getClientCredits(id);
        return ResponseEntity.ok(credits);
    }
    @PostMapping("/simulate")
    public ResponseEntity<Double> simulateCredit(@RequestBody CreditEntity credit) {
        double monthlyPayment = creditService.creditSimulate(credit);
        return ResponseEntity.ok(monthlyPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCreditById(@PathVariable Long id){
        Optional<CreditEntity> credit = creditService.findCreditByID(id);
        if(credit.isPresent()){
            creditService.deleteCreditById(id);
            if(documentService.creditHasDocuments(id)){
                documentService.deleteByCreditID(id);
            }
            return ResponseEntity.ok("Credit deleted successfully");
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateCreditStatus(@PathVariable Long id, @RequestBody CreditEntity.Status status) {
        creditService.updateCreditStatus(id, status);
        return ResponseEntity.ok().build();
    }
}
