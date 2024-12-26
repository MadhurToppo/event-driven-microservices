package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class CardMobileRollbackedEvent {

    private String customerId;
    private Long accountNumber;
    private Long cardNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;

}
