package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Deck;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.users.DeckDTO;

import java.util.stream.Collectors;

public class DeckMapper {
    private final DeckCardMapper deckCardMapper;

    public DeckMapper(DeckCardMapper deckMapper) {
        this.deckCardMapper = deckMapper;
    }

    @NotNull
    public DeckDTO toDTO(Deck deck) {
        DeckDTO deckDTO = new DeckDTO();
        if (deck != null) {
            deckDTO.setId(deck.getId());
            deckDTO.setName(deck.getName());
            deckDTO.setDescription(deck.getDescription());

            deckDTO.setCards(deck.getCards().stream().map(deckCardMapper::toDTO).collect(Collectors.toSet()));
        }
        return deckDTO;
    }

    @NotNull
    public Deck toEntity(DeckDTO deckDTO) {
        Deck deck = new Deck();
        if (deckDTO != null) {
            deck.setId(deckDTO.getId());
            deck.setName(deckDTO.getName());
            deck.setDescription(deckDTO.getDescription());

            deck.setCards(deckDTO.getCards().stream().map(deckCardMapper::toEntity).collect(Collectors.toSet()));
        }
        return deck;
    }
}
