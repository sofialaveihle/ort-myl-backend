package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.entities.users.Player;
import com.google.gson.Gson;
import users.PlayerDTO;

public class UserController {

    private static final Gson gson = new Gson();

    public String updatePlayer(String requestBody, String authHeader) throws Exception {
        FirebaseInitializer.init();

        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        PlayerDTO dto = gson.fromJson(requestBody, PlayerDTO.class);
        DAOPlayer daoPlayer = new DAOPlayer();
        Player player = daoPlayer.findByUuid(uid);

        if (player == null) {
            return "{\"error\": \"Jugador no encontrado\"}";
        }

        player.setName(dto.getName());
        daoPlayer.update(player);

        return "{\"message\": \"Jugador actualizado correctamente\"}";
    }
}
