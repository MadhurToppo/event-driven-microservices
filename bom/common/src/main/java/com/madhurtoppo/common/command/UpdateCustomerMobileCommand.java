package com.madhurtoppo.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class UpdateCustomerMobileCommand {

    @TargetAggregateIdentifier
    private final String customerId;
    private final Long accountNumber;
    private final Long loanNumber;
    private final Long cardNumber;
    private final String mobileNumber;
    private final String newMobileNumber;

}
