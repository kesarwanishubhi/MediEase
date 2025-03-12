package com.shubhi.mediease.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

public record DoctorReq(
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
) {
}

