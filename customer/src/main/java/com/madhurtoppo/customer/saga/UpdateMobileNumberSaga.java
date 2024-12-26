package com.madhurtoppo.customer.saga;


import com.madhurtoppo.common.command.RollbackAccountMobileCommand;
import com.madhurtoppo.common.command.RollbackCardMobileCommand;
import com.madhurtoppo.common.command.RollbackCustomerMobileCommand;
import com.madhurtoppo.common.command.UpdateAccountMobileCommand;
import com.madhurtoppo.common.command.UpdateCardMobileCommand;
import com.madhurtoppo.common.command.UpdateLoanMobileCommand;
import com.madhurtoppo.common.event.AccountMobileRollbackedEvent;
import com.madhurtoppo.common.event.AccountMobileUpdatedEvent;
import com.madhurtoppo.common.event.CardMobileRollbackedEvent;
import com.madhurtoppo.common.event.CardMobileUpdatedEvent;
import com.madhurtoppo.common.event.CustomerMobileRollbackedEvent;
import com.madhurtoppo.common.event.CustomerMobileUpdatedEvent;
import com.madhurtoppo.common.event.LoanMobileUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;


@Saga
@Slf4j
public class UpdateMobileNumberSaga {

    @Autowired
    private transient CommandGateway commandGateway;


    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileUpdatedEvent customerMobileUpdatedEvent) {
        log.info("Saga Event 1 [Start] : Received customerMobileUpdatedEvent for customerId: {}",
                customerMobileUpdatedEvent.getCustomerId());
        UpdateAccountMobileCommand updateAccountMobileCommand = UpdateAccountMobileCommand.builder()
                .accountNumber(customerMobileUpdatedEvent.getAccountNumber())
                .cardNumber(customerMobileUpdatedEvent.getCardNumber())
                .loanNumber(customerMobileUpdatedEvent.getLoanNumber())
                .mobileNumber(customerMobileUpdatedEvent.getMobileNumber())
                .newMobileNumber(customerMobileUpdatedEvent.getNewMobileNumber())
                .build();
        commandGateway.send(updateAccountMobileCommand, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateAccountMobileCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCustomerMobileCommand rollbackCustomerMobileCommand = RollbackCustomerMobileCommand.builder()
                            .customerId(customerMobileUpdatedEvent.getCustomerId())
                            .mobileNumber(customerMobileUpdatedEvent.getMobileNumber())
                            .newMobileNumber(customerMobileUpdatedEvent.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage())
                            .build();
                    commandGateway.send(rollbackCustomerMobileCommand);
                }
            }
        });
    }


    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileUpdatedEvent accountMobileUpdatedEvent) {
        log.info("Saga Event 2 [Start] : Received accountMobileUpdatedEvent for accountNumber: {}",
                accountMobileUpdatedEvent.getAccountNumber());
        UpdateCardMobileCommand updateCardMobileCommand = UpdateCardMobileCommand.builder()
                .accountNumber(accountMobileUpdatedEvent.getAccountNumber())
                .cardNumber(accountMobileUpdatedEvent.getCardNumber())
                .loanNumber(accountMobileUpdatedEvent.getLoanNumber())
                .customerId(accountMobileUpdatedEvent.getCustomerId())
                .mobileNumber(accountMobileUpdatedEvent.getMobileNumber())
                .newMobileNumber(accountMobileUpdatedEvent.getNewMobileNumber())
                .build();
        commandGateway.send(updateCardMobileCommand, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateCardMobileCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackAccountMobileCommand rollbackAccountMobileCommand = RollbackAccountMobileCommand.builder()
                            .accountNumber(accountMobileUpdatedEvent.getAccountNumber())
                            .customerId(accountMobileUpdatedEvent.getCustomerId())
                            .mobileNumber(accountMobileUpdatedEvent.getMobileNumber())
                            .newMobileNumber(accountMobileUpdatedEvent.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage())
                            .build();
                    commandGateway.send(rollbackAccountMobileCommand);
                }
            }
        });
    }


    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileUpdatedEvent cardMobileUpdatedEvent) {
        log.info("Saga Event 3 [Start] : Received cardMobileUpdatedEvent for cardNumber: {}",
                cardMobileUpdatedEvent.getCardNumber());
        UpdateLoanMobileCommand updateLoanMobileCommand = UpdateLoanMobileCommand.builder()
                .customerId(cardMobileUpdatedEvent.getCustomerId())
                .accountNumber(cardMobileUpdatedEvent.getAccountNumber())
                .loanNumber(cardMobileUpdatedEvent.getLoanNumber())
                .cardNumber(cardMobileUpdatedEvent.getCardNumber())
                .mobileNumber(cardMobileUpdatedEvent.getMobileNumber())
                .newMobileNumber(cardMobileUpdatedEvent.getNewMobileNumber())
                .build();
        commandGateway.send(updateLoanMobileCommand, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateLoanMobileCommand> commandMessage,
                    @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCardMobileCommand rollbackCardMobileCommand = RollbackCardMobileCommand.builder()
                            .cardNumber(cardMobileUpdatedEvent.getCardNumber())
                            .accountNumber(cardMobileUpdatedEvent.getAccountNumber())
                            .customerId(cardMobileUpdatedEvent.getCustomerId())
                            .mobileNumber(cardMobileUpdatedEvent.getMobileNumber())
                            .newMobileNumber(cardMobileUpdatedEvent.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage())
                            .build();
                    commandGateway.send(rollbackCardMobileCommand);
                }
            }
        });
    }


    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(LoanMobileUpdatedEvent loanMobileUpdatedEvent) {
        log.info("Saga Event 4 [End] : Received loanMobileUpdatedEvent for loanNumber: {}",
                loanMobileUpdatedEvent.getLoanNumber());
    }


    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileRollbackedEvent cardMobileRollbackedEvent) {
        log.info("Saga Compensation Event : Received cardMobileRollbackedEvent for cardNumber: {}",
                cardMobileRollbackedEvent.getCardNumber());
        RollbackAccountMobileCommand rollbackAccountMobileCommand = RollbackAccountMobileCommand.builder()
                .accountNumber(cardMobileRollbackedEvent.getAccountNumber())
                .customerId(cardMobileRollbackedEvent.getCustomerId())
                .mobileNumber(cardMobileRollbackedEvent.getMobileNumber())
                .newMobileNumber(cardMobileRollbackedEvent.getNewMobileNumber())
                .errorMsg(cardMobileRollbackedEvent.getErrorMsg())
                .build();
        commandGateway.send(rollbackAccountMobileCommand);
    }


    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileRollbackedEvent accountMobileRollbackedEvent) {
        log.info("Saga Compensation Event : Received accountMobileRollbackedEvent for accountNumber: {}",
                accountMobileRollbackedEvent.getAccountNumber());
        RollbackCustomerMobileCommand rollbackCustomerMobileCommand = RollbackCustomerMobileCommand.builder()
                .customerId(accountMobileRollbackedEvent.getCustomerId())
                .mobileNumber(accountMobileRollbackedEvent.getMobileNumber())
                .newMobileNumber(accountMobileRollbackedEvent.getNewMobileNumber())
                .errorMsg(accountMobileRollbackedEvent.getErrorMsg())
                .build();
        commandGateway.send(rollbackCustomerMobileCommand);
    }


    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileRollbackedEvent customerMobileRollbackedEvent) {
        log.info("Saga Compensation Event [End] : Received customerMobileRollbackedEvent for customerId: {}",
                customerMobileRollbackedEvent.getCustomerId());
    }

}
