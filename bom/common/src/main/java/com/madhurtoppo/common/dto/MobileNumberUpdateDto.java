package com.madhurtoppo.common.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
public class MobileNumberUpdateDto {

    @NotEmpty(message = "Customer ID cannot be empty")
    private String customerId;

    @NotEmpty(message = "Account number cannot be empty")
    private Long accountNumber;

    @NotEmpty(message = "Loan number cannot be empty")
    private Long loanNumber;

    @NotEmpty(message = "Card number cannot be empty")
    private Long cardNumber;

    @NotEmpty(message = "Current mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String currentMobileNumber;

    @NotEmpty(message = "New mobile number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String newMobileNumber;

}
