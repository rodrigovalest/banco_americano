package com.rodrigovalest.ms_costumer.services;

import com.rodrigovalest.ms_costumer.exceptions.CpfAlreadyRegisteredException;
import com.rodrigovalest.ms_costumer.exceptions.EmailAlreadyRegistedException;
import com.rodrigovalest.ms_costumer.exceptions.EntityNotFoundException;
import com.rodrigovalest.ms_costumer.exceptions.InvalidCpfException;
import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer create(Customer customer) {
        if (!CustomerService.validateCpf(customer.getCpf()))
            throw new InvalidCpfException("CPF is invalid");
        if (this.customerRepository.existsByCpf(customer.getCpf()))
            throw new CpfAlreadyRegisteredException("CPF already registered");
        if (this.customerRepository.existsByEmail(customer.getEmail()))
            throw new EmailAlreadyRegistedException("Email already registered");

        return this.customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("customer with id {" + id + "} not found")
        );
    }

    public static boolean validateCpf(String cpf) {
        if (cpf == null)
            return false;

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11)
            return false;

        if (cpf.matches("(\\d)\\1{10}"))
            return false;

        int sum = 0;
        for (int i = 0; i < 9; i++)
            sum += (cpf.charAt(i) - '0') * (10 - i);

        int firstVerifier = 11 - (sum % 11);
        if (firstVerifier >= 10)
            firstVerifier = 0;

        if (firstVerifier != (cpf.charAt(9) - '0'))
            return false;

        sum = 0;
        for (int i = 0; i < 10; i++)
            sum += (cpf.charAt(i) - '0') * (11 - i);

        int secondVerifier = 11 - (sum % 11);
        if (secondVerifier >= 10)
            secondVerifier = 0;

        return secondVerifier == (cpf.charAt(10) - '0');
    }
}
