package com.shubhi.mediease.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Admindetails(

        @JsonProperty("username")
        String username,

        @JsonProperty("email")
        String email,

        @JsonProperty("password")
        String password,

        @JsonProperty("phone")
        String phone,

        @JsonProperty("is_active")
        boolean isActive
) {

}
