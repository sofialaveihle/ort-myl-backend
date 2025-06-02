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
import ar.com.mylback.utils.entitydtomappers.users.UserMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.gson.Gson;
import users.PlayerDTO;
import users.StoreDTO;

public class AuthController {

    private static final Gson gson = new Gson();

    public String registerStore(String requestBody) throws Exception {

            StoreDTO dto = gson.fromJson(requestBody, StoreDTO.class);
            Store store = StoreMapper.fromDTO(dto);

            new DAO<>(Store.class).save(store);

            return "{\"message\": \"Tienda registrada correctamente\"}";
    }

    public String registerPlayer(String requestBody) throws Exception {

            PlayerDTO dto = gson.fromJson(requestBody, PlayerDTO.class);

            Player player = new Player();
            UserMapper.fromDTO(dto, player);

            new DAO<>(Player.class).save(player);

            return "{\"message\": \"Usuario registrado correctamente\"}";
    }

    public String loginUser(String authHeader) throws Exception {
        FirebaseInitializer.init();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Exception("Token inválido o ausente");
        }

        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
        if (!userRecord.isEmailVerified()) {
            throw new Exception("Debe verificar su correo electrónico antes de iniciar sesión");
        }

        DAOPlayer daoPlayer = new DAOPlayer();
        Player player = daoPlayer.findByUuidWithAssociations(uid);
        if (player != null) {
            PlayerDTO dto = PlayerMapper.toDTO(player);
            return gson.toJson(dto);
        }

        DAOStore daoStore = new DAOStore();
        Store store = daoStore.findByUuid(uid);
        if (store != null) {
            if (!store.isValid()) {
                throw new Exception("La tienda aún no fue validada por el equipo de MyL");
            }
            StoreDTO dto = StoreMapper.toDTO(store);
            return gson.toJson(dto);
        }

        throw new Exception("No existe un usuario asociado a este UID");
    }

    public String deleteAccount(String authHeader) throws Exception {
        FirebaseInitializer.init();

        String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

        DAOPlayer daoPlayer = new DAOPlayer();
        Player player = daoPlayer.findByUuidWithAssociations(uid);
        if (player != null) {
            daoPlayer.delete(player);
            FirebaseAuth.getInstance().deleteUser(uid);
            return "{\"message\": \"Cuenta de jugador eliminada correctamente\"}";
        }

        DAOStore daoStore = new DAOStore();
        Store store = daoStore.findByUuid(uid);
        if (store != null) {
            daoStore.delete(store);
            FirebaseAuth.getInstance().deleteUser(uid);
            return "{\"message\": \"Cuenta de tienda eliminada correctamente\"}";
        }

        throw new Exception("No se encontró una cuenta con ese UID");
    }

}
