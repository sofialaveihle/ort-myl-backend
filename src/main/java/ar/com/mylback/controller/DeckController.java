package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.users.DAODeck;
import ar.com.mylback.dal.crud.users.DAODeckCard;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.entities.users.Deck;
import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.users.DeckMapper;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import ar.com.myldtos.users.DeckDTO;
import com.google.gson.Gson;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class DeckController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final DAOPlayer daoPlayer;
    private final DAODeck daoDeck;
    private final DAODeckCard daoDeckCard;
    private final DeckMapper deckMapper;
    private final String authToken;

    public DeckController(Gson gson, FirebaseAuthValidator firebaseAuthValidator,
                          DAOPlayer daoPlayer, DAODeck daoDeck, DAODeckCard daoDeckCard,
                          DeckMapper deckMapper, String authToken) throws MylException {
        if (gson != null && firebaseAuthValidator != null &&
                daoPlayer != null && daoDeck != null && daoDeckCard != null &&
                deckMapper != null &&
                authToken != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
            this.daoPlayer = daoPlayer;
            this.daoDeck = daoDeck;
            this.daoDeckCard = daoDeckCard;
            this.deckMapper = deckMapper;
            this.authToken = authToken;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse addDeck(String body) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para agregar el mazo")));
            }

            Deck deck = deckMapper.toEntity(gson.fromJson(body, DeckDTO.class));
            deck.setPlayer(player);

            Set<DeckCard> deckCards = deck.getCards();
            deck.setCards(null);

            for (DeckCard dc : deckCards) {
                int quantity = dc.getQuantity();
                if (quantity <= 0 || quantity > 3) {
                    return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400,
                            "Cantidad inválida para la carta con ID " + dc.getId().getCardId() + ": " + quantity)));
                }
            }

            // save empty deck
            daoDeck.save(deck);

            // get deck hibernate generated id
            // load the id to the cards and save them
            deckCards.forEach(dc -> {
                dc.getId().setDeckId(deck.getId());
                dc.setDeck(deck);
            });
            daoDeckCard.save(deckCards);

            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Mazo id: " + deck.getId() + " agregado exitosamente")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al agregar el mazo")));
        }
    }

    public HttpResponse getPlayerDecks() {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado")));
            }

            List<Deck> decks = daoDeck.findAll(player.getUuid());
            List<DeckDTO> deckDTOs = decks.stream().map(deckMapper::toDTO).toList();

            return new HttpResponse(200, gson.toJson(deckDTOs));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al obtener mazos")));
        }
    }

    public HttpResponse getPlayerDeckById(Integer id) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado")));
            }

            Deck deck = daoDeck.findById(player.getUuid(), id);
            if (deck == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Mazo no encontrado id: " + id)));
            }
            DeckDTO deckDTO = deckMapper.toDTO(deck);
            return new HttpResponse(200, gson.toJson(deckDTO));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al obtener el mazo")));
        }
    }

    public HttpResponse updateDeck(Integer id, String body) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado")));
            }

            Deck deckFromClient = deckMapper.toEntity(gson.fromJson(body, DeckDTO.class));

            Deck deck = daoDeck.findById(player.getUuid(), id);
            if (deck == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Mazo no encontrado id: " + id)));
            }

            deck.setName(deckFromClient.getName());
            deck.setDescription(deckFromClient.getDescription());

            daoDeck.update(deck);
            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Mazo id: " + deck.getId() + " actualizado")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al actualizar el mazo")));
        }
    }

    public HttpResponse deleteDeck(Integer id) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado")));
            }

            daoDeck.deleteById(id);
            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Mazo eliminado")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al eleminar el mazo: " + id)));
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
