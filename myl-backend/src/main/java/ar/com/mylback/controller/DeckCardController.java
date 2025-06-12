package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.users.DAODeck;
import ar.com.mylback.dal.crud.users.DAODeckCard;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.entities.users.Deck;
import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.dal.entities.users.DeckCardId;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import ar.com.myldtos.users.AddCardToDeckDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeckCardController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final DAOPlayer daoPlayer;
    private final DAODeck daoDeck;
    private final DAODeckCard daoDeckCard;
    private final String authToken;

    public DeckCardController(Gson gson, FirebaseAuthValidator firebaseAuthValidator,
                              DAOPlayer daoPlayer, DAODeck daoDeck, DAODeckCard daoDeckCard,
                              String authToken) throws MylException {
        if (gson != null && firebaseAuthValidator != null &&
                daoPlayer != null && daoDeck != null && daoDeckCard != null &&
                authToken != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
            this.daoPlayer = daoPlayer;
            this.daoDeck = daoDeck;
            this.daoDeckCard = daoDeckCard;
            this.authToken = authToken;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse addCardToDecks(Integer cardId, String body) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para agregar las cartas al mazo")));
            }

            Type listType = new TypeToken<List<AddCardToDeckDTO>>() {
            }.getType();
            List<AddCardToDeckDTO> addCardToDeckDTOs = gson.fromJson(body, listType);

            List<DeckCard> deckCards = new ArrayList<>();
            for (AddCardToDeckDTO addCardToDeckDTO : addCardToDeckDTOs) {
                // validate if the Deck exists
                Deck deck = daoDeck.findById(player.getUuid(), addCardToDeckDTO.getDeckId());
                if (deck != null && (addCardToDeckDTO.getQuantity() > 0 || addCardToDeckDTO.getQuantity() <= 3)) {
                    DeckCard deckCard = new DeckCard();
                    DeckCardId deckCardId = new DeckCardId();
                    deckCardId.setCardId(cardId);
                    deckCardId.setDeckId(addCardToDeckDTO.getDeckId());
                    deckCard.setId(deckCardId);
                    deckCard.setQuantity(addCardToDeckDTO.getQuantity());
                    DeckCard existingDeckCard = daoDeckCard.findById(deckCardId);
                    if (existingDeckCard != null) {
                        int totalQuantity = existingDeckCard.getQuantity() + deckCard.getQuantity();
                        if (totalQuantity <= 3) {
                            deckCard.setQuantity(totalQuantity);
                            daoDeckCard.update(deckCard);
                        } else {
                            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Max 3 cartas iguales por mazo")));
                        }
                    } else {
                        deckCards.add(deckCard);
                    }
                } else {
                    return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Mazo invalido para cargar las cartas")));
                }
            }

            daoDeckCard.save(deckCards);

            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Cartas agregadas exitosamente")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al guardar las cartas")));
        }
    }

    public HttpResponse deleteCardFromDeckDeck(Integer cardId, Integer deckId) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para eliminar carta del mazo")));
            }

            DeckCardId deckCardId = new DeckCardId();
            deckCardId.setCardId(cardId);
            deckCardId.setDeckId(deckId);

            daoDeckCard.deleteById(deckCardId);

            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Carta eliminada exitosamente")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al eliminar la carta del mazo")));
        }
    }

    @Nullable
    private Player getPlayer() throws MylException {
        String uid = firebaseAuthValidator.validateAndGetUid(authToken);
        if (uid != null) {
            return daoPlayer.findByUid(uid);
        } else {
            throw new MylException(MylException.Type.NOT_FOUND);
        }
    }
}
