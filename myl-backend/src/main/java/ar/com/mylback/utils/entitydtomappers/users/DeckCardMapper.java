package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.DeckCard;
import ar.com.mylback.dal.entities.users.DeckCardId;
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

    public DeckCard toEntity(DeckCardDTO deckCardDTO) {
        DeckCard deckCard = new DeckCard();
        if (deckCardDTO != null) {
            deckCard.setCard(cardMapper.toEntity(deckCardDTO.getCard()));
            deckCard.setQuantity(deckCardDTO.getQuantity());

            deckCard.setId(new DeckCardId());
            if (deckCardDTO.getDeck() != null) {
                deckCard.getId().setDeckId(deckCardDTO.getDeck().getId());
            }
            if (deckCardDTO.getCard() != null) {
                deckCard.getId().setCardId(deckCardDTO.getCard().getId());
            }
        }
        return deckCard;
    }
}
