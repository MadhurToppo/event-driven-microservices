package com.madhurtoppo.cards.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class UpdateCardCommand {

    @TargetAggregateIdentifier
    private final Long cardNumber;
    private final String mobileNumber;
    private final String cardType;
    private final int totalLimit;
    private final int amountUsed;
    private final int availableAmount;
    private final boolean activeSw;

}
