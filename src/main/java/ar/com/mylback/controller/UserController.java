package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.auth.FirebaseInitializer;
import ar.com.mylback.dal.crud.cards.DAO;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.crud.users.DAOStore;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.entitydtomappers.users.PlayerMapper;
import ar.com.mylback.utils.entitydtomappers.users.StoreMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import users.PlayerDTO;
import users.PlayerRegisterDTO;
import users.StoreDTO;

public class UserController {

    private static final Gson gson = new Gson();

    public String updatePlayer(String requestBody, String authHeader) {
        FirebaseInitializer.init();

        try {
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

        } catch (Exception e) {
            System.err.println("Error al actualizar jugador: " + e.getMessage());
            return "{\"error\": \"Error al actualizar jugador: " + e.getMessage() + "\"}";
        }
    }
}
