package com.clickatell.racing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRiderDto {
    private Long id;
    @NotBlank(message = "please enter the name")
    private String name;
    @Email(message = "enter valid email")
    @NotBlank(message = "please enter the email")
    private String email;
}
