package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.utils.entitydtomappers.cards.CardMapper;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.users.DeckCardDTO;

public class DeckCardMapper {
    private final CardMapper cardMapper;

    public DeckCardMapper(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    @NotNull
    public DeckCardDTO toDTO(DeckCard deckCard) {
        DeckCardDTO deckCardDTO = new DeckCardDTO();
        if (deckCard != null) {
            deckCardDTO.setCard(cardMapper.toDTO(deckCard.getCard()));
            deckCardDTO.setQuantity(deckCard.getQuantity());
        }
        return deckCardDTO;
    }
}
