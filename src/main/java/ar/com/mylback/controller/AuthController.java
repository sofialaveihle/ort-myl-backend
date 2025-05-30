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
import users.PlayerDTO;
import users.PlayerRegisterDTO;
import users.StoreDTO;
import users.StoreRegisterDTO;

public class AuthController {

    private static final Gson gson = new Gson();

    public String registerStore(String requestBody) {
        try {
            StoreRegisterDTO dto = gson.fromJson(requestBody, StoreRegisterDTO.class);

            Store store = new Store();
            store.setUuid(dto.getUuid());
            store.setEmail(dto.getEmail());
            store.setName(dto.getStoreName());
            store.setAddress(dto.getAddress());
            store.setPhoneNumber(dto.getPhone());
            store.setUrl(dto.getUrl());
            store.setValid(false);

            new DAO<>(Store.class).save(store);

            return "{\"message\": \"Tienda registrada correctamente\"}";

        } catch (Exception e) {
            System.err.println("Error al registrar tienda: " + e.getMessage());
            return "{\"error\": \"Error al registrar tienda: " + e.getMessage() + "\"}";
        }
    }

    public String registerPlayer(String requestBody) {
        try {
            PlayerRegisterDTO dto = gson.fromJson(requestBody, PlayerRegisterDTO.class);

            Player player = new Player();
            player.setUuid(dto.getUuid());
            player.setEmail(dto.getEmail());
            player.setName(dto.getName());

            new DAO<>(Player.class).save(player);

            return "{\"message\": \"Usuario registrado correctamente\"}";

        } catch (Exception e) {
            System.err.println("Error al registrar usuario:");
            e.printStackTrace();
            return "{\"error\": \"Error al registrar usuario: " + e.getMessage() + "\"}";
        }
    }

    public String loginUser(String authHeader) {
        FirebaseInitializer.init();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "{\"error\": \"Token inválido o ausente\"}";
        }

        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);


            // Validar que el correo esté verificado en Firebase
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            if (!userRecord.isEmailVerified()) {
                return "{\"error\": \"Debe verificar su correo electrónico antes de iniciar sesión\"}";
            }

            // Buscar en tabla de players
            DAOPlayer daoPlayer = new DAOPlayer();
            Player player = daoPlayer.findByUuidWithAssociations(uid);
            if (player != null) {
                PlayerDTO dto = PlayerMapper.toDTO(player);
                return gson.toJson(dto);
            }

            // Buscar en tabla de tiendas
            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uid);
            if (store != null) {
                if (!store.isValid()) {
                    return "{\"error\": \"La tienda aún no fue validada por el equipo de MyL\"}";
                }
                StoreDTO dto = StoreMapper.toDTO(store);
                return gson.toJson(dto);
            }

            return "{\"error\": \"No existe un usuario asociado a este UID\"}";

        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            return "{\"error\": \"Error durante el login: " + e.getMessage() + "\"}";
        }
    }

    public String deleteAccount(String authHeader) {
        FirebaseInitializer.init();

        try {
            String uid = FirebaseAuthValidator.validateAndGetUid(authHeader);

            // Reviso en tabla de players si esta el UID
            DAOPlayer daoPlayer = new DAOPlayer();
            Player player = daoPlayer.findByUuidWithAssociations(uid);
            if (player != null) {
                daoPlayer.delete(player);
                FirebaseAuth.getInstance().deleteUser(uid);
                return "{\"message\": \"Cuenta de jugador eliminada correctamente\"}";
            }

            // Reviso en tabla de tiendas si esta el UID
            DAOStore daoStore = new DAOStore();
            Store store = daoStore.findByUuid(uid);
            if (store != null) {
                daoStore.delete(store);
                FirebaseAuth.getInstance().deleteUser(uid);
                return "{\"message\": \"Cuenta de tienda eliminada correctamente\"}";
            }

            return "{\"error\": \"No se encontró una cuenta con ese UID\"}";
        } catch (Exception e) {
            System.err.println("Error al eliminar cuenta: " + e.getMessage());
            return "{\"error\": \"No se pudo eliminar la cuenta\"}";
        }
    }

}
