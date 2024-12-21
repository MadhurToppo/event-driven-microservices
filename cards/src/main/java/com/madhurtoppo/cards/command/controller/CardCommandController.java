package com.madhurtoppo.cards.command.controller;

import com.madhurtoppo.cards.command.CreateCardCommand;
import com.madhurtoppo.cards.command.DeleteCardCommand;
import com.madhurtoppo.cards.command.UpdateCardCommand;
import com.madhurtoppo.cards.constants.CardsConstants;
import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.cards.dto.ResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;


@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@RequiredArgsConstructor
public class CardCommandController {

    private final CommandGateway commandGateway;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@RequestParam("mobileNumber")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits") String mobileNumber) {
        long randomCardNumber = 1000000000L + new Random().nextInt(900000000);
        CreateCardCommand createCommand = CreateCardCommand.builder()
                .cardNumber(randomCardNumber).mobileNumber(mobileNumber)
                .cardType(CardsConstants.CREDIT_CARD).totalLimit(CardsConstants.NEW_CARD_LIMIT)
                .amountUsed(0).availableAmount(CardsConstants.NEW_CARD_LIMIT)
                .activeSw(CardsConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(createCommand);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }


    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
        UpdateCardCommand updateCommand = UpdateCardCommand.builder()
                .cardNumber(cardsDto.getCardNumber()).mobileNumber(cardsDto.getMobileNumber())
                .cardType(CardsConstants.CREDIT_CARD).totalLimit(cardsDto.getTotalLimit())
                .amountUsed(cardsDto.getAmountUsed()).availableAmount(cardsDto.getAvailableAmount())
                .activeSw(CardsConstants.ACTIVE_SW).build();
        commandGateway.sendAndWait(updateCommand);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
    }


    @PatchMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam("cardNumber") Long cardNumber) {
        DeleteCardCommand deleteCommand = DeleteCardCommand.builder()
                .cardNumber(cardNumber).activeSw(CardsConstants.IN_ACTIVE_SW).build();
        commandGateway.sendAndWait(deleteCommand);
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.OK)
                .body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
    }

}
