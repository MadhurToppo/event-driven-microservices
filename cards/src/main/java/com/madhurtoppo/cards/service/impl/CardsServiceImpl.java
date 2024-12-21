package com.madhurtoppo.cards.service.impl;

import com.madhurtoppo.cards.command.event.CardUpdatedEvent;
import com.madhurtoppo.cards.constants.CardsConstants;
import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.cards.entity.Cards;
import com.madhurtoppo.cards.exception.CardAlreadyExistsException;
import com.madhurtoppo.cards.exception.ResourceNotFoundException;
import com.madhurtoppo.cards.mapper.CardsMapper;
import com.madhurtoppo.cards.repository.CardsRepository;
import com.madhurtoppo.cards.service.ICardsService;
import com.madhurtoppo.common.event.CardDataChangeEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;


@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;
    private EventGateway eventGateway;


    /**
     * @param card - Cards object
     */
    @Override
    public void createCard(Cards card) {
        Optional<Cards> optionalCard = cardsRepository.findByMobileNumberAndActiveSw(card.getMobileNumber(),
                CardsConstants.ACTIVE_SW);
        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + card.getMobileNumber());
        }
        cardsRepository.save(card);
    }


    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(mobileNumber, CardsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
                );
        return CardsMapper.mapToCardsDto(card, new CardsDto());
    }


    /**
     * @param event - CardUpdatedEvent Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardUpdatedEvent event) {
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(event.getMobileNumber(),
                CardsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber",
                event.getMobileNumber()));
        CardsMapper.mapEventToCard(event, card);
        cardsRepository.save(card);
        return true;
    }


    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(Long cardNumber) {
        Cards card = cardsRepository
                .findById(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber.toString()));
        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardsRepository.save(card);
        CardDataChangeEvent cardDataChangeEvent = new CardDataChangeEvent();
        cardDataChangeEvent.setMobileNumber(card.getMobileNumber());
        cardDataChangeEvent.setCardNumber(0L);
        eventGateway.publish(0L);
        return true;
    }


}
