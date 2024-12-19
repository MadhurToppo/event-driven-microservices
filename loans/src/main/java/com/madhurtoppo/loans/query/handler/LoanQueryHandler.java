package com.madhurtoppo.loans.query.handler;

import com.madhurtoppo.loans.dto.LoansDto;
import com.madhurtoppo.loans.query.FindLoanQuery;
import com.madhurtoppo.loans.service.ILoansService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class LoanQueryHandler {

    private final ILoansService iLoansService;


    @QueryHandler
    public LoansDto findLoan(FindLoanQuery query) {
        return iLoansService.fetchLoan(query.getMobileNumber());
    }

}
