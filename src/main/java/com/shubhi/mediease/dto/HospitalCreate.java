package com.shubhi.mediease.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalCreate {

    @NotBlank(message = "Hospital name cannot be empty!")
    @Size(min = 3, max = 100, message = "Hospital name must be between 3 and 100 characters!")
    private String name;

    @NotBlank(message = "Address cannot be empty!")

    private String address;

    @NotBlank(message = "Phone number is required!")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits!")
    private String phoneNumber;

    @NotBlank(message = "Email is required!")
    @Email(message = "Invalid email format!")
    private String email;


}
