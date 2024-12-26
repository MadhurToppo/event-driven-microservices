package com.madhurtoppo.accounts.command.aggregate;

import com.madhurtoppo.accounts.command.CreateAccountCommand;
import com.madhurtoppo.accounts.command.DeleteAccountCommand;
import com.madhurtoppo.accounts.command.UpdateAccountCommand;
import com.madhurtoppo.accounts.command.event.AccountCreatedEvent;
import com.madhurtoppo.accounts.command.event.AccountDeletedEvent;
import com.madhurtoppo.accounts.command.event.AccountUpdatedEvent;
import com.madhurtoppo.common.command.RollbackAccountMobileCommand;
import com.madhurtoppo.common.command.UpdateAccountMobileCommand;
import com.madhurtoppo.common.event.AccountDataChangedEvent;
import com.madhurtoppo.common.event.AccountMobileRollbackedEvent;
import com.madhurtoppo.common.event.AccountMobileUpdatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@Aggregate
public class AccountsAggregate {

    @AggregateIdentifier
    private Long accountNumber;
    private String mobileNumber;
    private String accountType;
    private String branchAddress;
    private boolean activeSw;
    private String errorMsg;


    public AccountsAggregate() {
    }


    @CommandHandler
    public AccountsAggregate(CreateAccountCommand createCommand) {
        AccountCreatedEvent accountCreatedEvent = new AccountCreatedEvent();
        BeanUtils.copyProperties(createCommand, accountCreatedEvent);
        AccountDataChangedEvent accountDataChangedEvent = new AccountDataChangedEvent();
        BeanUtils.copyProperties(createCommand, accountDataChangedEvent);
        AggregateLifecycle.apply(accountCreatedEvent)
                .andThen(() -> AggregateLifecycle.apply(accountDataChangedEvent));
    }


    @EventSourcingHandler
    public void on(AccountCreatedEvent accountCreatedEvent) {
        this.accountNumber = accountCreatedEvent.getAccountNumber();
        this.mobileNumber = accountCreatedEvent.getMobileNumber();
        this.accountType = accountCreatedEvent.getAccountType();
        this.branchAddress = accountCreatedEvent.getBranchAddress();
        this.activeSw = accountCreatedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateAccountCommand updateCommand) {
        AccountUpdatedEvent accountUpdatedEvent = new AccountUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, accountUpdatedEvent);
        AccountDataChangedEvent accountDataChangedEvent = new AccountDataChangedEvent();
        BeanUtils.copyProperties(updateCommand, accountDataChangedEvent);
        AggregateLifecycle.apply(accountUpdatedEvent);
        AggregateLifecycle.apply(accountDataChangedEvent);
    }


    @EventSourcingHandler
    public void on(AccountUpdatedEvent accountUpdatedEvent) {
        this.accountType = accountUpdatedEvent.getAccountType();
        this.branchAddress = accountUpdatedEvent.getBranchAddress();
    }


    @CommandHandler
    public void handle(DeleteAccountCommand deleteCommand) {
        AccountDeletedEvent accountDeletedEvent = new AccountDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, accountDeletedEvent);
        AggregateLifecycle.apply(accountDeletedEvent);
    }


    @EventSourcingHandler
    public void on(AccountDeletedEvent accountDeletedEvent) {
        this.activeSw = accountDeletedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateAccountMobileCommand updateAccountMobileCommand) {
        AccountMobileUpdatedEvent accountMobileUpdatedEvent = new AccountMobileUpdatedEvent();
        BeanUtils.copyProperties(updateAccountMobileCommand, accountMobileUpdatedEvent);
        AggregateLifecycle.apply(accountMobileUpdatedEvent);
    }


    @EventSourcingHandler
    public void on(AccountMobileUpdatedEvent accountMobileUpdatedEvent) {
        this.mobileNumber = accountMobileUpdatedEvent.getNewMobileNumber();
    }


    @CommandHandler
    public void handle(RollbackAccountMobileCommand rollbackAccountMobileCommand) {
        AccountMobileRollbackedEvent accountMobileRollbackedEvent = new AccountMobileRollbackedEvent();
        BeanUtils.copyProperties(rollbackAccountMobileCommand, accountMobileRollbackedEvent);
        AggregateLifecycle.apply(accountMobileRollbackedEvent);
    }


    @EventSourcingHandler
    public void on(AccountMobileRollbackedEvent accountMobileRollbackedEvent) {
        this.mobileNumber = accountMobileRollbackedEvent.getMobileNumber();
        this.errorMsg = accountMobileRollbackedEvent.getErrorMsg();
    }

}
