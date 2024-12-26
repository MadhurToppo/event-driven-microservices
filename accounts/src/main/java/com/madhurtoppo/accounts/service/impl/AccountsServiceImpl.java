package com.madhurtoppo.accounts.service.impl;

import com.madhurtoppo.accounts.constants.AccountsConstants;
import com.madhurtoppo.accounts.dto.AccountsDto;
import com.madhurtoppo.accounts.entity.Accounts;
import com.madhurtoppo.accounts.exception.AccountAlreadyExistsException;
import com.madhurtoppo.accounts.exception.ResourceNotFoundException;
import com.madhurtoppo.accounts.mapper.AccountsMapper;
import com.madhurtoppo.accounts.repository.AccountsRepository;
import com.madhurtoppo.accounts.service.IAccountsService;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;
import java.util.Random;


@Service
@AllArgsConstructor
@Slf4j
public class AccountsServiceImpl implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final StreamBridge streamBridge;


    /**
     * @param mobileNumber - String
     */
    @Override
    public void createAccount(String mobileNumber) {
        Optional<Accounts> optionalAccounts = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber,
                AccountsConstants.ACTIVE_SW);
        if (optionalAccounts.isPresent()) {
            throw new AccountAlreadyExistsException("Account already registered with given mobileNumber " + mobileNumber);
        }
        accountsRepository.save(createNewAccount(mobileNumber));
    }


    /**
     * @param mobileNumber - String
     * @return the new account details
     */
    private Accounts createNewAccount(String mobileNumber) {
        Accounts newAccount = new Accounts();
        newAccount.setMobileNumber(mobileNumber);
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        newAccount.setActiveSw(AccountsConstants.ACTIVE_SW);
        return newAccount;
    }


    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public AccountsDto fetchAccount(String mobileNumber) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(mobileNumber, AccountsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber", mobileNumber)
                );
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(account, new AccountsDto());
        return accountsDto;
    }


    /**
     * @param accountsDto - AccountsDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(AccountsDto accountsDto) {
        Accounts account = accountsRepository.findByMobileNumberAndActiveSw(accountsDto.getMobileNumber(),
                AccountsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Account", "mobileNumber",
                accountsDto.getMobileNumber()));
        AccountsMapper.mapToAccounts(accountsDto, account);
        accountsRepository.save(account);
        return true;
    }


    /**
     * @param accountNumber - Input Account Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(Long accountNumber) {
        Accounts account = accountsRepository.findById(accountNumber).orElseThrow(
                () -> new ResourceNotFoundException("Account", "accountNumber", accountNumber.toString())
        );
        account.setActiveSw(AccountsConstants.IN_ACTIVE_SW);
        accountsRepository.save(account);
        return true;
    }


    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        boolean result = false;
        try {
            String currentMobileNumber = mobileNumberUpdateDto.getCurrentMobileNumber();
            Accounts accounts = accountsRepository.findByMobileNumberAndActiveSw(currentMobileNumber, true).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNumber)
            );
            accounts.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
            accountsRepository.save(accounts);
            updateCardMobileNumber(mobileNumberUpdateDto);
            result = true;
        } catch (Exception exception) {
            log.error("Exception in updateMobileNumber {}", exception.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            rollbackCustomerMobileNumber(mobileNumberUpdateDto);
        }
        return result;
    }


    private void updateCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending message to updateCardMobileNumber {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateCardMobileNumber-out-0", mobileNumberUpdateDto);
        log.info("Is the updateCardMobileNumber request successfully sent {}", result);
    }


    private void rollbackCustomerMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending message to rollbackCustomerMobileNumber {}", mobileNumberUpdateDto);
        var result = streamBridge.send("rollbackCustomerMobileNumber-out-0", mobileNumberUpdateDto);
        log.info("Is the rollbackCustomerMobileNumber request successfully sent {}", result);
    }


    @Override
    public boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        String newMobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
        Accounts accounts = accountsRepository.findByMobileNumberAndActiveSw(newMobileNumber, true).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", newMobileNumber)
        );
        accounts.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
        accountsRepository.save(accounts);
        rollbackCustomerMobileNumber(mobileNumberUpdateDto);
        return true;
    }

}
