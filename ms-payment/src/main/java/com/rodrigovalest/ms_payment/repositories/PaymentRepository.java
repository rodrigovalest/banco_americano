package com.rodrigovalest.ms_payment.repositories;

import com.rodrigovalest.ms_payment.model.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
