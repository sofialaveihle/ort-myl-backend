package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.PlayerCard;
//import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.users.PlayerCardDTO;

public class PlayerCardMapper {
    @NotNull
    public PlayerCardDTO toDTO(PlayerCard playerCard) {
        PlayerCardDTO playerCardDTO = new PlayerCardDTO();
        if (playerCard != null) {
//            playerCardDTO.setCard(CardMapper.toDTO(playerCard.getCard()));
            playerCardDTO.setQuantity(playerCard.getQuantity());
        }
        return playerCardDTO;
    }
}
