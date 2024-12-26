package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class CardMobileUpdatedEvent {

    private Long cardNumber;
    private Long loanNumber;
    private Long accountNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String customerId;

}
