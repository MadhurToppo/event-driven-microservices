package com.madhurtoppo.gatewayserver.service.client;

import com.madhurtoppo.gatewayserver.dto.AccountsDto;
import com.madhurtoppo.gatewayserver.dto.CardsDto;
import com.madhurtoppo.gatewayserver.dto.CustomerDto;
import com.madhurtoppo.gatewayserver.dto.LoansDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;


public interface CustomerServiceClient {

    @GetExchange(value = "/eazybank/customer/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CustomerDto>> fetchCustomerSummary(@RequestParam String mobileNumber);


    @GetExchange(value = "/eazybank/accounts/api/fetch", accept = "application/json")
    Mono<ResponseEntity<AccountsDto>> fetchAccountDetails(@RequestParam("mobileNumber") String mobileNumber);


    @GetExchange(value = "/eazybank/loans/api/fetch", accept = "application/json")
    Mono<ResponseEntity<LoansDto>> fetchLoanDetails(@RequestParam("mobileNumber") String mobileNumber);


    @GetExchange(value = "/eazybank/cards/api/fetch", accept = "application/json")
    Mono<ResponseEntity<CardsDto>> fetchCardDetails(@RequestParam("mobileNumber") String mobileNumber);

}
