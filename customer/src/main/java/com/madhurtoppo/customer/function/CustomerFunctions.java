package com.madhurtoppo.customer.function;

import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import com.madhurtoppo.customer.service.ICustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class CustomerFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateMobileNumberStatus() {
        return mobileNumberUpdateDto -> {
            log.info("Received message to updateMobileNumber {}", mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackCustomerMobileNumber(ICustomerService iCustomerService) {
        return mobileNumberUpdateDto -> {
            log.info("Received message to rollbackCustomerMobileNumber {}", mobileNumberUpdateDto);
            iCustomerService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}
