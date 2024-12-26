package com.madhurtoppo.loans.service;

import com.madhurtoppo.loans.command.event.LoanUpdatedEvent;
import com.madhurtoppo.loans.dto.LoansDto;
import com.madhurtoppo.loans.entity.Loans;


public interface ILoansService {

    /**
     * @param loan - Loans object
     */
    void createLoan(Loans loan);


    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);


    /**
     * @param event - LoanUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoanUpdatedEvent event);


    /**
     * @param loanNumber - Input Loan Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    boolean deleteLoan(Long loanNumber);


    /**
     * Update mobile number associated with a loan
     *
     * @param oldMobileNumber - original mobile number
     * @param newMobileNumber - new mobile number
     * @return boolean indicating if the update of mobile number is successful or not
     */
    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
