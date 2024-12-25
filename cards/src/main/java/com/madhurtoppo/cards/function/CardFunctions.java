package com.madhurtoppo.cards.function;

import com.madhurtoppo.cards.service.ICardsService;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;


@Configuration
@Slf4j
public class CardFunctions {

    @Bean
    public Consumer<MobileNumberUpdateDto> updateCardMobileNumber(ICardsService iAccountsService) {
        return mobileNumberUpdateDto -> {
            log.info("Sending message to updateCardMobileNumber {}", mobileNumberUpdateDto);
            iAccountsService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

}
