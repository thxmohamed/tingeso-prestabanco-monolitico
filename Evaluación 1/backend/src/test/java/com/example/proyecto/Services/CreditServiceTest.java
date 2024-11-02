package com.example.proyecto.Services;

import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Entities.UserEntity;
import com.example.proyecto.Repositories.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class CreditServiceTest {
    @Autowired
    CreditRepository creditRepository;
    @Autowired
    CreditService creditService;
    UserEntity user = new UserEntity();

    @BeforeEach
    void setup(){
        creditRepository.deleteAll();
    }

    @Test
    void whenGetCreditList_ThenCorrect(){
        CreditEntity credit1 = new CreditEntity();
        creditRepository.save(credit1);

        CreditEntity credit2 = new CreditEntity();
        creditRepository.save(credit2);

        ArrayList<CreditEntity> credits = creditService.getCredits();
        assertThat(credits).hasSize(2);
    }

    @Test
    void whenSaveCredit_ThenCorrect(){
        CreditEntity credit = new CreditEntity();
        credit.setObservations("This is a test");
        CreditEntity saved = creditService.saveCredit(credit);

        assertThat(saved.getObservations()).isEqualTo(credit.getObservations());
    }

    @Test
    void whenGetAllClientCredits_ThenCorrect(){
        user.setId(1L);
        CreditEntity credit1 = new CreditEntity();
        CreditEntity credit2 = new CreditEntity();
        credit1.setClientID(1L);
        credit2.setClientID(1L);

        creditRepository.save(credit1);
        creditRepository.save(credit2);

        ArrayList<CreditEntity> credits = creditService.getClientCredits(user.getId());
        assertThat(credits).hasSize(2);
    }

    @Test
    void whenGetMonthlyFee_ThenCorrect(){
        CreditEntity credit = new CreditEntity();
        credit.setInterestRate(2.3F);
        credit.setYearsLimit(4);
        credit.setRequestedAmount(5000);

        double monthlyFee = creditService.creditSimulate(credit);

        assertThat(monthlyFee).isBetween(109d, 110d);
    }

    @Test
    void whenGetByID_ThenCorrect(){
        CreditEntity credit = new CreditEntity();
        creditRepository.save(credit);
        Long id = credit.getId();

        CreditEntity found = creditService.findCreditByID(id);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test
    void whenDeleteCreditByID_ThenCreditNotFound(){
        CreditEntity credit = new CreditEntity();
        CreditEntity credit1 = new CreditEntity();

        creditRepository.save(credit1);
        creditRepository.save(credit);

        creditService.deleteCreditById(2L);

        Optional<CreditEntity> found = creditRepository.findById(2L);
        assertThat(found.isPresent()).isFalse();
    }

    @Test
    void whenUpdateStatus_ThenCorrect(){
        CreditEntity credit = new CreditEntity();
        credit.setStatus(CreditEntity.Status.valueOf
                ("E2_PENDIENTE_DOCUMENTACION"));
        creditRepository.save(credit);

        Long id = credit.getId();

        creditService.updateCreditStatus(id,
                CreditEntity.Status.E4_PRE_APROBADA);

        CreditEntity updated = creditRepository.findById(id).orElseThrow();

        assertThat(updated.getStatus()).isEqualTo(
                CreditEntity.Status.valueOf("E4_PRE_APROBADA"));
    }

    @Test
    void whenUpdateObservations_ThenCorrect(){
        CreditEntity credit = new CreditEntity();
        credit.setObservations("this is not updated");
        creditRepository.save(credit);

        Long id = credit.getId();

        creditService.updateObservations(id, "this is updated");

        CreditEntity updated = creditRepository.findById(id).orElseThrow();

        assertThat(updated.getObservations()).isEqualTo("this is updated");
    }

}
