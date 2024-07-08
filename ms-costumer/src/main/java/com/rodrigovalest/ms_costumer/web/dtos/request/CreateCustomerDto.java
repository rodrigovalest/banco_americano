package com.rodrigovalest.ms_costumer.web.dtos.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerDto {

    @NotEmpty(message = "cpf must not be empty")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "CPF must follow the pattern xxx.xxx.xxx-xx")
    private String cpf;

    @NotEmpty(message = "name must not be empty")
    @Size(min = 3, message = "name must have at least 3 characters")
    private String name;

    @NotEmpty(message = "gender must not be empty")
    @Pattern(regexp = "Masculino|Feminino|masculino|feminino|FEMININO|MASCULINO", message = "gender must be either Masculino or Feminino")
    private String gender;

    @NotEmpty(message = "birthdate must not be empty")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "birthdate must follow the pattern dd/MM/yyyy")
    private String birthdate;

    @Email(message = "email must be a valid email address")
    @NotEmpty(message = "email must not be empty")
    private String email;

    @NotEmpty(message = "photo must not be empty")
    private String photo;
}
