package com.madhurtoppo.profile.service;

import com.madhurtoppo.common.event.AccountDataChangedEvent;
import com.madhurtoppo.common.event.CardDataChangeEvent;
import com.madhurtoppo.common.event.CustomerDataChangedEvent;
import com.madhurtoppo.common.event.LoanDataChangeEvent;
import com.madhurtoppo.profile.dto.ProfileDto;


public interface IProfileService {


    /**
     * @param customerDataChangedEvent - The {@link CustomerDataChangedEvent} to
     * handle.
     */
    void handleCustomerDataChangedEvent(CustomerDataChangedEvent customerDataChangedEvent);


    /**
     * @param accountDataChangedEvent - The {@link AccountDataChangedEvent} to
     * handle.
     */
    void handleAccountDataChangedEvent(AccountDataChangedEvent accountDataChangedEvent);


    /**
     * @param loanDataChangeEvent - The {@link LoanDataChangeEvent} to
     * handle.
     */
    void handleLoanDataChangedEvent(LoanDataChangeEvent loanDataChangeEvent);


    /**
     * @param cardDataChangeEvent - The {@link CardDataChangeEvent} to
     * handle.
     */
    void handleCardDataChangedEvent(CardDataChangeEvent cardDataChangeEvent);


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Profile Details based on a given mobileNumber
     */
    ProfileDto fetchProfile(String mobileNumber);


}
