package com.madhurtoppo.cards.command.event;

import lombok.Data;


@Data
public class CardDeletedEvent {

    private Long cardNumber;
    private boolean activeSw;

}
