package com.madhurtoppo.cards.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;


@Data
@Builder
public class DeleteCardCommand {

    @TargetAggregateIdentifier
    private final Long cardNumber;
    private final boolean activeSw;

}
