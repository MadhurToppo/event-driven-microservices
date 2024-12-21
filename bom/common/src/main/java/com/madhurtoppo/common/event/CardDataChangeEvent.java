package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class CardDataChangeEvent {

    private String mobileNumber;
    private Long cardNumber;

}
