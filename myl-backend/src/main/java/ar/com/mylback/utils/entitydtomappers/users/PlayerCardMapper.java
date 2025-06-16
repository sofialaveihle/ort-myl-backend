package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.PlayerCard;
import ar.com.mylback.dal.entities.users.PlayerCardId;
import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import ar.com.myldtos.users.PlayerCardDTO;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCardMapper {
    private final CardMapper cardMapper;

    public PlayerCardMapper(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    @NotNull
    public PlayerCardDTO toDTO(PlayerCard playerCard) {
        PlayerCardDTO playerCardDTO = new PlayerCardDTO();
        if (playerCard != null) {
            playerCardDTO.setCard(cardMapper.toDTO(playerCard.getCard()));
            playerCardDTO.setQuantity(playerCard.getQuantity());
        }
        return playerCardDTO;
    }

    @NotNull
    public PlayerCard toEntity(PlayerCardDTO playerCardDTO) {
        PlayerCard playerCard = new PlayerCard();
        if (playerCardDTO != null) {
            playerCard.setCard(cardMapper.toEntity(playerCardDTO.getCard()));

            playerCard.setQuantity(playerCardDTO.getQuantity());
            playerCard.setId(new PlayerCardId());
            if (playerCardDTO.getCard() != null) {
                playerCard.getId().setCardId(playerCardDTO.getCard().getId());
            }
            if (playerCardDTO.getPlayer() != null) {
                playerCard.getId().setPlayerUuid(playerCardDTO.getPlayer().getUuid());
            }
        }
        return playerCard;
    }

    @NotNull
    public List<PlayerCard> toEntityList(List<PlayerCardDTO> playerCardDTOs) {
        List<PlayerCard> playerCards = new ArrayList<>();
        if (playerCardDTOs != null && !playerCardDTOs.isEmpty()) {
            playerCardDTOs.stream().map(this::toEntity).forEach(playerCards::add);
        }
        return playerCards;
    }

    @NotNull
    public List<PlayerCardDTO> toDTOList(List<PlayerCard> playerCards) {
        List<PlayerCardDTO> playerCardsDTOs = new ArrayList<>();
        if (playerCards != null && !playerCards.isEmpty()) {
            playerCards.stream().map(this::toDTO).forEach(playerCardsDTOs::add);
        }
        return playerCardsDTOs;
    }
}
