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

    @Autowired
    private AWSService awsService;

    @Transactional
    public Customer create(Customer customer, String photo) {
        if (!CustomerService.validateCpf(customer.getCpf()))
            throw new InvalidCpfException("CPF is invalid");
        if (this.customerRepository.existsByCpf(customer.getCpf()))
            throw new CpfAlreadyRegisteredException("CPF already registered");
        if (this.customerRepository.existsByEmail(customer.getEmail()))
            throw new EmailAlreadyRegistedException("Email already registered");

        String photoUrl = this.awsService.upload(photo);
        customer.setUrlPhoto(photoUrl);

        return this.customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer findById(Long id) {
        return this.customerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("customer with id {" + id + "} not found")
        );
    }

    @Transactional
    public Customer update(Customer customer, Long id) {
        Customer persistedCustomer = this.findById(id);

        if (!CustomerService.validateCpf(customer.getCpf()))
            throw new InvalidCpfException("CPF is invalid");
        if (this.customerRepository.existsByCpf(customer.getCpf()) && !customer.getCpf().equals(persistedCustomer.getCpf()))
            throw new CpfAlreadyRegisteredException("CPF already registered");
        if (this.customerRepository.existsByEmail(customer.getEmail()) && !customer.getEmail().equals(persistedCustomer.getEmail()))
            throw new EmailAlreadyRegistedException("Email already registered");

        persistedCustomer.setName(customer.getName());
        persistedCustomer.setGender(customer.getGender());
        persistedCustomer.setEmail(customer.getEmail());
        persistedCustomer.setBirthdate(customer.getBirthdate());
        persistedCustomer.setCpf(customer.getCpf());

        return this.customerRepository.save(persistedCustomer);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!this.customerRepository.existsById(id))
            throw new EntityNotFoundException("Customer with id {" + id + "} not found");
        this.customerRepository.deleteById(id);
    }

    @Transactional
    public void addPointsByCustomerId(Long points, Long customerId) {
        Customer customer = this.findById(customerId);
        customer.setPoints(customer.getPoints() + points);
        this.customerRepository.save(customer);
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
