package com.madhurtoppo.gatewayserver.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class AccountsDto {

    @NotEmpty(message = "Account number is required")
    @Pattern(regexp = "^$|[0-9]{10}", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Pattern(regexp = "^[0-9]{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotEmpty(message = "Account type is required")
    private String accountType;

    @NotEmpty(message = "Branch name is required")
    private String branchAddress;

    private boolean isActive;

}
