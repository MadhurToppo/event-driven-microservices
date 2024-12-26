package com.madhurtoppo.accounts.query.projection;

import com.madhurtoppo.accounts.command.event.AccountCreatedEvent;
import com.madhurtoppo.accounts.command.event.AccountDeletedEvent;
import com.madhurtoppo.accounts.command.event.AccountUpdatedEvent;
import com.madhurtoppo.accounts.entity.Accounts;
import com.madhurtoppo.accounts.service.IAccountsService;
import com.madhurtoppo.common.event.AccountMobileRollbackedEvent;
import com.madhurtoppo.common.event.AccountMobileUpdatedEvent;
import com.madhurtoppo.common.event.CustomerMobileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ProcessingGroup("account-group")
public class AccountProjection {

    private final IAccountsService iAccountsService;


    @EventHandler
    public void on(AccountCreatedEvent event) {
        Accounts accountEntity = new Accounts();
        BeanUtils.copyProperties(event, accountEntity);
        iAccountsService.createAccount(accountEntity);
    }


    @EventHandler
    public void on(AccountUpdatedEvent event) {
        iAccountsService.updateAccount(event);
    }


    @EventHandler
    public void on(AccountDeletedEvent event) {
        iAccountsService.deleteAccount(event.getAccountNumber());
    }


    @EventHandler
    public void on(AccountMobileUpdatedEvent accountMobileUpdatedEvent) {
        iAccountsService.updateMobileNumber(accountMobileUpdatedEvent.getMobileNumber(), accountMobileUpdatedEvent.getNewMobileNumber());
    }


    @EventHandler
    public void on(AccountMobileRollbackedEvent accountMobileRollbackedEvent) {
        iAccountsService.updateMobileNumber(accountMobileRollbackedEvent.getNewMobileNumber(),
                accountMobileRollbackedEvent.getMobileNumber());
    }

}
