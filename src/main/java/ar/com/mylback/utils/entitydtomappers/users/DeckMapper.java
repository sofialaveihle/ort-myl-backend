package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Deck;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.users.DeckDTO;

import java.util.stream.Collectors;

public class DeckMapper {
//    private final PlayerMapper playerMapper;
    private final DeckCardMapper deckCardMapper;

    public DeckMapper(/*PlayerMapper playerMapper, */DeckCardMapper deckMapper) {
//        this.playerMapper = playerMapper;
        this.deckCardMapper = deckMapper;
    }

    @NotNull
    public DeckDTO toDTO(Deck deck) {
        DeckDTO deckDTO = new DeckDTO();
        if (deck != null) {
            deckDTO.setId(deck.getId());
            deckDTO.setName(deck.getName());
//            deckDTO.setPlayer(playerMapper.toDTO(deck.getPlayer()));

            deckDTO.setCards(deck.getCards().stream().map(deckCardMapper::toDTO).collect(Collectors.toSet()));
        }
        return deckDTO;
    }
}
