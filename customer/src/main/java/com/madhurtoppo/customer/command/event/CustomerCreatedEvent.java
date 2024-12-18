package com.madhurtoppo.customer.command.event;

import lombok.Data;


@Data
public class CustomerCreatedEvent {
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
}
