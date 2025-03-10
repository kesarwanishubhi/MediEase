package com.shubhi.mediease.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

public record DoctorResponse(
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
) {
}

