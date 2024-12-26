package com.madhurtoppo.customer.query.handler;

import com.madhurtoppo.customer.dto.CustomerDto;
import com.madhurtoppo.customer.query.FindCustomerQuery;
import com.madhurtoppo.customer.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerQueryHandler {

    private final ICustomerService iCustomerService;


    @QueryHandler
    public CustomerDto findCustomer(FindCustomerQuery findCustomerQuery) {
        return iCustomerService.fetchCustomer(findCustomerQuery.getMobileNumber());
    }

}