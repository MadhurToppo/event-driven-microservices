package com.madhurtoppo.cards.query.handler;

import com.madhurtoppo.cards.dto.CardsDto;
import com.madhurtoppo.cards.query.FindCardQuery;
import com.madhurtoppo.cards.service.ICardsService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CardQueryHandler {

    private final ICardsService iCardsService;


    @QueryHandler
    public CardsDto findCard(FindCardQuery query) {
        return iCardsService.fetchCard(query.getMobileNumber());
    }

}
