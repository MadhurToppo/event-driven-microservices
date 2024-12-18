package com.madhurtoppo.customer.command.event;

import lombok.Data;


@Data
public class CustomerDeletedEvent {

    private String customerId;
    private String isActive;

}
