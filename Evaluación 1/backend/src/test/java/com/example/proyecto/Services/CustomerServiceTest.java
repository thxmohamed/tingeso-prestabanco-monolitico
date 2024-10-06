package com.example.proyecto.Services;
import com.example.proyecto.Entities.CustomerEntity;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomerServiceTest {
    CustomerService customerService = new CustomerService();
    CustomerEntity customerEntity = new CustomerEntity();

    /*
    @Test
    void whenSimulateCredit_thenCorrect(){
        //given
        float amount = 100000000;
        int years = 20*12;
        double interest = 0.00375;

        //when
        double M = customerService.creditSimulate(amount, interest, years);

        //then

        assertThat(Math.floor(M)).isEqualTo(632649);
    }

     */
}
