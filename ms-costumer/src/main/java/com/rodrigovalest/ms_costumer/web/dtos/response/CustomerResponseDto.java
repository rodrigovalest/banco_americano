package com.rodrigovalest.ms_costumer.web.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
