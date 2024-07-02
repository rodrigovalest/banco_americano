package com.rodrigovalest.ms_payment.repositories;

import com.rodrigovalest.ms_payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByCustomerId(Long customerId);
}
