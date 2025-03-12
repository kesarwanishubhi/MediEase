package com.shubhi.mediease.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorUpdate {
    private String specialization;
    private Integer experienceYears;
    private String qualification;
    private Double consultationFee;
    private String hospitalAddress;
    private String schedule;
    private String password; // Optional: Only if updating password
}
