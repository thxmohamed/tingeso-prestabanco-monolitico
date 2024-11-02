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

    public CheckRulesEntity createEvaluation(CheckRulesEntity checkRules) {
        if (checkRules.getCreditID() == null || checkRules.getClientID() == null) {
            throw new IllegalArgumentException("CreditID and ClientID cannot be null");
        }
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
    public void checkRelationQuotaIncome(Long checkid, double income) {
        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findById(checkid);
        if (checkRulesOpt.isPresent()) {
            CheckRulesEntity checkRules = checkRulesOpt.get();
            Optional<CreditEntity> creditOpt = creditRepository.findById(checkRules.getCreditID());

            if (creditOpt.isPresent()) {
                CreditEntity credit = creditOpt.get();

                boolean result = (credit.getMonthlyFee() / income) * 100 <= 35;
                checkRulesRepository.updateRule1(checkid, result);
            } else {
                throw new EntityNotFoundException("CreditEntity no encontrado con ID: " + checkRules.getCreditID());
            }
        } else {
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkCreditHistory(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule2(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkEmploymentStability(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule3(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }
    @Transactional
    public void checkDebtIncome(Long checkid, double currentDebt){
        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findById(checkid);
        if(checkRulesOpt.isPresent()){
            CheckRulesEntity checkRules = checkRulesOpt.get();
            CreditEntity credit = creditRepository.getById(checkRules.getCreditID());
            UserEntity user = userRepository.getById(checkRules.getClientID());
            double totalDebt = credit.getMonthlyFee() + currentDebt;
            boolean check = totalDebt/user.getIncome() <= 0.5;
            checkRulesRepository.updateRule4(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
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
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule71(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkSavingHistory(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule72(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkPeriodicDeposits(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule73(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkBalanceYearsAgo(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule74(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }

    @Transactional
    public void checkRecentWithdrawals(Long checkid, boolean check){
        Optional<CheckRulesEntity> checkRules = checkRulesRepository.findById(checkid);
        if(checkRules.isPresent()){
            checkRulesRepository.updateRule75(checkid, check);
        }else{
            throw new EntityNotFoundException("CheckRulesEntity no encontrado con ID: " + checkid);
        }
    }
}
