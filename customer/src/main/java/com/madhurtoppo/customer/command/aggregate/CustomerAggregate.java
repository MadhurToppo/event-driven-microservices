package com.madhurtoppo.customer.command.aggregate;

import com.madhurtoppo.common.command.RollbackCustomerMobileCommand;
import com.madhurtoppo.common.command.UpdateCustomerMobileCommand;
import com.madhurtoppo.common.event.CustomerMobileRollbackedEvent;
import com.madhurtoppo.common.event.CustomerMobileUpdatedEvent;
import com.madhurtoppo.customer.command.CreateCustomerCommand;
import com.madhurtoppo.customer.command.DeleteCustomerCommand;
import com.madhurtoppo.customer.command.UpdateCustomerCommand;
import com.madhurtoppo.customer.command.event.CustomerCreatedEvent;
import com.madhurtoppo.customer.command.event.CustomerDeletedEvent;
import com.madhurtoppo.customer.command.event.CustomerUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@Aggregate
public class CustomerAggregate {

    @AggregateIdentifier
    private String customerId;
    private String name;
    private String email;
    private String mobileNumber;
    private boolean activeSw;
    private String errorMsg;


    public CustomerAggregate() {
    }


    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand createCustomerCommand) {
        CustomerCreatedEvent customerCreatedEvent = new CustomerCreatedEvent();
        BeanUtils.copyProperties(createCustomerCommand, customerCreatedEvent);
        AggregateLifecycle.apply(customerCreatedEvent);
    }


    @EventSourcingHandler
    public void on(CustomerCreatedEvent customerCreatedEvent) {
        this.customerId = customerCreatedEvent.getCustomerId();
        this.name = customerCreatedEvent.getName();
        this.email = customerCreatedEvent.getEmail();
        this.mobileNumber = customerCreatedEvent.getMobileNumber();
        this.activeSw = customerCreatedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateCustomerCommand updateCustomerCommand) {
        CustomerUpdatedEvent customerUpdatedEvent = new CustomerUpdatedEvent();
        BeanUtils.copyProperties(updateCustomerCommand, customerUpdatedEvent);
        AggregateLifecycle.apply(customerUpdatedEvent);
    }


    @EventSourcingHandler
    public void on(CustomerUpdatedEvent customerUpdatedEvent) {
        this.name = customerUpdatedEvent.getName();
        this.email = customerUpdatedEvent.getEmail();
    }


    @CommandHandler
    public void handle(DeleteCustomerCommand deleteCustomerCommand) {
        CustomerDeletedEvent customerDeletedEvent = new CustomerDeletedEvent();
        BeanUtils.copyProperties(deleteCustomerCommand, customerDeletedEvent);
        AggregateLifecycle.apply(customerDeletedEvent);
    }


    @EventSourcingHandler
    public void on(CustomerDeletedEvent customerDeletedEvent) {
        this.activeSw = customerDeletedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateCustomerMobileCommand updateCustomerMobileCommand) {
        CustomerMobileUpdatedEvent customerMobileUpdatedEvent = new CustomerMobileUpdatedEvent();
        BeanUtils.copyProperties(updateCustomerMobileCommand, customerMobileUpdatedEvent);
        AggregateLifecycle.apply(customerMobileUpdatedEvent);
    }


    @EventSourcingHandler
    public void on(CustomerMobileUpdatedEvent customerUpdatedEvent) {
        this.mobileNumber = customerUpdatedEvent.getNewMobileNumber();
    }


    @CommandHandler
    public void handle(RollbackCustomerMobileCommand rollbackCustomerMobileCommand) {
        CustomerMobileRollbackedEvent customerMobileRollbackedEvent = new CustomerMobileRollbackedEvent();
        BeanUtils.copyProperties(rollbackCustomerMobileCommand, customerMobileRollbackedEvent);
        AggregateLifecycle.apply(customerMobileRollbackedEvent);
    }


    @EventSourcingHandler
    public void on(CustomerMobileRollbackedEvent customerMobileRollbackedEvent) {
        this.mobileNumber = customerMobileRollbackedEvent.getMobileNumber();
        this.errorMsg = customerMobileRollbackedEvent.getErrorMsg();
    }

}
