package com.madhurtoppo.common.event;

import lombok.Data;


@Data
public class LoanDataChangeEvent {

    private String mobileNumber;
    private Long loanNumber;

}
