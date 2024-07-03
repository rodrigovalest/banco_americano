package com.rodrigovalest.ms_payment.repositories;

import com.rodrigovalest.ms_payment.model.Payment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

@DataJpaTest
public class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void findByUserId_WithValidId_ReturnListOfPayment() {
        // Arrange
        Long customerId = 100L;
        Payment payment1 = new Payment(null, customerId, 3L, 1000L, null);
        Payment payment2 = new Payment(null, customerId, 1L, 10L, null);
        Payment payment3 = new Payment(null, customerId, 6L, 2000L, null);
        Payment payment4 = new Payment(null, customerId, 7L, 400L, null);

        this.testEntityManager.persist(payment1);
        this.testEntityManager.persist(payment2);
        this.testEntityManager.persist(payment3);
        this.testEntityManager.persist(payment4);

        // Act
        List<Payment> sut = this.paymentRepository.findByCustomerId(customerId);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.size()).isEqualTo(4);
    }

    @Test
    public void findByCustomerId_WithCustomerIdHavingNoPayments_ReturnsEmptyList() {
        // Arrange
        Long customerId = 100L;
        // Ensure no payments for this customer
        this.testEntityManager.persist(new Payment(null, 101L, 1L, 1000L, null)); // Different customer

        // Act
        List<Payment> sut = this.paymentRepository.findByCustomerId(customerId);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut).isEmpty();
    }

}
