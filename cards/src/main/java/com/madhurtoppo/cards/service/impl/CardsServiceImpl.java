package com.madhurtoppo.cards.service.impl;

import com.madhurtoppo.cards.constants.CardsConstants;
import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.cards.entity.Cards;
import com.madhurtoppo.cards.exception.CardAlreadyExistsException;
import com.madhurtoppo.cards.exception.ResourceNotFoundException;
import com.madhurtoppo.cards.mapper.CardsMapper;
import com.madhurtoppo.cards.repository.CardsRepository;
import com.madhurtoppo.cards.service.ICardsService;
import com.madhurtoppo.common.dto.MobileNumberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class CardsServiceImpl implements ICardsService {

    private final CardsRepository cardsRepository;
    private final StreamBridge streamBridge;

    /**
     * @param mobileNumber - Mobile Number of the Customer
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCard = cardsRepository.findByMobileNumberAndActiveSw(mobileNumber,
                CardsConstants.ACTIVE_SW);
        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(randomCardNumber);
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        newCard.setActiveSw(CardsConstants.ACTIVE_SW);
        return newCard;
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
     * @param cardsDto - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(cardsDto.getMobileNumber(),
                CardsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber",
                cardsDto.getCardNumber().toString()));
        CardsMapper.mapToCards(cardsDto, card);
        cardsRepository.save(card);
        return true;
    }

    /**
     * @param cardNumber - Input Card Number
     * @return boolean indicating if the delete of card details is successful or not
     */
    @Override
    public boolean deleteCard(Long cardNumber) {
        Cards card = cardsRepository.findById(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber.toString())
                );
        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardsRepository.save(card);
        return true;
    }


    @Override
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        String currentMobileNumber = mobileNumberUpdateDto.getCurrentMobileNumber();
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(currentMobileNumber,
                CardsConstants.ACTIVE_SW).orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber",
                currentMobileNumber));
        card.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
        cardsRepository.save(card);
        updateLoanMobileNumber(mobileNumberUpdateDto);
        return true;
    }

    private void updateLoanMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending message to updateLoanMobileNumber {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateLoanMobileNumber-out-0", mobileNumberUpdateDto);
        log.info("Message sent to updateLoanMobileNumber {}", result);
    }


}
