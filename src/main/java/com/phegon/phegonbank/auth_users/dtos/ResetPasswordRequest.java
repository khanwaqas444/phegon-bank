package com.phegon.phegonbank.auth_users.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {

    //will be used to request for forget password
    private String email;

    //will be used to set new password
    private String code;
    private String newPassword;


}
