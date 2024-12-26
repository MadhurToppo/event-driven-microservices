package com.madhurtoppo.cards.service;

import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;


public interface ICardsService {

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createCard(String mobileNumber);


    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);


    /**
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateCard(CardsDto cardsDto);


    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    boolean deleteCard(Long cardNumber);


    /**
     * This method is used to update the mobile number of the customer.
     *
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto Object
     * @return boolean indicating if the update of mobileNumber is successful or not
     */
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);


    /**
     * This method is used to roll back the mobile number of the customer.
     *
     * @param mobileNumberUpdateDto - MobileNumberUpdateDto Object
     * @return boolean indicating if the rollback of mobileNumber is successful or not
     */
    boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

}
