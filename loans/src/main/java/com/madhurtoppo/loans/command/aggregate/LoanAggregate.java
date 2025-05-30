package com.madhurtoppo.loans.command.aggregate;

import com.madhurtoppo.common.command.UpdateLoanMobileCommand;
import com.madhurtoppo.common.event.LoanMobileUpdatedEvent;
import com.madhurtoppo.loans.command.CreateLoanCommand;
import com.madhurtoppo.loans.command.DeleteLoanCommand;
import com.madhurtoppo.loans.command.UpdateLoanCommand;
import com.madhurtoppo.loans.command.event.LoanCreatedEvent;
import com.madhurtoppo.loans.command.event.LoanDeletedEvent;
import com.madhurtoppo.loans.command.event.LoanUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@Aggregate
@Slf4j
public class LoanAggregate {

    @AggregateIdentifier
    private Long loanNumber;
    private String mobileNumber;
    private String loanType;
    private String loanStatus;
    private int totalLoan;
    private int amountPaid;
    private int outstandingAmount;
    private boolean activeSw;

    private final String LOAN_APPROVAL_DEADLINE = "loan-approval-deadline-";


    public LoanAggregate() {
    }


    @CommandHandler
    public LoanAggregate(CreateLoanCommand createCommand) {
        LoanCreatedEvent loanCreatedEvent = new LoanCreatedEvent();
        BeanUtils.copyProperties(createCommand, loanCreatedEvent);
        AggregateLifecycle.apply(loanCreatedEvent);
    }


    @EventSourcingHandler
    public void on(LoanCreatedEvent loanCreatedEvent) {
        this.loanNumber = loanCreatedEvent.getLoanNumber();
        this.mobileNumber = loanCreatedEvent.getMobileNumber();
        this.loanType = loanCreatedEvent.getLoanType();
        this.loanStatus = loanCreatedEvent.getLoanStatus();
        this.totalLoan = loanCreatedEvent.getTotalLoan();
        this.amountPaid = loanCreatedEvent.getAmountPaid();
        this.outstandingAmount = loanCreatedEvent.getOutstandingAmount();
        this.activeSw = loanCreatedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateLoanCommand updateCommand) {
        LoanUpdatedEvent loanUpdatedEvent = new LoanUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, loanUpdatedEvent);
        AggregateLifecycle.apply(loanUpdatedEvent);
    }


    @EventSourcingHandler
    public void on(LoanUpdatedEvent loanUpdatedEvent) {
        this.loanType = loanUpdatedEvent.getLoanType();
        this.totalLoan = loanUpdatedEvent.getTotalLoan();
        this.amountPaid = loanUpdatedEvent.getAmountPaid();
        this.outstandingAmount = loanUpdatedEvent.getOutstandingAmount();
    }


    @CommandHandler
    public void handle(DeleteLoanCommand deleteCommand) {
        LoanDeletedEvent loanDeletedEvent = new LoanDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, loanDeletedEvent);
        AggregateLifecycle.apply(loanDeletedEvent);
    }


    @EventSourcingHandler
    public void on(LoanDeletedEvent loanDeletedEvent) {
        this.activeSw = loanDeletedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateLoanMobileCommand updateLoanMobileCommand) {
        LoanMobileUpdatedEvent loanMobileUpdatedEvent = new LoanMobileUpdatedEvent();
        BeanUtils.copyProperties(updateLoanMobileCommand, loanMobileUpdatedEvent);
        AggregateLifecycle.apply(loanMobileUpdatedEvent);
//        throw new RuntimeException("An error occurred while processing UpdateLoanMobileCommand");
    }


    @EventSourcingHandler
    public void on(LoanMobileUpdatedEvent loanMobileUpdatedEvent) {
        this.mobileNumber = loanMobileUpdatedEvent.getNewMobileNumber();
    }


}
