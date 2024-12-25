package com.madhurtoppo.loans.service;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import com.madhurtoppo.loans.dto.LoansDto;


public interface ILoansService {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createLoan(String mobileNumber);


    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    LoansDto fetchLoan(String mobileNumber);


    /**
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateLoan(LoansDto loansDto);


    /**
     * @param loanNumber - Input Loan Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    boolean deleteLoan(Long loanNumber);


    /**
     * This method is used to update the mobile number of the customer.
     *
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto Object containing the new mobile number details
     * @return boolean indicating if the update of the mobile number is successful or not
     */
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

}
