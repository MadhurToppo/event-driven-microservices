package com.madhurtoppo.accounts.function;

import com.madhurtoppo.accounts.service.IAccountsService;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class AccountFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateAccountMobileNumber(IAccountsService iAccountsService) {
        return mobileNumberUpdateDto -> {
            log.info("Sending message to updateAccountMobileNumber {}", mobileNumberUpdateDto);
            iAccountsService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackAccountMobileNumber(IAccountsService iAccountsService) {
        return mobileNumberUpdateDto -> {
            log.info("Sending message to rollbackAccountMobileNumber {}", mobileNumberUpdateDto);
            iAccountsService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}
