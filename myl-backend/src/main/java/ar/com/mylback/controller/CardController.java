package ar.com.mylback.controller;

import ar.com.mylback.dal.crud.cards.DAOCard;
import ar.com.mylback.dal.entities.cards.Card;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.url.QueryString;
import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import ar.com.myldtos.ErrorTemplateDTO;
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

    public HttpResponse getCardsEndpoint(QueryString queryString) {
        try {
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

            if (cards == null || cards.isEmpty()) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "No se encontraron cartas con los filtros proporcionados")));
            }

            return getCardsFromList(cards);
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al buscar cartas", e.getMessage())));
        }
    }

    public HttpResponse getCardsByName(QueryString queryString) {
        try {
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

            if (cards == null || cards.isEmpty()) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "No se encontraron cartas con ese nombre")));
            }

            return getCardsFromList(cards);
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al buscar cartas", e.getMessage())));
        }
    }

    public HttpResponse getCardById(int id) {
        try {
            Card card = daoCard.findById(id);
            if (card == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Carta no encontrada")));
            }

            CardDTO cardDTO = cardMapper.toDTO(card);
            return new HttpResponse(200, gson.toJson(cardDTO));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al buscar la carta", e.getMessage())));
        }
    }

    private HttpResponse getCardsFromList(List<Card> cards) {
        List<CardDTO> cardDTOs = cards.stream()
                .map(cardMapper::toDTO)
                .toList();

        return new HttpResponse(200, gson.toJson(cardDTOs));
    }
}
