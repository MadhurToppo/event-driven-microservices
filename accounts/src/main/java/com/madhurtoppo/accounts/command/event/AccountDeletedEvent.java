package com.madhurtoppo.accounts.command.event;

import lombok.Data;


@Data
public class AccountDeletedEvent {

    private Long accountNumber;
    private boolean activeSw;

}
