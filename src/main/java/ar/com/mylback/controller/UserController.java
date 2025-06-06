package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import com.google.gson.Gson;
import ar.com.myldtos.users.PlayerDTO;

public class UserController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final DAOPlayer daoPlayer;

    public UserController(Gson gson, FirebaseAuthValidator firebaseAuthValidator, DAOPlayer daoPlayer) throws MylException {
        if (gson != null && firebaseAuthValidator != null && daoPlayer != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
            this.daoPlayer = daoPlayer;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse updatePlayer(String requestBody, String authHeader) {
        try {
            String uid = firebaseAuthValidator.validateAndGetUid(authHeader);

            PlayerDTO dto = gson.fromJson(requestBody, PlayerDTO.class);
            Player player = daoPlayer.findByUuid(uid);

            if (player == null) {
                return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "Jugador no encontrado")));
            }

            player.setName(dto.getName());
            daoPlayer.update(player);

            return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Jugador actualizado correctamente")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error de validación", e.getMessage())));
        }
    }
}
