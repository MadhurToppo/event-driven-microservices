package com.madhurtoppo.customer.command.controller;

import com.madhurtoppo.common.command.UpdateCustomerMobileCommand;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import com.madhurtoppo.customer.command.CreateCustomerCommand;
import com.madhurtoppo.customer.command.DeleteCustomerCommand;
import com.madhurtoppo.customer.command.UpdateCustomerCommand;
import com.madhurtoppo.customer.constants.CustomerConstants;
import com.madhurtoppo.customer.dto.CustomerDto;
import com.madhurtoppo.customer.dto.ResponseDto;
import com.madhurtoppo.customer.query.FindCustomerQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CustomerCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        CreateCustomerCommand customerCommand = CreateCustomerCommand.builder()
                .customerId(UUID.randomUUID().toString())
                .email(customerDto.getEmail())
                .name(customerDto.getName())
                .mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(customerCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.CREATED)
                .body(new ResponseDto(CustomerConstants.STATUS_201, CustomerConstants.MESSAGE_201));
    }


    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCustomerDetails(@Valid @RequestBody CustomerDto customerDto) {
        UpdateCustomerCommand updateCustomerCommand = UpdateCustomerCommand.builder()
                .customerId(customerDto.getCustomerId())
                .email(customerDto.getEmail())
                .name(customerDto.getName())
                .mobileNumber(customerDto.getMobileNumber())
                .activeSw(CustomerConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(updateCustomerCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.STATUS_200));
    }


    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCustomer(@RequestParam("customerId")
    @Pattern(regexp = "(^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$)",
            message = "CustomerId is invalid") String customerId) {
        DeleteCustomerCommand deleteCustomerCommand = DeleteCustomerCommand.builder()
                .customerId(customerId)
                .activeSw(CustomerConstants.IN_ACTIVE_SW)
                .build();
        commandGateway.sendAndWait(deleteCustomerCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.OK)
                .body(new ResponseDto(CustomerConstants.STATUS_200, CustomerConstants.MESSAGE_200));
    }

    @PatchMapping("/mobileNumber")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberUpdateDto mobileNumberUpdateDto) {
        UpdateCustomerMobileCommand updateCusMobNumCommand = UpdateCustomerMobileCommand.builder()
                .customerId(mobileNumberUpdateDto.getCustomerId())
                .accountNumber(mobileNumberUpdateDto.getAccountNumber())
                .loanNumber(mobileNumberUpdateDto.getLoanNumber())
                .cardNumber(mobileNumberUpdateDto.getCardNumber())
                .mobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber())
                .newMobileNumber(mobileNumberUpdateDto.getNewMobileNumber()).build();
        try (SubscriptionQueryResult<ResponseDto, ResponseDto> queryResult = queryGateway.subscriptionQuery(
                new FindCustomerQuery(mobileNumberUpdateDto.getNewMobileNumber()),
                ResponseTypes.instanceOf(ResponseDto.class), ResponseTypes.instanceOf(ResponseDto.class))) {
            commandGateway.send(updateCusMobNumCommand, new CommandCallback<>() {
                @Override
                public void onResult(@Nonnull CommandMessage<? extends UpdateCustomerMobileCommand> commandMessage,
                                     @Nonnull CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional()) {
                        ResponseEntity
                                .status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(new ResponseDto(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR.name(),
                                        commandResultMessage.exceptionResult().getMessage()));
                    }
                }
            });
            return ResponseEntity.status(HttpStatus.OK)
                    .body(queryResult.updates().blockFirst());
        }
    }

}
