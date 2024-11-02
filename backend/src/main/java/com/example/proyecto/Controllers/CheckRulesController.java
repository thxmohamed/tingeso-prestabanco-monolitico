package com.example.proyecto.Controllers;

import com.example.proyecto.Entities.CheckRulesEntity;
import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Services.CheckRulesService;
import com.example.proyecto.Services.UserService;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkrules")
@CrossOrigin("*")
public class CheckRulesController {
    @Autowired
    CheckRulesService checkRulesService;
    @Autowired
    UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> createEvaluation(@RequestBody CheckRulesEntity checkRules){
        try {
            CheckRulesEntity check = checkRulesService.createEvaluation(checkRules);
            return ResponseEntity.ok(check);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating evaluation: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<CheckRulesEntity>> getAll(){
        List<CheckRulesEntity> list = checkRulesService.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckRulesEntity> getById(@PathVariable Long id){
        CheckRulesEntity check = checkRulesService.getById(id);
        return ResponseEntity.ok(check);
    }
    @PostMapping("/check1/{checkid}")
    public ResponseEntity<CheckRulesEntity> checkRelationQuotaIncome(@PathVariable Long checkid) {
        double income = userService.getUserById(checkRulesService.getById(checkid).getClientID()).getIncome();
        checkRulesService.checkRelationQuotaIncome(checkid, income);
        CheckRulesEntity result = checkRulesService.getById(checkid);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/check2/{checkid}/{request}")
    public ResponseEntity<CheckRulesEntity> checkCreditHistory(@PathVariable Long checkid, @PathVariable boolean request) {
        try {
            checkRulesService.checkCreditHistory(checkid, request);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/check3/{checkid}/{request}")
    public ResponseEntity<CheckRulesEntity> checkEmploymentStability(@PathVariable Long checkid, @PathVariable boolean request) {
        try {
            System.out.println("Llamando a checkEmploymentStability con checkid: " + checkid + " y request: " + request);
            checkRulesService.checkEmploymentStability(checkid, request);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            System.out.println("Resultado obtenido: " + result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error en check3: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/check4/{checkid}/{currentDebt}")
    public ResponseEntity<CheckRulesEntity> checkDebtIncome(@PathVariable Long checkid, @PathVariable double currentDebt) {
        try {
            checkRulesService.checkDebtIncome(checkid, currentDebt);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/check6/{checkid}")
    public ResponseEntity<CheckRulesEntity> checkApplicantAge(@PathVariable Long checkid) {
        try {
            checkRulesService.checkApplicantAge(checkid);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("check71/{checkid}/{check}")
    public ResponseEntity<CheckRulesEntity> checkMinimumBalance(@PathVariable Long checkid, @PathVariable boolean check) {
        try {
            checkRulesService.checkMinimumBalance(checkid, check);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("check72/{checkid}/{check}")
    public ResponseEntity<CheckRulesEntity> checkSavingHistory(@PathVariable Long checkid, @PathVariable boolean check) {
        try {
            checkRulesService.checkSavingHistory(checkid, check);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("check73/{checkid}/{check}")
    public ResponseEntity<CheckRulesEntity> checkPeriodicDeposits(@PathVariable Long checkid, @PathVariable boolean check) {
        try {
            checkRulesService.checkPeriodicDeposits(checkid, check);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("check74/{checkid}/{check}")
    public ResponseEntity<CheckRulesEntity> checkBalanceYearsAgo(@PathVariable Long checkid, @PathVariable boolean check) {
        try {
            checkRulesService.checkBalanceYearsAgo(checkid, check);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("check75/{checkid}/{check}")
    public ResponseEntity<CheckRulesEntity> checkRecentWithdrawals(@PathVariable Long checkid, @PathVariable boolean check) {
        try {
            checkRulesService.checkRecentWithdrawals(checkid, check);
            CheckRulesEntity result = checkRulesService.getById(checkid);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("credit/{id}")
    public ResponseEntity<CheckRulesEntity> getByCreditID(@PathVariable Long id){
        CheckRulesEntity result = checkRulesService.getByCreditID(id);
        return ResponseEntity.ok(result);
    }
}
