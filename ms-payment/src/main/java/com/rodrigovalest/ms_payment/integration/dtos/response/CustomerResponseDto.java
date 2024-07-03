package com.rodrigovalest.ms_payment.integration.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private String name;
    private String cpf;
    private String email;
    private String gender;
    private String birthdate;
    private Long points;
    private String photo;
}
