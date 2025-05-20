package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.DAOCard;
import ar.com.mylback.dal.entities.Card;
import ar.com.mylback.utils.ImageUrlGenerator;
import ar.com.mylback.utils.QueryString;
import ar.com.mylback.utils.entitydtomappers.CardMapper;
import com.google.gson.Gson;
import ar.com.myldtos.cards.CardDTO;

import java.util.List;

public class CardsController {

    public String getCardsEndpoint(QueryString queryString) throws Exception {
        // get cards from DB
        DAOCard<Integer> daoCard = new DAOCard<>();
        List<Card> cards = daoCard.findAllPaged(queryString.getPage(),
                queryString.getPageSize(),
                queryString.getCosts(),
                queryString.getDamages(),
                queryString.getCollectionsIds(),
                queryString.getRaritiesIds(),
                queryString.getTypesIds(),
                queryString.getRacesIds(),
                queryString.getFormatsIds(),
                queryString.getKeyWordsIds());

        List<CardDTO> cardDTOs = cards.stream()
                .map(CardMapper::toDTO)
                .toList();

        ImageUrlGenerator.getInstance().close();
        Gson gson = new Gson();
        return gson.toJson(cardDTOs);
    }
}
