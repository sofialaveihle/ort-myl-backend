package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.crud.users.DAOPlayerCard;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.dal.entities.users.PlayerCard;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.users.PlayerCardMapper;
import ar.com.mylback.utils.url.QueryString;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import ar.com.myldtos.users.PlayerCardDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlayerCardController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final String authToken;
    private final DAOPlayer daoPlayer;
    private final DAOPlayerCard daoPlayerCard;
    private final PlayerCardMapper playerCardMapper;

    public PlayerCardController(Gson gson, FirebaseAuthValidator firebaseAuthValidator,
                                DAOPlayer daoPlayer, DAOPlayerCard daoPlayerCard,
                                PlayerCardMapper playerCardMapper,
                                String authToken) throws MylException {
        if (gson != null && firebaseAuthValidator != null
                && daoPlayer != null && daoPlayerCard != null
                && playerCardMapper != null
                && authToken != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
            this.daoPlayer = daoPlayer;
            this.daoPlayerCard = daoPlayerCard;
            this.playerCardMapper = playerCardMapper;
            this.authToken = authToken;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse addPlayerCards(String body) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para agregar sus cartas")));
            }

            Type listType = new TypeToken<List<PlayerCardDTO>>() {
            }.getType();
            List<PlayerCard> playerCards = playerCardMapper.toEntityList(gson.fromJson(body, listType));
            if (playerCards.isEmpty()) {
                return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "No hay cartas para agregar")));
            }

            List<PlayerCard> playerCardsToInsert = new ArrayList<>();
            for (PlayerCard playerCard : playerCards) {
                playerCard.getId().setPlayerUuid(player.getUuid());
                PlayerCard dbPlayerCard = daoPlayerCard.findById(playerCard.getId());
                if (dbPlayerCard != null) {
                    dbPlayerCard.setQuantity(dbPlayerCard.getQuantity() + playerCard.getQuantity());
                    daoPlayerCard.update(dbPlayerCard); // TODO add update collection
                } else {
                    playerCardsToInsert.add(playerCard);
                }
            }
            daoPlayerCard.save(playerCardsToInsert);
            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Cartas del jugador: " + player.getName() + " agregadas exitosamente")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al agregar las cartas del jugador")));
        }
    }

    public HttpResponse getPlayerCards(QueryString queryString) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para obtener sus cartas")));
            }
            List<PlayerCardDTO> playerCards = playerCardMapper.toDTOList(daoPlayerCard.findAll(player.getUuid(), queryString.getPage(), queryString.getPageSize()));

            return new HttpResponse(200, gson.toJson(playerCards));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al obtener las cartas del jugador")));
        }
    }

    public HttpResponse deletePlayerCards(String body) {
        try {
            Player player = getPlayer();
            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Usuario no encontrado para eliminar sus cartas")));
            }

            Type listType = new TypeToken<List<PlayerCardDTO>>() {
            }.getType();
            List<PlayerCard> playerCardsToDelete = playerCardMapper.toEntityList(gson.fromJson(body, listType));
            if (playerCardsToDelete.isEmpty()) {
                return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "No hay cartas para agregar")));
            }

            for (PlayerCard playerCardToDelete : playerCardsToDelete) {
                playerCardToDelete.getId().setPlayerUuid(player.getUuid());
                PlayerCard dbPlayerCard = daoPlayerCard.findById(playerCardToDelete.getId());
                if (dbPlayerCard != null) {
                    if (dbPlayerCard.getQuantity() - playerCardToDelete.getQuantity() > 0) {
                        dbPlayerCard.setQuantity(dbPlayerCard.getQuantity() - playerCardToDelete.getQuantity());
                        daoPlayerCard.update(dbPlayerCard); // TODO update in one tx
                    } else {
                        daoPlayerCard.deleteById(playerCardToDelete.getId()); // TODO delete in one tx
                    }
                } else {
                    return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "No existe la carta a eliminar")));
                }
            }

            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Cartas del jugador: " + player.getName() + " eliminadas exitosamente")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, "Error al eliminar las cartas del jugador")));
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
