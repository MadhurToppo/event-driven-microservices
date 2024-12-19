package com.madhurtoppo.accounts.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class DeleteAccountCommand {

    @TargetAggregateIdentifier
    private final Long accountNumber;
    private final boolean activeSw;

}
