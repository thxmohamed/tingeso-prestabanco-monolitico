package com.example.proyecto.Services;

import com.example.proyecto.Entities.CheckRulesEntity;
import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Entities.UserEntity;
import com.example.proyecto.Repositories.CheckRulesRepository;
import com.example.proyecto.Repositories.CreditRepository;
import com.example.proyecto.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CheckRulesService {
    @Autowired
    CheckRulesRepository checkRulesRepository;
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    UserRepository userRepository;

    public CheckRulesEntity createEvaluation(CheckRulesEntity checkRules){
        return checkRulesRepository.save(checkRules);
    }

    public List<CheckRulesEntity> getAll(){
        return checkRulesRepository.findAll();
    }

    public CheckRulesEntity getById(Long id){
        Optional<CheckRulesEntity> check = checkRulesRepository.findById(id);
        if(check.isPresent()){
            return check.get();
        }else{
            return null;
        }
    }

    public CheckRulesEntity getByCreditID(Long id){
        Optional<CheckRulesEntity> check = checkRulesRepository.findByCreditID(id);
        if(check.isPresent()){
            return check.get();
        }else{
            return null;
        }
    }
    @Transactional
    public void checkRelationQuotaIncome(Long id, double income) {
        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findByCreditID(id);

        if (checkRulesOpt.isPresent()) {
            CheckRulesEntity checkRules = checkRulesOpt.get();
            Long checkId = checkRules.getId();

            Optional<CreditEntity> creditOpt = creditRepository.findById(checkRules.getCreditID());

            if (creditOpt.isPresent()) {
                CreditEntity credit = creditOpt.get();

                // Calcular si la cuota mensual es <= 35% de los ingresos
                boolean result = (credit.getMonthlyFee() / income) * 100 <= 35;

                // Actualizar la regla 1 con el resultado de la validación
                checkRulesRepository.updateRule1(checkId, result);
            } else {
                throw new EntityNotFoundException("CreditEntity no encontrado con ID: " + checkRules.getCreditID());
            }
        } else {
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + id);
        }
    }

    @Transactional
    public void checkCreditHistory(Long id, boolean check){
        checkRulesRepository.updateRule2(id, check);
    }

    @Transactional
    public void checkEmploymentStability(Long id, boolean check){
        checkRulesRepository.updateRule3(id, check);
    }
    @Transactional
    public void checkDebtIncome(Long id, double currentDebt){
        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findById(id);
        if(checkRulesOpt.isPresent()){
            CheckRulesEntity checkRules = checkRulesOpt.get();
            CreditEntity credit = creditRepository.getById(checkRules.getCreditID());
            UserEntity user = userRepository.getById(checkRules.getClientID());
            double totalDebt = credit.getMonthlyFee() + currentDebt;
            boolean check = totalDebt/user.getIncome() <= 0.5;
            checkRulesRepository.updateRule4(id, check);
        }

    }

    @Transactional
    public void checkApplicantAge(Long checkid){
        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findById(checkid);
        if(checkRulesOpt.isPresent()){
            CheckRulesEntity checkRules = checkRulesOpt.get();
            Optional<CreditEntity> creditOpt = creditRepository.findById(checkRules.getCreditID());
            Optional<UserEntity> userOpt = userRepository.findById(checkRules.getClientID());
            if(creditOpt.isPresent() && userOpt.isPresent()){
                UserEntity user = userOpt.get();
                CreditEntity credit = creditOpt.get();
                int finalAge = credit.getYearsLimit() + user.getAge();
                boolean result = finalAge < 70;

                checkRulesRepository.updateRule6(checkid, result);
            }
        }
    }

    @Transactional
    public void checkMinimumBalance(Long checkid, boolean check){
        checkRulesRepository.updateRule71(checkid, check);
    }

    @Transactional
    public void checkSavingHistory(Long checkid, boolean check){
        checkRulesRepository.updateRule72(checkid, check);
    }

    @Transactional
    public void checkPeriodicDeposits(Long checkid, boolean check){
        checkRulesRepository.updateRule73(checkid, check);
    }

    @Transactional
    public void checkBalanceYearsAgo(Long checkid, boolean check){
        checkRulesRepository.updateRule74(checkid, check);
    }

    @Transactional
    public void checkRecentWithdrawals(Long checkid, boolean check){
        checkRulesRepository.updateRule75(checkid, check);
    }

}
