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
import com.madhurtoppo.customer.constants.CustomerConstants;
import com.madhurtoppo.customer.dto.ResponseDto;
import com.madhurtoppo.customer.query.FindCustomerQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;



@Saga
@Slf4j
public class UpdateMobileNumberSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    @StartSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileUpdatedEvent event) {
        log.info("Saga Event 1 [Start] : Received CusMobNumUpdatedEvent for customerId: {}", event.getCustomerId());
                UpdateAccountMobileCommand command = UpdateAccountMobileCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateAccountMobileCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCustomerMobileCommand rollbackCommand = RollbackCustomerMobileCommand.builder()
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackCommand);
                }
            }
        });

    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileUpdatedEvent event) {
        log.info("Saga Event 2 : Received AccntMobileNumUpdatedEvent for accountNumber: {}", event.getAccountNumber());
        UpdateCardMobileCommand command = UpdateCardMobileCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateCardMobileCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackAccountMobileCommand rollbackCommand = RollbackAccountMobileCommand.builder()
                            .accountNumber(event.getAccountNumber())
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackCommand);
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileUpdatedEvent event) {
        log.info("Saga Event 3 : Received CardMobileNumUpdatedEvent for cardNumber: {}", event.getCardNumber());
        UpdateLoanMobileCommand command = UpdateLoanMobileCommand.builder()
                .accountNumber(event.getAccountNumber())
                .cardNumber(event.getCardNumber())
                .loanNumber(event.getLoanNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber()).build();
        commandGateway.send(command, new CommandCallback<>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends UpdateLoanMobileCommand> commandMessage,
                                 @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    RollbackCardMobileCommand rollbackCommand = RollbackCardMobileCommand.builder()
                            .cardNumber(event.getCardNumber())
                            .accountNumber(event.getAccountNumber())
                            .customerId(event.getCustomerId())
                            .mobileNumber(event.getMobileNumber())
                            .newMobileNumber(event.getNewMobileNumber())
                            .errorMsg(commandResultMessage.exceptionResult().getMessage()).build();
                    commandGateway.sendAndWait(rollbackCommand);
                }
            }
        });
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(LoanMobileUpdatedEvent event) {
        log.info("Saga Event 4 [END] : Received LoanMobileNumUpdatedEvent for loanNumber: {}", event.getLoanNumber());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MOBILE_UPD_SUCCESS_MESSAGE));
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CardMobileRollbackedEvent event) {
        log.info("Saga Compensation Event : Received CardMobNumRollbackedEvent for cardNumber: {}", event.getCardNumber());
        RollbackAccountMobileCommand rollbackCommand = RollbackAccountMobileCommand.builder()
                .accountNumber(event.getAccountNumber())
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber())
                .errorMsg(event.getErrorMsg()).build();
        commandGateway.send(rollbackCommand);
    }

    @SagaEventHandler(associationProperty = "customerId")
    public void handle(AccountMobileRollbackedEvent event) {
        log.info("Saga Compensation Event : Received AccntMobNumRollbackedEvent for accountNumber: {}", event.getAccountNumber());
        RollbackCustomerMobileCommand rollbackCommand = RollbackCustomerMobileCommand.builder()
                .customerId(event.getCustomerId())
                .mobileNumber(event.getMobileNumber())
                .newMobileNumber(event.getNewMobileNumber())
                .errorMsg(event.getErrorMsg()).build();
        commandGateway.send(rollbackCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "customerId")
    public void handle(CustomerMobileRollbackedEvent event) {
        log.info("Saga Compensation Event [END] : Received CusMobNumRollbackedEvent for customerId: {}",
                event.getCustomerId());
        queryUpdateEmitter.emit(FindCustomerQuery.class, query -> true,
                new ResponseDto(CustomerConstants.STATUS_500, CustomerConstants.MOBILE_UPD_FAILURE_MESSAGE));
    }

}
