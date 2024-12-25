package com.madhurtoppo.accounts.service;

import com.madhurtoppo.accounts.dto.AccountsDto;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;


public interface IAccountsService {

    /**
     *
     * @param mobileNumber - Input Mobile Number
     */
    void createAccount(String mobileNumber);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    AccountsDto fetchAccount(String mobileNumber);

    /**
     *
     * @param accountsDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(AccountsDto accountsDto);

    /**
     *
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(Long accountNumber);


    /**
     * This method is used to update the mobile number of the customer.
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto Object
     * @return boolean indicating if the update of mobileNumber is successful or not
     */
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

}
