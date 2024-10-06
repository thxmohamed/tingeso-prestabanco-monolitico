package com.example.proyecto.Services;

import com.example.proyecto.Repositories.*;
import com.example.proyecto.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessLogicService {
    @Autowired
    CreditRepository creditRepository;

    public double creditSimulate(float amount, double yearInterest, int years){
        double interest = yearInterest/12;
        int months = years*12;
        return (amount*interest*Math.pow((1 + interest), months))/
                (Math.pow(1 + interest, months) - 1);
    }

    public double relationQuotaIncome(double monthlyFee, double income){
        return (monthlyFee/income)*100;
    }




}
