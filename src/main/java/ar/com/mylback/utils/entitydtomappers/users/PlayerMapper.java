package ar.com.mylback.utils.entitydtomappers.users;

import ar.com.mylback.dal.entities.users.Player;
import jakarta.validation.constraints.NotNull;
import users.PlayerDTO;

import java.util.stream.Collectors;

public class PlayerMapper {
    @NotNull
    public static PlayerDTO toDTO(Player player) {
        PlayerDTO playerDTO = new PlayerDTO();
        if (player != null) {
            UserMapper.toDTO(player, playerDTO);
            playerDTO.setDecks(player.getDecks().stream().map(DeckMapper::toDTO).collect(Collectors.toSet()));
            playerDTO.setCards(player.getPlayerCards().stream().map(PlayerCardMapper::toDTO).collect(Collectors.toSet()));
        }
        return playerDTO;
    }
}
