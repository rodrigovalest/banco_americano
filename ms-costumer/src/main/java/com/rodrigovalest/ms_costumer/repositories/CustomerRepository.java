package com.rodrigovalest.ms_costumer.repositories;

import com.rodrigovalest.ms_costumer.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
