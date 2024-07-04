package com.rodrigovalest.ms_costumer.services;

import com.rodrigovalest.ms_costumer.exceptions.*;
import com.rodrigovalest.ms_costumer.models.entities.Customer;
import com.rodrigovalest.ms_costumer.models.enums.GenderEnum;
import com.rodrigovalest.ms_costumer.repositories.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AWSService awsService;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void createCostumer_WithValidData_ReturnsCostumer() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        String photoUrl = "http://something.com";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.customerRepository.existsByCpf(customer.getCpf())).thenReturn(false);
        when(this.customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(this.customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(this.awsService.upload(base64Photo)).thenReturn(photoUrl);

        Customer sut = this.customerService.create(customer, base64Photo);

        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(customer.getId());
        Assertions.assertThat(sut.getCpf()).isEqualTo(customer.getCpf());
        Assertions.assertThat(sut.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(sut.getGender()).isEqualTo(customer.getGender());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(customer.getBirthdate());
        Assertions.assertThat(sut.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(sut.getPoints()).isEqualTo(customer.getPoints());
        Assertions.assertThat(sut.getUrlPhoto()).isEqualTo(photoUrl);

        verify(this.customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithAlreadyRegisteredEmail_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.customerRepository.existsByCpf(customer.getCpf())).thenReturn(false);
        when(this.customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(EmailAlreadyRegistedException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithAlreadyRegisteredCpf_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.customerRepository.existsByCpf(customer.getCpf())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(CpfAlreadyRegisteredException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithInvalidCpf_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "101.639.219-84", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(InvalidCpfException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithAWSErrorExceptionInUploadingFile_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.awsService.upload(base64Photo)).thenThrow(AWSErrorException.class);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(AWSErrorException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithFileConvertionExceptionInUploadingFile_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.awsService.upload(base64Photo)).thenThrow(FileConvertionException.class);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(FileConvertionException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void createCostumer_WithFileSizeExceptionInUploadingFile_ThrowsException() {
        String base64Photo = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9UAAAAC0lEQVR42mNkYAAAAAYAAjCB0C9U";
        Customer customer = new Customer(null, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, null);
        when(this.awsService.upload(base64Photo)).thenThrow(FileSizeException.class);

        Assertions.assertThatThrownBy(() -> this.customerService.create(customer, base64Photo)).isInstanceOf(FileSizeException.class);

        verify(this.customerRepository, times(0)).save(any(Customer.class));
    }

    @Test
    public void findById_WithValidId_ReturnsCostumer() {
        // Arrange
        Long id = 1L;
        Customer customer = new Customer(id, "101.639.219-84", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, "http://example.com/photo.jpg");
        when(this.customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        Customer sut = this.customerService.findById(id);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(customer.getId());
        Assertions.assertThat(sut.getCpf()).isEqualTo(customer.getCpf());
        Assertions.assertThat(sut.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(sut.getGender()).isEqualTo(customer.getGender());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(customer.getBirthdate());
        Assertions.assertThat(sut.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(sut.getPoints()).isEqualTo(customer.getPoints());
        Assertions.assertThat(sut.getUrlPhoto()).isEqualTo(customer.getUrlPhoto());

        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_WithInvalidId_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(this.customerRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Assertions.assertThatThrownBy(() -> this.customerService.findById(id)).isInstanceOf(EntityNotFoundException.class);

        // Assert
        verify(this.customerRepository, times(1)).findById(1L);
    }

    @Test
    public void updateCustomer_WithValidData_ReturnsUpdatedCustomer() {
        // Arrange
        Long id = 100L;
        Customer toUpdateCustomer = new Customer(null, "499.130.480-60", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        Customer persistedCustomer = new Customer(id, "499.130.480-60", "Tonio", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        Customer updatedCustomer = new Customer(id, "499.130.480-60", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");

        when(this.customerRepository.findById(id)).thenReturn(Optional.of(persistedCustomer));
        when(this.customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        // Act
        Customer sut = this.customerService.update(toUpdateCustomer, id);

        // Assert
        Assertions.assertThat(sut).isNotNull();
        Assertions.assertThat(sut.getId()).isEqualTo(id);
        Assertions.assertThat(sut.getCpf()).isEqualTo(toUpdateCustomer.getCpf());
        Assertions.assertThat(sut.getName()).isEqualTo(toUpdateCustomer.getName());
        Assertions.assertThat(sut.getEmail()).isEqualTo(toUpdateCustomer.getEmail());
        Assertions.assertThat(sut.getPoints()).isEqualTo(toUpdateCustomer.getPoints());
        Assertions.assertThat(sut.getBirthdate()).isEqualTo(toUpdateCustomer.getBirthdate());
        Assertions.assertThat(sut.getGender()).isEqualTo(toUpdateCustomer.getGender());

        verify(this.customerRepository, times(1)).findById(id);
        verify(this.customerRepository, times(1)).save(updatedCustomer);
    }

    @Test
    public void updateCustomer_WithInexistentId_ThrowsException() {
        Long id = 100L;
        Customer toUpdateCustomer = new Customer(null, "499.130.480-60", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        when(this.customerRepository.findById(id)).thenThrow(EntityNotFoundException.class);

        Assertions.assertThatThrownBy(() -> this.customerService.update(toUpdateCustomer, id)).isInstanceOf(EntityNotFoundException.class);

        verify(this.customerRepository, times(1)).findById(id);
    }

    @Test
    public void updateCustomer_WithInvalidCpf_ThrowsException() {
        Long id = 100L;
        Customer toUpdateCustomer = new Customer(null, "101.639.219-84", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        Customer persistedCustomer = new Customer(id, "499.130.480-60", "Tonio", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        when(this.customerRepository.findById(id)).thenReturn(Optional.of(persistedCustomer));

        Assertions.assertThatThrownBy(() -> this.customerService.update(toUpdateCustomer, id)).isInstanceOf(InvalidCpfException.class);

        verify(this.customerRepository, times(1)).findById(id);
    }

    @Test
    public void updateCustomer_WithAlreadyExistentEmail_ThrowsException() {
        Long id = 100L;
        Customer toUpdateCustomer = new Customer(null, "499.130.480-60", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "emailalreadyused@example.com", 100L, "http://example.com/photo.jpg");
        Customer persistedCustomer = new Customer(id, "499.130.480-60", "Tonio", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        when(this.customerRepository.findById(id)).thenReturn(Optional.of(persistedCustomer));
        when(this.customerRepository.existsByCpf(toUpdateCustomer.getCpf())).thenReturn(false);
        when(this.customerRepository.existsByEmail(toUpdateCustomer.getEmail())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> this.customerService.update(toUpdateCustomer, id)).isInstanceOf(EmailAlreadyRegistedException.class);

        verify(this.customerRepository, times(1)).findById(id);
    }

    @Test
    public void updateCustomer_WithAlreadyExistentCpf_ThrowsException() {
        Long id = 100L;
        Customer toUpdateCustomer = new Customer(null, "499.130.480-60", "Tonio Silveira", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "emailalreadyused@example.com", 100L, "http://example.com/photo.jpg");
        Customer persistedCustomer = new Customer(id, "212.280.439-49", "Tonio", GenderEnum.MALE, LocalDate.of(1990, 1, 1), "tonio@example.com", 100L, "http://example.com/photo.jpg");
        when(this.customerRepository.findById(id)).thenReturn(Optional.of(persistedCustomer));
        when(this.customerRepository.existsByCpf(toUpdateCustomer.getCpf())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> this.customerService.update(toUpdateCustomer, id)).isInstanceOf(CpfAlreadyRegisteredException.class);

        verify(this.customerRepository, times(1)).findById(id);
    }

    @Test
    public void deleteById_WithValidId_ThrowsException() {
        // Arrange
        Long id = 100L;
        when(this.customerRepository.existsById(id)).thenReturn(true);

        // Act
        this.customerService.deleteById(id);

        // Assert
        verify(this.customerRepository, times(1)).existsById(id);
        verify(this.customerRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteById_WithInvalidId_ThrowsException() {
        Long id = 100L;
        when(this.customerRepository.existsById(id)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> this.customerService.deleteById(id)).isInstanceOf(EntityNotFoundException.class);

        verify(this.customerRepository, times(1)).existsById(id);
        verify(this.customerRepository, times(0)).deleteById(id);
    }

    @Test
    public void validateCpf_WithValidCpf_ReturnTrue() {
        String validCpf1 = "212.280.439-49";
        String validCpf2 = "981.639.279-84";
        String validCpf3 = "98163927984";

        boolean sut1 = CustomerService.validateCpf(validCpf1);
        boolean sut2 = CustomerService.validateCpf(validCpf2);
        boolean sut3 = CustomerService.validateCpf(validCpf2);

        Assertions.assertThat(sut1).isEqualTo(true);
        Assertions.assertThat(sut2).isEqualTo(true);
        Assertions.assertThat(sut3).isEqualTo(true);
    }

    @Test
    public void validateCpf_WithInvalidCpf_ReturnTrue() {
        String invalidCpf1 = "981.639.219-84";
        String invalidCpf2 = "101.639.219-84";
        String invalidCpf3 = null;
        String invalidCpf4 = "ABCDE";

        boolean sut1 = CustomerService.validateCpf(invalidCpf1);
        boolean sut2 = CustomerService.validateCpf(invalidCpf2);
        boolean sut3 = CustomerService.validateCpf(invalidCpf3);
        boolean sut4 = CustomerService.validateCpf(invalidCpf4);

        Assertions.assertThat(sut1).isEqualTo(false);
        Assertions.assertThat(sut2).isEqualTo(false);
        Assertions.assertThat(sut3).isEqualTo(false);
        Assertions.assertThat(sut4).isEqualTo(false);
    }

    @Test
    public void addPointsByCustomerId_WithValidData_ReturnsVoid() {
        Long points = 100L;
        Long customerId = 1230L;
        Customer persistedCustomer = new Customer(customerId, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, "http://example.com/photo.jpg");
        Customer updatedCustomer = new Customer(customerId, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L + points, "http://example.com/photo.jpg");
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(persistedCustomer));

        this.customerService.addPointsByCustomerId(points, customerId);

        verify(this.customerRepository, times(1)).findById(customerId);
        verify(this.customerRepository, times(1)).save(updatedCustomer);
    }

    @Test
    public void addPointsByCustomerId_WithInexistentId_ThrowsException() {
        Long points = 100L;
        Long customerId = 1230L;
        Customer persistedCustomer = new Customer(customerId, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L, "http://example.com/photo.jpg");
        Customer updatedCustomer = new Customer(customerId, "499.130.480-60", "Maria", GenderEnum.FEMALE, LocalDate.of(1990, 1, 1), "maria@example.com", 100L + points, "http://example.com/photo.jpg");
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> this.customerService.addPointsByCustomerId(points, customerId)).isInstanceOf(EntityNotFoundException.class);

        verify(this.customerRepository, times(1)).findById(customerId);
        verify(this.customerRepository, times(0)).save(updatedCustomer);
    }
}
