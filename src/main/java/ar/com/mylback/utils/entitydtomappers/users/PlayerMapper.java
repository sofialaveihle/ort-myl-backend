package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Player;
import jakarta.validation.constraints.NotNull;
import ar.com.myldtos.users.PlayerDTO;

import java.util.stream.Collectors;

public class PlayerMapper extends UserMapper {
    private final DeckMapper deckMapper;
    private final PlayerCardMapper playerCardMapper;

    public PlayerMapper(DeckMapper deckMapper, PlayerCardMapper playerCardMapper) {
        this.deckMapper = deckMapper;
        this.playerCardMapper = playerCardMapper;
    }

    @NotNull
    public PlayerDTO toDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        if (player != null) {
            super.toDTO(player, playerDTO);
            playerDTO.setDecks(player.getDecks().stream().map(deckMapper::toDTO).collect(Collectors.toSet()));
            playerDTO.setCards(player.getPlayerCards().stream().map(playerCardMapper::toDTO).collect(Collectors.toSet()));
        }
        return playerDTO;
    }
}
