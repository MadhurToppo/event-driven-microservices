package com.madhurtoppo.loans.function;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import com.madhurtoppo.loans.service.ILoansService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class LoanFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateLoanMobileNumber(ILoansService iLoansService) {
        return mobileNumberUpdateDto -> {
            log.info("Sending message to updateLoanMobileNumber {}", mobileNumberUpdateDto);
            iLoansService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

}
