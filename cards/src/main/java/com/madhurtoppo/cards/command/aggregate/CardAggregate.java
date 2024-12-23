package com.madhurtoppo.cards.command.aggregate;

import com.madhurtoppo.cards.command.CreateCardCommand;
import com.madhurtoppo.cards.command.DeleteCardCommand;
import com.madhurtoppo.cards.command.UpdateCardCommand;
import com.madhurtoppo.cards.command.event.CardCreatedEvent;
import com.madhurtoppo.cards.command.event.CardDeletedEvent;
import com.madhurtoppo.cards.command.event.CardUpdatedEvent;
import com.madhurtoppo.common.event.CardDataChangeEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;


@Aggregate
public class CardAggregate {

    @AggregateIdentifier
    private Long cardNumber;
    private String mobileNumber;
    private String cardType;
    private int totalLimit;
    private int amountUsed;
    private int availableAmount;
    private boolean activeSw;


    public CardAggregate() {
    }


    @CommandHandler
    public CardAggregate(CreateCardCommand createCommand) {
        CardCreatedEvent cardCreatedEvent = new CardCreatedEvent();
        BeanUtils.copyProperties(createCommand, cardCreatedEvent);
        CardDataChangeEvent cardDataChangeEvent = new CardDataChangeEvent();
        BeanUtils.copyProperties(createCommand, cardDataChangeEvent);
        AggregateLifecycle.apply(cardCreatedEvent)
                .andThen(() -> AggregateLifecycle.apply(cardDataChangeEvent));
    }


    @EventSourcingHandler
    public void on(CardCreatedEvent cardCreatedEvent) {
        this.cardNumber = cardCreatedEvent.getCardNumber();
        this.mobileNumber = cardCreatedEvent.getMobileNumber();
        this.cardType = cardCreatedEvent.getCardType();
        this.totalLimit = cardCreatedEvent.getTotalLimit();
        this.amountUsed = cardCreatedEvent.getAmountUsed();
        this.availableAmount = cardCreatedEvent.getAvailableAmount();
        this.activeSw = cardCreatedEvent.isActiveSw();
    }


    @CommandHandler
    public void handle(UpdateCardCommand updateCommand) {
        CardUpdatedEvent cardUpdatedEvent = new CardUpdatedEvent();
        BeanUtils.copyProperties(updateCommand, cardUpdatedEvent);
        CardDataChangeEvent cardDataChangeEvent = new CardDataChangeEvent();
        BeanUtils.copyProperties(updateCommand, cardDataChangeEvent);
        AggregateLifecycle.apply(cardUpdatedEvent);
        AggregateLifecycle.apply(cardDataChangeEvent);
    }


    @EventSourcingHandler
    public void on(CardUpdatedEvent cardUpdatedEvent) {
        this.cardType = cardUpdatedEvent.getCardType();
        this.totalLimit = cardUpdatedEvent.getTotalLimit();
        this.amountUsed = cardUpdatedEvent.getAmountUsed();
        this.availableAmount = cardUpdatedEvent.getAvailableAmount();
    }


    @CommandHandler
    public void handle(DeleteCardCommand deleteCommand) {
        CardDeletedEvent cardDeletedEvent = new CardDeletedEvent();
        BeanUtils.copyProperties(deleteCommand, cardDeletedEvent);
        AggregateLifecycle.apply(cardDeletedEvent);
    }


    @EventSourcingHandler
    public void on(CardDeletedEvent cardDeletedEvent) {
        this.activeSw = cardDeletedEvent.isActiveSw();
    }

}
