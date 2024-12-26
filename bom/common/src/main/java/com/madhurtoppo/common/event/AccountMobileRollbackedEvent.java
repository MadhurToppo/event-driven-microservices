package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class AccountMobileRollbackedEvent {

    private String customerId;
    private Long accountNumber;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;

}
