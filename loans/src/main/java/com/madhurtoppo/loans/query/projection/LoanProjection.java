package com.madhurtoppo.loans.query.projection;

import com.madhurtoppo.common.event.LoanMobileUpdatedEvent;
import com.madhurtoppo.loans.command.event.LoanCreatedEvent;
import com.madhurtoppo.loans.command.event.LoanDeletedEvent;
import com.madhurtoppo.loans.command.event.LoanUpdatedEvent;
import com.madhurtoppo.loans.entity.Loans;
import com.madhurtoppo.loans.service.ILoansService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ProcessingGroup("loan-group")
public class LoanProjection {

    private final ILoansService iLoansService;


    @EventHandler
    public void on(LoanCreatedEvent event) {
        Loans loanEntity = new Loans();
        BeanUtils.copyProperties(event, loanEntity);
        iLoansService.createLoan(loanEntity);
    }


    @EventHandler
    public void on(LoanUpdatedEvent event) {
        iLoansService.updateLoan(event);
    }


    @EventHandler
    public void on(LoanDeletedEvent event) {
        iLoansService.deleteLoan(event.getLoanNumber());
    }


    @EventHandler
    public void on(LoanMobileUpdatedEvent loanMobileUpdatedEvent) {
        iLoansService.updateMobileNumber(loanMobileUpdatedEvent.getMobileNumber(), loanMobileUpdatedEvent.getNewMobileNumber());
    }

}
