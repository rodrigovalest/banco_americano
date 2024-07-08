package com.rodrigovalest.ms_costumer.repositories;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void existsByCpf_WithValidData_ReturnsTrue() {
        Customer customer = new Customer(
                null,
                "499.130.480-60",
                "Maria",
                GenderEnum.FEMALE,
                LocalDate.of(1990, 1, 1),
                "maria@example.com",
                100L,
                "http://example.com/photo.jpg"
        );

        this.testEntityManager.persist(customer);

        boolean sut = this.customerRepository.existsByCpf(customer.getCpf());

        Assertions.assertThat(sut).isEqualTo(true);
    }

    @Test
    public void existsByCpf_WithValidData_ReturnsFalse() {
        boolean sut = this.customerRepository.existsByCpf("499.130.480-60");
        Assertions.assertThat(sut).isEqualTo(false);
    }

    @Test
    public void existsByEmail_WithValidData_ReturnsTrue() {
        Customer customer = new Customer(
                null,
                "499.130.480-60",
                "Maria",
                GenderEnum.FEMALE,
                LocalDate.of(1990, 1, 1),
                "maria@example.com",
                100L,
                "http://example.com/photo.jpg"
        );

        this.testEntityManager.persist(customer);

        boolean sut = this.customerRepository.existsByEmail(customer.getEmail());

        Assertions.assertThat(sut).isEqualTo(true);
    }

    @Test
    public void existsByEmail_WithValidData_ReturnsFalse() {
        boolean sut = this.customerRepository.existsByEmail("maria@example.com");
        Assertions.assertThat(sut).isEqualTo(false);
    }
}
