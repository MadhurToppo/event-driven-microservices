package com.madhurtoppo.accounts.function;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class AccountFunctions {
    public Consumer<MobileNumberUpdateDto> updateAccountMobileNumber() {
        return mobileNumberUpdateDto -> log.info("Sending message to updateAccountMobileNumber {}", mobileNumberUpdateDto);
    }
}
