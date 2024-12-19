package com.madhurtoppo.loans.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class DeleteLoanCommand {

    @TargetAggregateIdentifier
    private final Long loanNumber;
    private final boolean activeSw;

}
