package com.madhurtoppo.customer.query.projection;

import com.madhurtoppo.common.event.CustomerMobileRollbackedEvent;
import com.madhurtoppo.common.event.CustomerMobileUpdatedEvent;
import com.madhurtoppo.customer.command.event.CustomerCreatedEvent;
import com.madhurtoppo.customer.command.event.CustomerDeletedEvent;
import com.madhurtoppo.customer.command.event.CustomerUpdatedEvent;
import com.madhurtoppo.customer.entity.Customer;
import com.madhurtoppo.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class CustomerProjection {

    private final ICustomerService iCustomerService;


    @EventHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        Customer customerEntity = new Customer();
        BeanUtils.copyProperties(customerCreatedEvent, customerEntity);
        iCustomerService.createCustomer(customerEntity);
    }


    @EventHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        // throw new RuntimeException("It is a bad day");
        iCustomerService.updateCustomer(customerUpdatedEvent);
    }


    @EventHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        iCustomerService.deleteCustomer(customerDeletedEvent.getCustomerId());
    }


    @EventHandler
    public void on(CustomerMobileUpdatedEvent customerMobileUpdatedEvent) {
        iCustomerService.updateMobileNumber(customerMobileUpdatedEvent.getMobileNumber(), customerMobileUpdatedEvent.getNewMobileNumber());
    }


    @EventHandler
    public void on(CustomerMobileRollbackedEvent customerMobileRollbackedEvent) {
        iCustomerService.updateMobileNumber(customerMobileRollbackedEvent.getNewMobileNumber(),
                customerMobileRollbackedEvent.getMobileNumber());
    }

}
