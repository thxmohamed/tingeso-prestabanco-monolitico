package com.example.proyecto.Services;

import com.example.proyecto.Entities.CheckRulesEntity;
import com.example.proyecto.Entities.CreditEntity;
import com.example.proyecto.Entities.UserEntity;
import com.example.proyecto.Repositories.CheckRulesRepository;
import com.example.proyecto.Repositories.CreditRepository;
import com.example.proyecto.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class CheckRulesServiceTest {
    @Autowired
    private CheckRulesService checkRulesService;
    @Autowired
    private CheckRulesRepository checkRulesRepository;
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private UserRepository userRepository;
    UserEntity user = new UserEntity();
    CreditEntity credit = new CreditEntity();

    @BeforeEach
    void setup(){
        checkRulesRepository.deleteAll();
        creditRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenCreateRule_ThenOk(){
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(1L);
        checkRules.setCreditID(40L);
        CheckRulesEntity created = checkRulesService.createEvaluation(checkRules);
        assertThat(created.getCreditID()).isEqualTo(40L);
        assertThat(created.getClientID()).isEqualTo(1L);
    }

    @Test
    void whenCreateRuleWithIncompleteData_ThenThrowsException() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(null);

        assertThatThrownBy(() -> checkRulesService.createEvaluation(checkRules))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CreditID and ClientID cannot be null");
    }

    @Test
    void whenGetAllCheckRules_ThenOk(){
        CheckRulesEntity checkRules = new CheckRulesEntity();
        CheckRulesEntity checkRules1 = new CheckRulesEntity();
        checkRulesRepository.save(checkRules1);
        checkRulesRepository.save(checkRules);

        List<CheckRulesEntity> checkrules = checkRulesService.getAll();
        assertThat(checkrules).hasSize(2);
    }

    @Test
    void whenNoCheckRulesInDatabase_ThenGetAllReturnsEmptyList() {
        checkRulesRepository.deleteAll(); // Asegurarse de que la base de datos esté vacía
        List<CheckRulesEntity> checkrules = checkRulesService.getAll();
        assertThat(checkrules).isEmpty();
    }

    @Test
    void whenGetCheckRulesByID_ThenReturnCheckRules(){
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(55L);
        checkRulesRepository.save(checkRules);
        Long id = checkRules.getId();

        CheckRulesEntity found = checkRulesService.getById(id);
        assertThat(found.getCreditID()).isEqualTo(55L);
    }

    @Test
    void whenGetCheckRulesById_NotFound_ThenReturnNull() {
        Long nonExistentId = 999L;
        CheckRulesEntity found = checkRulesService.getById(nonExistentId);
        assertThat(found).isNull();
    }

    @Test
    void whenGetCheckRulesByCreditID_ThenReturnCheckRules(){
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(55L);
        checkRulesRepository.save(checkRules);
        Long id = checkRules.getId();

        CheckRulesEntity found = checkRulesService.getByCreditID(55L);
        assertThat(found.getId()).isEqualTo(id);
    }

    @Test
    void whenGetCheckRulesByCreditID_NotFound_ThenReturnNull() {
        Long nonExistentCreditId = 999L;
        CheckRulesEntity found = checkRulesService.getByCreditID(nonExistentCreditId);
        assertThat(found).isNull();
    }

    @Test
    void whenCheckRelationQuotaIncome_SuccessAndQuotaWithin35Percent() {

        CreditEntity credit = new CreditEntity();
        credit.setMonthlyFee(3000.0F);
        credit = creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkRelationQuotaIncome(checkRules.getId(), 10000.0);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule1()).isTrue();
    }

    @Test
    void whenCheckRelationQuotaIncome_SuccessAndQuotaExceeds35Percent() {
        // Crear entidades de prueba y guardar en la base de datos
        CreditEntity credit = new CreditEntity();
        credit.setMonthlyFee(4000.0F);
        credit = creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        // Ejecutar el método a probar
        checkRulesService.checkRelationQuotaIncome(checkRules.getId(), 10000.0); // Ingreso es 10,000, cuota 40%

        // Verificar el resultado en la base de datos
        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule1()).isFalse();
    }

    @Test
    void whenCheckRulesEntityNotFound_ThenThrowException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkRelationQuotaIncome(nonExistentCheckId, 10000.0))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenCreditEntityNotFound_ThenThrowException() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setCreditID(999L);
        checkRules = checkRulesRepository.save(checkRules);

        CheckRulesEntity finalCheckRules = checkRules;
        assertThatThrownBy(() -> checkRulesService.checkRelationQuotaIncome(finalCheckRules.getId(), 10000.0))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CreditEntity no encontrado con ID: " + checkRules.getCreditID());
    }

    @Test
    void whenCheckCreditHistory_WithTrueValue_ThenRule2IsUpdatedToTrue() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkCreditHistory(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule2()).isTrue();
    }

    @Test
    void whenCheckCreditHistory_WithFalseValue_ThenRule2IsUpdatedToFalse() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkCreditHistory(checkRules.getId(), false);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule2()).isFalse();
    }

    @Test
    void whenCheckEmploymentStability_WithTrueValue_ThenRule3IsUpdatedToTrue() {

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkEmploymentStability(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule3()).isTrue();
    }

    @Test
    void whenCheckEmploymentStability_WithFalseValue_ThenRule3IsUpdatedToFalse() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkEmploymentStability(checkRules.getId(), false);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule3()).isFalse();
    }

    @Test
    void whenCheckCreditHistory_WithNonExistentCheckId_ThenThrowException() {
        Long nonExistentCheckId = 999L;
        assertThatThrownBy(() -> checkRulesService.checkCreditHistory(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }
    @Test
    void whenCheckEmploymentStability_WithNonExistentCheckId_ThenThrowException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkEmploymentStability(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenDebtIncomeBelowThreshold_ThenRule4IsUpdatedToTrue() {

        user.setIncome(1000.0F);
        userRepository.save(user);

        credit.setMonthlyFee(200.0F);
        credit = creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(user.getId());
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        double currentDebt = 100.0;

        checkRulesService.checkDebtIncome(checkRules.getId(), currentDebt);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule4()).isTrue();
    }

    @Test
    void whenDebtIncomeAboveThreshold_ThenRule4IsUpdatedToFalse() {
        user.setIncome(1000.0F);
        userRepository.save(user);

        credit.setMonthlyFee(200.0F);
        credit = creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(user.getId());
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        double currentDebt = 400.0; // Total deuda = 600, ingreso = 1000, ratio = 0.6 > 0.5

        checkRulesService.checkDebtIncome(checkRules.getId(), currentDebt);

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule4()).isFalse();
    }

    @Test
    void whenCheckDebtIncome_WithNonExistentCheckId_ThenThrowException() {
        Long nonExistentCheckId = 999L;
        double currentDebt = 100.0;

        assertThatThrownBy(() -> checkRulesService.checkDebtIncome(nonExistentCheckId, currentDebt))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenApplicantAgeWithinLimit_ThenRule6IsUpdatedToTrue() {
        user.setAge(30);
        userRepository.save(user);

        credit.setYearsLimit(30);
        creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(user.getId());
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkApplicantAge(checkRules.getId());

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule6()).isTrue();
    }

    @Test
    void whenApplicantAgeExceedsLimit_ThenRule6IsUpdatedToFalse() {
        user.setAge(48);
        user = userRepository.save(user);

        credit.setYearsLimit(30);
        creditRepository.save(credit);

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(user.getId());
        checkRules.setCreditID(credit.getId());
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkApplicantAge(checkRules.getId());

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule6()).isFalse();
    }

    @Test
    void whenCheckApplicantAge_WithNonExistentID_ThenDoNothing(){
        Long nonExistentCheckId = 999L;

        checkRulesService.checkApplicantAge(nonExistentCheckId);

        Optional<CheckRulesEntity> checkRulesOpt = checkRulesRepository.findById(nonExistentCheckId);
        assertThat(checkRulesOpt).isEmpty();
    }

    @Test
    void whenCreditOrUserEntityNotFound_ThenDoNothing() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRules.setClientID(999L);
        checkRules.setCreditID(999L);
        checkRules = checkRulesRepository.save(checkRules);

        checkRulesService.checkApplicantAge(checkRules.getId());

        Optional<CheckRulesEntity> updatedCheckRulesOpt = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRulesOpt).isPresent();
        assertThat(updatedCheckRulesOpt.get().isRule6()).isFalse();
    }

    @Test
    void whenCheckMinimumBalanceWithExistingCheckId_ThenRule71IsUpdated() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRulesRepository.save(checkRules);
        checkRulesService.checkMinimumBalance(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRules = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRules).isPresent();
        assertThat(updatedCheckRules.get().isRule71()).isTrue();
    }

    @Test
    void whenCheckMinimumBalanceWithNonExistentCheckId_ThenThrowEntityNotFoundException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkMinimumBalance(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenCheckSavingHistoryWithExistingCheckId_ThenRule72IsUpdated() {

        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRulesRepository.save(checkRules);
        checkRulesService.checkSavingHistory(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRules = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRules).isPresent();
        assertThat(updatedCheckRules.get().isRule72()).isTrue();
    }

    @Test
    void whenCheckSavingHistoryWithNonExistentCheckId_ThenThrowEntityNotFoundException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkSavingHistory(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenCheckPeriodicDepositsWithExistingCheckId_ThenRule73IsUpdated() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRulesRepository.save(checkRules);

        checkRulesService.checkPeriodicDeposits(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRules = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRules).isPresent();
        assertThat(updatedCheckRules.get().isRule73()).isTrue();
    }

    @Test
    void whenCheckPeriodicDepositsWithNonExistentCheckId_ThenThrowEntityNotFoundException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkPeriodicDeposits(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenCheckBalanceYearsAgoWithExistingCheckId_ThenRule74IsUpdated() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRulesRepository.save(checkRules);

        checkRulesService.checkBalanceYearsAgo(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRules = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRules).isPresent();
        assertThat(updatedCheckRules.get().isRule74()).isTrue();
    }

    @Test
    void whenCheckBalanceYearsAgoWithNonExistentCheckId_ThenThrowEntityNotFoundException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkBalanceYearsAgo(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

    @Test
    void whenCheckRecentWithdrawalsWithExistingCheckId_ThenRule75IsUpdated() {
        CheckRulesEntity checkRules = new CheckRulesEntity();
        checkRulesRepository.save(checkRules);

        checkRulesService.checkRecentWithdrawals(checkRules.getId(), true);

        Optional<CheckRulesEntity> updatedCheckRules = checkRulesRepository.findById(checkRules.getId());
        assertThat(updatedCheckRules).isPresent();
        assertThat(updatedCheckRules.get().isRule75()).isTrue();
    }

    @Test
    void whenCheckRecentWithdrawalsWithNonExistentCheckId_ThenThrowEntityNotFoundException() {
        Long nonExistentCheckId = 999L;

        assertThatThrownBy(() -> checkRulesService.checkRecentWithdrawals(nonExistentCheckId, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CheckRulesEntity no encontrado con ID: " + nonExistentCheckId);
    }

}