package com.madhurtoppo.loans.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class UpdateLoanCommand {

    @TargetAggregateIdentifier
    private final Long loanNumber;
    private final String mobileNumber;
    private final String loanType;
    private final int totalLoan;
    private final int amountPaid;
    private final int outstandingAmount;
    private final boolean activeSw;

}
