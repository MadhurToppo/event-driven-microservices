package com.madhurtoppo.cards.service;

import com.madhurtoppo.cards.command.event.CardUpdatedEvent;
import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.cards.entity.Cards;


public interface ICardsService {

    /**
     * @param card - Cards Object
     */
    void createCard(Cards card);


    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);


    /**
     * @param event - CardUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateCard(CardUpdatedEvent event);


    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    boolean deleteCard(Long cardNumber);


    /**
     * @param oldMobileNumber - Input mobile Number
     * @param newMobileNumber - New Mobile Number to be updated
     * @return boolean indicating if the update of mobile number is successful or not

     */
    boolean updateMobileNumber(String oldMobileNumber, String newMobileNumber);

}
