package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class CustomerMobileRollbackedEvent {

    private String customerId;
    private String mobileNumber;
    private String newMobileNumber;
    private String errorMsg;

}
