package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCard;
import ar.com.mylback.dal.entities.cards.Card;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.QueryString;
import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import ar.com.myldtos.cards.CardDTO;
import com.google.gson.Gson;

import java.util.List;

public class CardController {
    private final Gson gson;
    private final DAOCard<Integer> daoCard;
    private final CardMapper cardMapper;

    public CardController(Gson gson, DAOCard<Integer> daoCard, CardMapper cardMapper) throws MylException {
        if (gson != null && daoCard != null && cardMapper != null) {
            this.gson = gson;
            this.daoCard = daoCard;
            this.cardMapper = cardMapper;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public String getCardsEndpoint(QueryString queryString) throws Exception {
        // get cards from DB
        List<Card> cards = daoCard.findAllPagedFiltered(queryString.getPage(),
                queryString.getPageSize(),
                queryString.getCosts(),
                queryString.getDamages(),
                queryString.getCollectionsIds(),
                queryString.getRaritiesIds(),
                queryString.getTypesIds(),
                queryString.getRacesIds(),
                queryString.getFormatsIds(),
                queryString.getKeyWordsIds(),
                List.of());

        return getCardsFromList(cards);
    }

    public String getCardsByName(QueryString queryString) throws Exception {
        List<Card> cards = daoCard.findAllPagedFiltered(queryString.getPage(), queryString.getPageSize(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                queryString.getNames());

        return getCardsFromList(cards);
    }

    public String getCardByIDEndpoint(int id) throws Exception {
        CardDTO cardDTO = cardMapper.toDTO(daoCard.findById(id));

        return gson.toJson(cardDTO);
    }

    private String getCardsFromList(List<Card> cards) {
        List<CardDTO> cardDTOs = cards.stream()
                .map(cardMapper::toDTO)
                .toList();

        return gson.toJson(cardDTOs);
    }
}
