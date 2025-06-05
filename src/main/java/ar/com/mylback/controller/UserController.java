package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.MylException;
import com.google.gson.Gson;
import users.PlayerDTO;

public class UserController {
    private final Gson gson;
    private final DAOPlayer daoPlayer;

    public UserController(Gson gson, DAOPlayer daoPlayer) throws MylException {
        if (gson != null) {
            this.gson = gson;
            this.daoPlayer = daoPlayer;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public String updatePlayer(String requestBody, String authHeader) throws Exception {
        FirebaseInitializer.init();

        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        PlayerDTO dto = gson.fromJson(requestBody, PlayerDTO.class);
        Player player = daoPlayer.findByUuid(uid);

        if (player == null) {
            return "{\"error\": \"Jugador no encontrado\"}";
        }

        player.setName(dto.getName());
        daoPlayer.update(player);

        return "{\"message\": \"Jugador actualizado correctamente\"}";
    }
}
