package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Deck;
import jakarta.validation.constraints.NotNull;
import users.DeckDTO;

import java.util.stream.Collectors;

public class DeckMapper {
    @NotNull
    public static DeckDTO toDTO(Deck deck) {
        DeckDTO deckDTO = new DeckDTO();
        if (deck != null) {
            deckDTO.setId(deck.getId());
            deckDTO.setName(deck.getName());
            deckDTO.setPlayer(PlayerMapper.toDTO(deck.getPlayer()));

            deckDTO.setCards(deck.getCards().stream().map(DeckCardMapper::toDTO).collect(Collectors.toSet()));
        }
        return deckDTO;
    }
}
