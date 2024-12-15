package com.madhurtoppo.gatewayserver.handler;

import com.madhurtoppo.gatewayserver.dto.AccountsDto;
import com.madhurtoppo.gatewayserver.dto.CardsDto;
import com.madhurtoppo.gatewayserver.dto.CustomerDto;
import com.madhurtoppo.gatewayserver.dto.CustomerSummaryDto;
import com.madhurtoppo.gatewayserver.dto.LoansDto;
import com.madhurtoppo.gatewayserver.service.client.CustomerSummaryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class CustomerCompositeHandler {

    private final CustomerSummaryClient customerSummaryClient;


    public Mono<ServerResponse> getCustomerSummary(ServerRequest request) {
        String mobileNumber = request.queryParam("mobileNumber").get();
        Mono<ResponseEntity<CustomerDto>> customerDetails = customerSummaryClient.fetchCustomerDetails(mobileNumber);
        Mono<ResponseEntity<AccountsDto>> accountDetails = customerSummaryClient.fetchAccountDetails(mobileNumber);
        Mono<ResponseEntity<LoansDto>> loanDetails = customerSummaryClient.fetchLoanDetails(mobileNumber);
        Mono<ResponseEntity<CardsDto>> cardDetails = customerSummaryClient.fetchCardDetails(mobileNumber);

        return Mono.zip(customerDetails, accountDetails, loanDetails, cardDetails)
                .flatMap(tuple -> {
                    CustomerDto customerDto = tuple.getT1().getBody();
                    AccountsDto accountsDto = tuple.getT2().getBody();
                    LoansDto loansDto = tuple.getT3().getBody();
                    CardsDto cardsDto = tuple.getT4().getBody();
                    CustomerSummaryDto customerSummaryDto = new CustomerSummaryDto(customerDto, accountsDto, loansDto, cardsDto);
                    return ServerResponse.ok().bodyValue(customerSummaryDto);
                });
    }

}
