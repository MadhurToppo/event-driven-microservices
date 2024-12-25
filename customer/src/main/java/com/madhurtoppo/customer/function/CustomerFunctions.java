package com.madhurtoppo.customer.function;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class CustomerFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateLoanMobileNumber() {
        return mobileNumberUpdateDto -> {
            log.info("Received message to updateMobileNumber {}", mobileNumberUpdateDto);
        };
    }

}
