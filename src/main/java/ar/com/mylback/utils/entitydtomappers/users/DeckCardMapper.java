package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import jakarta.validation.constraints.NotNull;
import users.DeckCardDTO;

public class DeckCardMapper {
    @NotNull
    public static DeckCardDTO toDTO(DeckCard deckCard) {
        DeckCardDTO deckCardDTO = new DeckCardDTO();
        if (deckCard != null) {
//            deckCardDTO.setCard(CardMapper.toDTO(deckCard.getCard()));
            deckCardDTO.setQuantity(deckCard.getQuantity());
        }
        return deckCardDTO;
    }
}
