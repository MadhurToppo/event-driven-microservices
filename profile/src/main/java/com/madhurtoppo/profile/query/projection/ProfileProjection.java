package com.madhurtoppo.profile.query.projection;

import com.madhurtoppo.common.event.AccountDataChangedEvent;
import com.madhurtoppo.common.event.CardDataChangeEvent;
import com.madhurtoppo.common.event.CustomerDataChangedEvent;
import com.madhurtoppo.common.event.LoanDataChangeEvent;
import com.madhurtoppo.profile.service.IProfileService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@ProcessingGroup("customer-group")
public class ProfileProjection {

    private final IProfileService iProfileService;


    @EventHandler
    public void on(CustomerDataChangedEvent customerDataChangedEvent) {
        iProfileService.handleCustomerDataChangedEvent(customerDataChangedEvent);
    }


    @EventHandler
    public void on(AccountDataChangedEvent accountDataChangedEvent) {
        iProfileService.handleAccountDataChangedEvent(accountDataChangedEvent);
    }


    @EventHandler
    public void on(LoanDataChangeEvent loanDataChangeEvent) {
        iProfileService.handleLoanDataChangedEvent(loanDataChangeEvent);
    }


    @EventHandler
    public void on(CardDataChangeEvent cardDataChangeEvent) {
        iProfileService.handleCardDataChangedEvent(cardDataChangeEvent);
    }

}
