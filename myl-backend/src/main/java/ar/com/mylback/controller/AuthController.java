package ar.com.mylback.controller;

import ar.com.mylback.auth.FirebaseAuthValidator;
import ar.com.mylback.dal.crud.DAO;
import ar.com.mylback.dal.crud.users.DAOPlayer;
import ar.com.mylback.dal.crud.users.DAOStore;
import ar.com.mylback.dal.entities.users.Player;
import ar.com.mylback.dal.entities.users.Store;
import ar.com.mylback.utils.HttpResponse;
import ar.com.mylback.utils.MylException;
import ar.com.mylback.utils.entitydtomappers.users.PlayerMapper;
import ar.com.mylback.utils.entitydtomappers.users.StoreMapper;
import ar.com.mylback.utils.entitydtomappers.users.UserMapper;
import ar.com.myldtos.ErrorTemplateDTO;
import ar.com.myldtos.SuccessTemplateDTO;
import ar.com.myldtos.users.PlayerDTO;
import ar.com.myldtos.users.StoreDTO;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import jakarta.validation.constraints.NotNull;

public class AuthController {
    private final Gson gson;
    private final FirebaseAuthValidator firebaseAuthValidator;
    private final DAOPlayer daoPlayer;
    private final DAOStore daoStore;
    private final UserMapper userMapper;
    private final StoreMapper storeMapper;
    private final PlayerMapper playerMapper;

    public AuthController(Gson gson, FirebaseAuthValidator firebaseAuthValidator, DAOPlayer daoPlayer, DAOStore daoStore, UserMapper userMapper, PlayerMapper playerMapper, StoreMapper storeMapper) throws MylException {
        if (gson != null && firebaseAuthValidator != null && daoPlayer != null && daoStore != null && userMapper != null && storeMapper != null && playerMapper != null) {
            this.gson = gson;
            this.firebaseAuthValidator = firebaseAuthValidator;
            this.daoPlayer = daoPlayer;
            this.daoStore = daoStore;
            this.userMapper = userMapper;
            this.storeMapper = storeMapper;
            this.playerMapper = playerMapper;
        } else {
            throw new MylException(MylException.Type.NULL_PARAMETER);
        }
    }

    public HttpResponse registerStore(String requestBody) {
        try {
            StoreDTO dto = gson.fromJson(requestBody, StoreDTO.class);
            Store store = storeMapper.fromDTO(dto);

            new DAO<>(Store.class).save(store);

            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Tienda registrada correctamente")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al registrar tienda", e.getMessage())));
        }
    }

    public HttpResponse registerPlayer(String requestBody) {
        try {
            PlayerDTO dto = gson.fromJson(requestBody, PlayerDTO.class);

            Player player = new Player();
            userMapper.fromDTO(dto, player);

            daoPlayer.save(player);
            return new HttpResponse(201, gson.toJson(new SuccessTemplateDTO("Usuario registrado correctamente")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al registrar usuario", e.getMessage())));
        }
    }

    @NotNull
    public HttpResponse me(String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new HttpResponse(401, gson.toJson(new ErrorTemplateDTO(401, "Token inválido o ausente")));
            }

            String uid = firebaseAuthValidator.validateAndGetUid(authHeader);

            UserRecord userRecord = firebaseAuthValidator.getUserByUID(uid);
            if (!userRecord.isEmailVerified()) {
                return new HttpResponse(403, gson.toJson(new ErrorTemplateDTO(403, "Debe verificar su correo electrónico antes de iniciar sesión")));
            }

            Player player = daoPlayer.findByUid(uid);
            if (player != null) {
                PlayerDTO dto = playerMapper.toDTO(player);
                return new HttpResponse(200, gson.toJson(dto));
            }

            Store store = daoStore.findByUid(uid);
            if (store != null) {
/*                if (!store.isValid()) {
                    return new HttpResponse(403, gson.toJson(new ErrorTemplateDTO(403, "La tienda aún no fue validada por el equipo de MyL")));
                }*/
                if (!store.isValid()) {
                    store.setValid(true);
                    daoStore.update(store);
                }
                StoreDTO dto = storeMapper.toDTO(store);
                return new HttpResponse(200, gson.toJson(dto));
            }

            return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "No existe un usuario asociado a este UID")));
        } catch (MylException e) {
            return new HttpResponse(500, gson.toJson(new ErrorTemplateDTO(500, e.getMessage())));
        }
    }

    public HttpResponse deleteAccount(String authHeader) {
        try {
            String uid = firebaseAuthValidator.validateAndGetUid(authHeader);

            Player player = daoPlayer.findByUidWithJoin(uid);
            if (player != null) {
                daoPlayer.delete(player);
                firebaseAuthValidator.deleteUser(uid);
                return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Cuenta de jugador eliminada correctamente")));
            }

            Store store = daoStore.findByUid(uid);
            if (store != null) {
                daoStore.delete(store);
                firebaseAuthValidator.deleteUser(uid);
                return new HttpResponse(200, gson.toJson(new SuccessTemplateDTO("Cuenta de tienda eliminada correctamente")));
            }

            return new HttpResponse(404, gson.toJson(new ErrorTemplateDTO(404, "No se encontró una cuenta con ese UID")));
        } catch (MylException e) {
            return new HttpResponse(400, gson.toJson(new ErrorTemplateDTO(400, "Error al eliminar cuenta", e.getMessage())));
        }
    }
}
