package com.madhurtoppo.loans.service.impl;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import com.madhurtoppo.loans.constants.LoansConstants;
import com.madhurtoppo.loans.dto.LoansDto;
import com.madhurtoppo.loans.entity.Loans;
import com.madhurtoppo.loans.exception.LoanAlreadyExistsException;
import com.madhurtoppo.loans.exception.ResourceNotFoundException;
import com.madhurtoppo.loans.mapper.LoansMapper;
import com.madhurtoppo.loans.repository.LoansRepository;
import com.madhurtoppo.loans.service.ILoansService;
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
public class LoansServiceImpl implements ILoansService {

    private final LoansRepository loansRepository;
    private final StreamBridge streamBridge;


    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoan = loansRepository.findByMobileNumberAndActiveSw(mobileNumber,
                LoansConstants.ACTIVE_SW);
        if (optionalLoan.isPresent()) {
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber " + mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }


    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new loan details
     */
    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 1000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(randomLoanNumber);
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setActiveSw(LoansConstants.ACTIVE_SW);
        return newLoan;
    }


    /**
     * @param mobileNumber - Input mobile Number
     * @return Loan Details based on a given mobileNumber
     */
    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loan = loansRepository.findByMobileNumberAndActiveSw(mobileNumber, LoansConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
                );
        return LoansMapper.mapToLoansDto(loan, new LoansDto());
    }


    /**
     * @param loansDto - LoansDto Object
     * @return boolean indicating if the update of loan details is successful or not
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loan = loansRepository.findByMobileNumberAndActiveSw(loansDto.getMobileNumber(),
                LoansConstants.ACTIVE_SW).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber().toString()));
        LoansMapper.mapToLoans(loansDto, loan);
        loansRepository.save(loan);
        return true;
    }


    /**
     * @param loanNumber - Input Loan Number
     * @return boolean indicating if the delete of loan details is successful or not
     */
    @Override
    public boolean deleteLoan(Long loanNumber) {
        Loans loan = loansRepository.findById(loanNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Loan", "loanNumber", loanNumber.toString())
                );
        loan.setActiveSw(LoansConstants.IN_ACTIVE_SW);
        loansRepository.save(loan);
        return true;
    }


    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        boolean result = false;
        try {
            String currentMobileNumber = mobileNumberUpdateDto.getCurrentMobileNumber();
            Loans loan = loansRepository.findByMobileNumberAndActiveSw(currentMobileNumber, LoansConstants.ACTIVE_SW).orElseThrow(
                    () -> new ResourceNotFoundException("Loan", "mobileNumber", currentMobileNumber)
            );
            loan.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
            loansRepository.save(loan);
//            throw new RuntimeException("Exception in updateMobileNumber");
            updateMobileNumberStatus(mobileNumberUpdateDto);
            result = true;
        } catch (Exception exception) {
            log.error("Exception in updateMobileNumber {}", exception.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            rollbackCardMobileNumber(mobileNumberUpdateDto);
        }
        return result;
    }


    private void updateMobileNumberStatus(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending message to updateMobileNumberStatus {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateMobileNumberStatus-out-0", mobileNumberUpdateDto);
        log.info("Message sent to updateMobileNumberStatus {}", result);
    }


    private void rollbackCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending message to rollbackCardMobileNumber {}", mobileNumberUpdateDto);
        var result = streamBridge.send("rollbackCardMobileNumber-out-0", mobileNumberUpdateDto);
        log.info("Is the rollbackCardMobileNumber request successfully sent {}", result);
    }

}
