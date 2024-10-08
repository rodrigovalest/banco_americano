package com.rodrigovalest.ms_payment.services;

import com.rodrigovalest.ms_payment.exceptions.*;
import com.rodrigovalest.ms_payment.integration.clients.CalculateClient;
import com.rodrigovalest.ms_payment.integration.clients.CustomerClient;
import com.rodrigovalest.ms_payment.integration.dtos.rabbitmq.PointsQueueMessageDto;
import com.rodrigovalest.ms_payment.integration.dtos.request.CalculateRequestDto;
import com.rodrigovalest.ms_payment.integration.rabbitmq.PointsQueuePublisher;
import com.rodrigovalest.ms_payment.model.Payment;
import com.rodrigovalest.ms_payment.repositories.PaymentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CalculateClient calculateClient;
    private final CustomerClient customerClient;
    private final PointsQueuePublisher pointsQueuePublisher;

    @Transactional
    public Payment create(Payment payment) {
        try {
            this.customerClient.getCustomerById(payment.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new CustomerNotFoundException("customer with id {" + payment.getCustomerId() + "} not found");
        } catch (FeignException e) {
            throw new MicroserviceConnectionErrorException(e.getMessage());
        }

        Long points;
        try {
            points = this.calculateClient.calculate(
                    new CalculateRequestDto(payment.getTotal(), payment.getCategoryId())
            ).getPoints();
        } catch (FeignException.NotFound e) {
            throw new CategoryNotFoundException("category with id {" + payment.getCustomerId() + "} not found");
        } catch (FeignException e) {
            throw new MicroserviceConnectionErrorException(e.getMessage());
        }

        try {
            this.pointsQueuePublisher.sendPointsMessage(new PointsQueueMessageDto(payment.getCustomerId(), points));
        } catch (AmqpException e) {
            throw new RabbitMqMessagingException("Failed to send points message: " + e.getMessage());
        }

        return this.paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment findById(UUID id) {
        return this.paymentRepository.findById(id).orElseThrow(
                () -> new PaymentNotFoundException("payment with id {" + id + "} not found")
        );
    }

    @Transactional(readOnly = true)
    public List<Payment> findByCustomerId(Long customerId) {
        try {
            this.customerClient.getCustomerById(customerId);
        } catch (FeignException.NotFound e) {
            throw new CustomerNotFoundException("customer with id {" + customerId + "} not found");
        } catch (FeignException e) {
            throw new MicroserviceConnectionErrorException(e.getMessage());
        }

        return this.paymentRepository.findByCustomerId(customerId);
    }
}
