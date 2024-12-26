package com.madhurtoppo.accounts.service;

import com.madhurtoppo.accounts.command.event.AccountUpdatedEvent;
import com.madhurtoppo.accounts.dto.AccountsDto;
import com.madhurtoppo.accounts.entity.Accounts;


public interface IAccountsService {

    /**
     * @param account - Accounts Object
     */
    void createAccount(Accounts account);


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    AccountsDto fetchAccount(String mobileNumber);


    /**
     * @param event - AccountUpdatedEvent Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    boolean updateAccount(AccountUpdatedEvent event);


    /**
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    boolean deleteAccount(Long accountNumber);


    /**
     * @param mobileNumber - Input Mobile Number
     * @param newMobileNumber - Updated Mobile Number
     * @return boolean indicating if the update of Mobile Number is successful or not
     */
    boolean updateMobileNumber(String mobileNumber, String newMobileNumber);

}
