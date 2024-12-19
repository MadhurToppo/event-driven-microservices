package com.madhurtoppo.accounts.query.handler;

import com.madhurtoppo.accounts.dto.AccountsDto;
import com.madhurtoppo.accounts.query.FindAccountQuery;
import com.madhurtoppo.accounts.service.IAccountsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AccountsQueryHandler {

    private final IAccountsService iAccountsService;


    @QueryHandler
    public AccountsDto findAccount(FindAccountQuery query) {
        return iAccountsService.fetchAccount(query.getMobileNumber());
    }

}
