package com.madhurtoppo.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class MobileNumberUpdateDto {

    @NotEmpty(message = "Current Mobile Number can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Current Mobile Number must be 10 digits")
    private String currentMobileNumber;

    @NotEmpty(message = "New Mobile Number can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "New Mobile Number must be 10 digits")
    private String newMobileNumber;

}
